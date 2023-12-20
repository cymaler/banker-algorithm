package com.cymal.service;

import com.cymal.bo.BankerAlgorithmRequest;
import com.cymal.bo.BankerAlgorithmSystemResource;
import com.cymal.bo.PCB;
import com.cymal.bo.SystemQueue;
import com.cymal.constants.ProcessStatusConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述： 银行家算法核心业务类
 * @author 杨宸
 * @date 2023-12-11
 */
@Slf4j
public class BankerAlgorithmService {

    /**
     * 银行家算法单例
     */
    private static BankerAlgorithmService INSTANCE;

    /**
     * 模拟系统资源
     */
    public static final BankerAlgorithmSystemResource systemResource;

    /**
     * 阻塞队列
     */
    public static final SystemQueue SYSTEM_QUEUE = new SystemQueue();

    /**
     * 阻塞队列最大进程数
     */
    private static final int maxProcessNum = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 是否进行安全检查
     */
    private static final Boolean IS_SAFE_CHECK = true;

    /**
     * 资源分配线程池
     */
    private static final ExecutorService alloctorService = Executors.newFixedThreadPool(maxProcessNum, new ThreadFactory() {

        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "bank-algorithm-alloctor-" + count.getAndIncrement());
        }

    });

    /**
     * 描述：私有的无参构造函数
     * @author 杨宸
     * @date 2023-12-18
     */
    private BankerAlgorithmService() {

    }

    /**
     * 初始化系统资源
     */
    static {

        systemResource = new BankerAlgorithmSystemResource();
        SYSTEM_QUEUE.setBlockingQueue(new ArrayBlockingQueue<>(maxProcessNum));
        SYSTEM_QUEUE.setReadyQueue(new ArrayBlockingQueue<>(maxProcessNum));
        SYSTEM_QUEUE.setRunningPCB(null);
        SYSTEM_QUEUE.setPcbQueue(new LinkedList<>());

    }

    /**
     * 描述：双重检查锁的单例模式, 生成唯一实例
     * @author 杨宸
     * @date 2023-12-18
     */
    public static BankerAlgorithmService getInstance() {
        if (INSTANCE == null) {
            synchronized (BankerAlgorithmService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BankerAlgorithmService();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 描述： 初始化系统资源
     * @author 杨宸
     * @date 2023-12-09
     */
    public void initSystemResource(BankerAlgorithmSystemResource req) {
        systemResource
                .setAvailable(req.getAvailable())
                .setMax(req.getMax())
                .setAllocation(req.getAllocation())
                .setNeed(req.getNeed());
        int processNum = systemResource.getMax().length;
        int resourceNum = systemResource.getAvailable().length;
        initSystemQueue(processNum, resourceNum);
    }

    /**
     * 描述：初始化系统队列
     * @author 杨宸
     * @date 2023-12-18
     */
    private static void initSystemQueue(int processNum, int resourceNum) {
        for(int i = 0; i < processNum; i++) {
            PCB pcb = new PCB().setProcess(i);
            pcb.setResource(new int [resourceNum]);
            pcb.setK(new int[resourceNum]);
            SYSTEM_QUEUE.getReadyQueue().add(pcb);
            SYSTEM_QUEUE.getPcbQueue().add(pcb);
        }
    }

    /**
     * 描述：银行家算法核心流程
     * @author 杨宸
     * @date 2023-12-11
     */
    public void bankerAlgorithm(BankerAlgorithmRequest request) {
        final int process = request.getProcess();
        final int [] resource = request.getResource();
        final int [] k = request.getK();
        final int [][] need = systemResource.getNeed();
        final int [] available = systemResource.getAvailable();
        final int [][] allocation = systemResource.getAllocation();
        final int reqLength = request.getReqLength();
        if (ArrayUtils.isEmpty(resource) || ArrayUtils.isEmpty(k) || reqLength != resource.length || reqLength != k.length) {
            log.info("[resource] isn't matched [count].");
            return;
        }
        if (SYSTEM_QUEUE.getRunningPCB() != null) {
            log.info("cpu is busying.");
            return;
        }
        PCB pcb = getSystemPCB(process);
        if (!SYSTEM_QUEUE.getReadyQueue().contains(pcb)) {
            log.info("request process is not in readyqueue.");
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(reqLength);
        final boolean [] result = new boolean [reqLength];
        for (final AtomicInteger index = new AtomicInteger(0); index.get() < reqLength; index.getAndIncrement()) {
            final int currentIndex = index.get();
            alloctorService.submit(() -> {
                try {
                    if (k[currentIndex] > need[process][resource[currentIndex]]) {
                        result[currentIndex] = false;
                        log.info("the request resource [{}] of process [{}] is above the need.", currentIndex, process);
                        return;
                    }
                    if (k[currentIndex] > available[currentIndex]) {
                        result[currentIndex] = false;
                        log.info("the request resource [{}] of process [{}] is above the available.", currentIndex, process);
                        return;
                    }
                    final int tryAvailable = available[currentIndex] - k[currentIndex];
                    final int tryAollcation = allocation[process][currentIndex] + k[currentIndex];
                    final int tryNeed = need[process][currentIndex] - k[currentIndex];
                    if (safeCheckAlgorithm(currentIndex, tryAvailable)) {
                        available[currentIndex] = tryAvailable;
                        allocation[process][currentIndex] = tryAollcation;
                        need[process][currentIndex] = tryNeed;
                        result[currentIndex] = true;
                        log.info("the request resource [{}] of process [{}] allocate success.", currentIndex, process);
                    } else {
                        result[currentIndex] = false;
                        log.info("the request resource [{}] of process [{}] is safe check failed.", currentIndex, process);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
            updateProcessStatusAndExecute(process, result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 描述：修改系统PCB状态
     * @author 杨宸
     * @date 2023-12-19
     */
    private void updateProcessStatusAndExecute(int process, boolean[] result) {
        PCB pcb = getSystemPCB(process);
        boolean flag = false;
        for(int index = 0; index < result.length; index++) {
            if (!result[index]) {
                pcb.getResource()[index] = 1;
                flag = true;
            }
        }
        SYSTEM_QUEUE.getReadyQueue().remove(pcb);
        if (flag) {
            pcb.setStatus(ProcessStatusConstants.BLOCKED);
            SYSTEM_QUEUE.getBlockingQueue().add(pcb);
        } else {
            pcb.setStatus(ProcessStatusConstants.RUNNING);
            SYSTEM_QUEUE.setRunningPCB(pcb);
        }
    }

    /**
     * 描述：根据进程号获得系统Pcb
     * @author 杨宸
     * @date 2023-12-19
     */
    private PCB getSystemPCB(int process) {
        Optional<PCB> pcbOptional = SYSTEM_QUEUE.getPcbQueue().stream().filter(p -> Objects.equals(process, p.getProcess())).findFirst();
        return pcbOptional.orElse(null);
    }

    /**
     * 描述：安全检查算法
     * @author 杨宸
     * @date 2023-12-11
     */
    private Boolean safeCheckAlgorithm(int resource, int tryAvailable) {
        int work = tryAvailable;
        Boolean[] finish = new Boolean[systemResource.getAvailable().length];
        initFinish(finish);
        while (IS_SAFE_CHECK) {
            boolean found = false;
            for (int index = 0; index < finish.length; index++) {
                if (!finish[index] && systemResource.getNeed()[index][resource] <= work) {
                    work = work + systemResource.getAllocation()[index][resource];
                    finish[index] = true;
                    found = true;
                }
            }
            if (!found) {
                return Arrays.stream(finish).noneMatch(BooleanUtils::isFalse);
            }
        }
        return Boolean.TRUE; // 关闭安全检查
    }

    /**
     * 描述：初始化Finish数组
     * @author 杨宸
     * @date 2023-12-11
     */
    private void initFinish(Boolean[] finish) {
        for(int i = 0; i < finish.length; i++) {
            finish[i] = Boolean.FALSE;
        }
    }

}
