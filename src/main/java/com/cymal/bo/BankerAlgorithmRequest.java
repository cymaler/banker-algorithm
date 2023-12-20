package com.cymal.bo;

import lombok.Data;

/**
 * 描述：进程申请资源请求
 * @author 杨宸
 * @date 2023-12-16
 */
@Data
public class BankerAlgorithmRequest {

    /**
     * 进程索引
     */
    private int process;

    /**
     * 资源索引
     */
    private int [] resource;

    /**
     * 资源数
     */
    private int [] k;

    /**
     * 本次请求的请求资源个数
     */
    private int reqLength;

}
