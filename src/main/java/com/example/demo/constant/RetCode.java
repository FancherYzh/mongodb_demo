package com.example.demo.constant;

/**
 * @author yzh
 */

public enum RetCode {

    SUCCESS("10000","操作成功"),

    FAILURE("00000","操作失败"),

    BLANK_STUDENT_NAME("00010", "学生名称不能为空");



    private String code;

    private String msg;

    RetCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }

}
