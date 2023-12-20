package com.cymal.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 描述：描述一个进程的相关信息包括但不限于进程号, 状态, 等待时间的类型和数量
 * @author 杨宸
 * @date 2023-12-16
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@ToString
public class PCB {

    /**
     * 描述：构造函数
     * @author 杨宸
     * @date 2023-12-16
     */
    public PCB(int process, int [] resource) {
        this.process = process;
        this.resource = resource;
    }

    /**
     * 描述：构造函数
     * @author 杨宸
     * @date 2023-12-16
     */
    public PCB(int process, int [] resource, int [] k) {
        this.process = process;
        this.resource = resource;
        this.k = k;
    }

    /**
     * 进程号
     */
    private int process;

    /**
     * 0：就绪态  1：运行态  2：阻塞态
     */
    private int status;

    /**
     * 如果该进程阻塞了, 是由于哪个资源阻塞的。如果没有阻塞该值为-1
     */
    private int [] resource;

    /**
     * 如果当前进程阻塞了, 需要等待多少资源
     */
    private int [] k;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PCB pcb = (PCB) o;
        return process == pcb.process;
    }

    @Override
    public int hashCode() {
        return Objects.hash(process);
    }

}
