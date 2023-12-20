package com.cymal.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：命令工具类
 * @author 杨宸
 * @date 2023-12-19
 */
public class CommandUtils {

    /**
     * 描述：将命令转化为银行家算法的入参
     * @author 杨宸
     * @date 2023-12-19
     */
    public static int[] toArray(String command) {
        if (StringUtils.isNotBlank(command)) {
            return Arrays.stream(command.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
        return null;
    }

}
