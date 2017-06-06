package com.lz.helper.utils.workdomain;

/**
 * Created by LZ on 2017/1/23.
 */

public class ResultDomain {

    private int work_hashcode;//工作线程hashcode
    private long result_time_stmp;//结果获取时间
    private int result_code;//结果标志位
    private int result_type;//结果类型
    private String resume;//结果描述

    public int getWork_hashcode() {
        return work_hashcode;
    }

    public void setWork_hashcode(int work_hashcode) {
        this.work_hashcode = work_hashcode;
    }

    public long getResult_time_stmp() {
        return result_time_stmp;
    }

    public void setResult_time_stmp(long result_time_stmp) {
        this.result_time_stmp = result_time_stmp;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public int getResult_type() {
        return result_type;
    }

    public void setResult_type(int result_type) {
        this.result_type = result_type;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
