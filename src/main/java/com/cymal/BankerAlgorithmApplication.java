package com.cymal;

import com.cymal.bo.BankerAlgorithmSystemResource;
import com.cymal.service.BankerAlgorithmService;
import com.cymal.service.CPU;
import com.cymal.service.CommandService;

import java.util.Scanner;
import java.util.stream.Stream;

/**
 * 描述：本程序为控制台模拟应用, 模拟进程申请资源过程中银行家算法的执行流程
 *
 * 模拟命令：
 * systemresource: 查看当前系统的资源
 * java -ps [pid]：查看对应进程的PCB
 * readyqueue：查看当前系统的就绪队列
 * blockqueue：查询当前系统的阻塞队列
 * runningprocess：查询当前正在运行的进程
 * java -process [pid] -resource [resourceId1,resourceId2,...,resourceIdn] -count [count1,count2,...,countn]：进程申请资源, 使用银行家算法观察是否可分配资源
 *
 * @author 杨宸
 * @date 2023-12-16
 */
public class BankerAlgorithmApplication {

    /**
     * 描述：主流程启动
     * @author 杨宸
     * @date 2023-12-19
     */
    public static void main(String[] args) {

        // 初始化单核CPU, 时间片设置为20ms
        CPU cpu = new CPU(20);
        cpu.start();

        // 为模拟的OS配置银行家算法
        BankerAlgorithmService service = BankerAlgorithmService.getInstance();
        service.initSystemResource(initSystemResource());
        CommandService command = new CommandService(service);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("#bankalgorithm: ");
            command.execute(sc.nextLine());
        }

    }

    /**
     * 描述：初始化系统资源, 默认为咱们操作系统作业的初始资源矩阵
     * @author 杨宸
     * @date 2023-12-18
     */
    private static BankerAlgorithmSystemResource initSystemResource() {
        BankerAlgorithmSystemResource systemResource = new BankerAlgorithmSystemResource();
        systemResource.setAvailable(new int[] {1, 5, 2, 0});
        systemResource.setNeed(Stream.of(new int[][] {
                        {0, 0, 0, 0},
                        {0, 7, 5, 0},
                        {1, 0, 0, 2},
                        {0, 0, 2, 0},
                        {0, 6, 4, 2}
        }).toArray(int[][]::new));
        systemResource.setMax(Stream.of(new int[][] {
                        {0, 0, 1, 2},
                        {1, 7, 5, 0},
                        {2, 3, 5, 6},
                        {0, 6, 5, 2},
                        {0, 6, 5, 6}
        }).toArray(int[][]::new));
        systemResource.setAllocation(Stream.of(new int[][] {
                        {0, 0, 1, 2},
                        {1, 0, 0, 0},
                        {1, 3, 5, 4},
                        {0, 6, 3, 2},
                        {0, 0, 1, 4}
        }).toArray(int[][]::new));
        return systemResource;
    }

}
