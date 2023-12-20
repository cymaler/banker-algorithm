package com.cymal.utils;

import java.util.concurrent.TimeUnit;

/**
 * 描述：模拟执行工具
 * @author 杨宸
 * @date 2023-12-11
 */
public class SimulationUtils {

    /**
     * 描述：模拟执行 second 秒
     * @author 杨宸
     * @date 2023-12-11
     */
    public static void simulationExecute(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 描述：模拟执行 second 秒
     * @author 杨宸
     * @date 2023-12-11
     */
    public static void slice(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
