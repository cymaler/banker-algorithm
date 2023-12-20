package com.cymal.service;

import com.cymal.bo.PCB;
import com.cymal.constants.ProcessStatusConstants;
import com.cymal.utils.SimulationUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述：模拟单核CPU
 * @author 杨宸
 * @date 2023-12-19
 */
@Slf4j
public class CPU {

    /**
     * 为单核CPU分配线程池
     */
    private static final ExecutorService runService = Executors.newSingleThreadExecutor();

    /**
     * 时间片
     */
    private final long timeslice;

    /**
     * 描述：构造函数
     * @author 杨宸
     * @date 2023-12-19
     */
    public CPU(long timeslice) {
        this.timeslice = timeslice;
    }

    /**
     * 描述：模拟操作系统CPU工作
     * @author 杨宸
     * @date 2023-12-19
     */
    public void start() {
        runService.submit(() -> {
            log.info("cpu is starting...");
            while (true) {
                SimulationUtils.slice(timeslice);
                PCB runningPCB = BankerAlgorithmService.SYSTEM_QUEUE.getRunningPCB();
                if (runningPCB != null) {
                    long startTime = System.currentTimeMillis();
                    SimulationUtils.simulationExecute(10);
                    BankerAlgorithmService.SYSTEM_QUEUE.setRunningPCB(null);
                    BankerAlgorithmService.SYSTEM_QUEUE.getReadyQueue().offer(runningPCB);
                    runningPCB.setStatus(ProcessStatusConstants.READY);
                    log.info("cpu execute a process:[{}] consuming:[{}] ms", runningPCB, System.currentTimeMillis() - startTime);
                    releaseResources(runningPCB);
                    notifyBlockingPcb();
                    System.out.print("#bankalgorithm:");
                }
            }
        });
    }

    /**
     * 描述：如果进程运行完后need全部为0, 则释放资源
     * @author 杨宸
     * @date 2023-12-20
     */
    private void releaseResources(PCB runningPCB) {
        long startTime = System.currentTimeMillis();
        int [][] need = BankerAlgorithmService.systemResource.getNeed();
        int [][] allocation = BankerAlgorithmService.systemResource.getAllocation();
        int [] available = BankerAlgorithmService.systemResource.getAvailable();
        boolean result = true;
        for(int index = 0; index < need[runningPCB.getProcess()].length; index++) {
            if (need[runningPCB.getProcess()][index] != 0) {
                result = false;
            }
        }
        if (result) {
            for(int index = 0; index < allocation[runningPCB.getProcess()].length; index++) {
                available[index] = available[index] + allocation[runningPCB.getProcess()][index];
                allocation[runningPCB.getProcess()][index] = 0;
            }
            log.info("process:[{}] release resource consuming:[{}] ms", runningPCB, System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 描述；唤醒阻塞队列中的全部Pcb并加入就绪队列
     * @author 杨宸
     * @date 2023-12-19
     */
    private void notifyBlockingPcb() {
        while (!BankerAlgorithmService.SYSTEM_QUEUE.getBlockingQueue().isEmpty()) {
            PCB pcb = BankerAlgorithmService.SYSTEM_QUEUE.getBlockingQueue().poll();
            BankerAlgorithmService.SYSTEM_QUEUE.getReadyQueue().offer(pcb);
            log.info("notify a process:[{}] form blockqueue", pcb);
        }
    }

}
