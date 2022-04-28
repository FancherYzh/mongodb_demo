package com.example.demo.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yzh
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 2763523580667734693L;

    private String retCode;

    private T data;

    private long pageNum;

    private long pageSize;

    private long total;

    public Result(String retCode, T data) {
        this.retCode = retCode;
        this.data = data;
    }

    public Result(String retCode, T data, long pageNum, long pageSize, long total) {
        this.retCode = retCode;
        this.data = data;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }
}
