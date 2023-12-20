package com.cymal.bo;

import lombok.Data;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

/**
 * 描述：模拟单核CPU系统队列和正在运行态的线程
 * @author 杨宸
 * @date 2023-12-16
 */
@Data
public class SystemQueue {

    /**
     * 维护PCB
     */
    private LinkedList<PCB> pcbQueue;

    /**
     * 系统的就绪队列
     */
    private BlockingQueue<PCB> readyQueue;

    /**
     * 正在运行的PCB
     */
    private PCB runningPCB;

    /**
     * 系统的阻塞队列
     */
    private BlockingQueue<PCB> blockingQueue;

}
