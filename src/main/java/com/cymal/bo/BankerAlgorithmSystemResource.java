package com.cymal.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;

/**
 * 描述：银行家算法系统资源对象, 其中数据结构蕴藏隐藏关系如下：
 * 1. Need[i][j] = Max[i][j] - Aollcation[i][j]
 * 2. 上面三种变量任取两个即可
 * @author 杨宸
 * @date 2023-12-09
 */
@Data
@Accessors(chain = true)
public class BankerAlgorithmSystemResource {

    /**
     * 包含 m 个元素, 其中每一个元素代表一类可利用的资源数组, 如果available[j]=k, 表示现在系统
     * 中J类有K个
     */
    private int [] available;

    /**
     * 包含 nxm 个元素, 它定义了系统中n个进程对m类资源的最大需求。如果max[i][j]=k, 代表进程i对j
     * 类资源的最大数组为k
     */
    private int [] [] max;

    /**
     * 包含 nxm 个元素, 它定义了系统中每一类资源已经分配给每一个进程的资源数, 如果allocation[i][j]=k,
     * 代表进程i已经分配j类资源k个
     */
    private int [] [] allocation;

    /**
     * 包含 nxm 个元素, 它表示了每一个进程尚需要的资源数。如果need[i][j]=k, 则进程i还需要j资源k个方能完成任务
     */
    private int [] [] need;

    @Override
    public String toString() {
        return "system resource = [" +
                "\navailable=" + Arrays.toString(available) +
                "\nmax=" + Arrays.deepToString(max) +
                "\nallocation=" + Arrays.deepToString(allocation) +
                "\nneed=" + Arrays.deepToString(need) +
                "\n]";
    }
}
