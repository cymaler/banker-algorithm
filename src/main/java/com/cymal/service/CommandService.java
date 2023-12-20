package com.cymal.service;

import com.cymal.bo.BankerAlgorithmRequest;
import com.cymal.bo.PCB;
import com.cymal.utils.CommandUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

/**
 * 描述：命令模式
 * @author 杨宸
 * @date 2023-12-18
 */
public class CommandService {

    /**
     * 银行家算法服务
     */
    private final BankerAlgorithmService service;

    /**
     * 描述：构造函数
     * @author 杨宸
     * @date 2023-12-18
     */
    public CommandService(BankerAlgorithmService bankerAlgorithmService) {
        this.service = bankerAlgorithmService;
    }

    /**
     * 描述：执行命令
     * @author 杨宸
     * @date 2023-12-18
     */
    public void execute(String ... commands) {
        for(String command : commands) {
            if (command.startsWith("java -ps")) {
                int lastIndex = command.lastIndexOf(" ");
                Integer processId = Integer.valueOf(command.substring(lastIndex + 1));
                Optional<PCB> any = BankerAlgorithmService.SYSTEM_QUEUE
                        .getPcbQueue()
                        .stream()
                        .filter(pcb -> Objects.equals(processId, pcb.getProcess()))
                        .findAny();
                if (any.isPresent()) {
                    System.out.println(any.get());
                } else {
                    System.out.println("process is not found.");
                }
            } else if (command.startsWith("readyqueue")) {
                BlockingQueue<PCB> readyQueue = BankerAlgorithmService.SYSTEM_QUEUE.getReadyQueue();
                System.out.println("readyQueue: ");
                readyQueue.stream().forEach(System.out::println);
            } else if (command.startsWith("blockqueue")) {
                BlockingQueue<PCB> blockingQueue = BankerAlgorithmService.SYSTEM_QUEUE.getBlockingQueue();
                System.out.println("blockingQueue: ");
                blockingQueue.stream().forEach(System.out::println);
            } else if (command.startsWith("runningprocess")) {
                PCB runningPCB = BankerAlgorithmService.SYSTEM_QUEUE.getRunningPCB();
                System.out.println("runningProcess: " + runningPCB);
            } else if (command.startsWith("java -process")) {
                String[] params = command.split(" ");
                BankerAlgorithmRequest request = new BankerAlgorithmRequest();
                request.setProcess(Integer.parseInt(params[2]));
                request.setResource(CommandUtils.toArray(params[4]));
                request.setK(CommandUtils.toArray(params[6]));
                request.setReqLength(request.getResource().length);
                service.bankerAlgorithm(request);
            } else if (command.startsWith("systemresource")) {
                System.out.println(BankerAlgorithmService.systemResource);
            } else if (StringUtils.isNotBlank(command) || !Objects.equals("\n", command)){
                System.out.println(command + "is not found.");
            } else {
                System.out.print("");
            }
        }
    }

}
