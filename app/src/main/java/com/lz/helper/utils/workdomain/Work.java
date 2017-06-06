package com.lz.helper.utils.workdomain;

/**
 * Created by LZ on 2017/1/23.
 */

public class Work {
    private long hashcode;//工作线程hash值
    private long timestmp;//任务时间戳
    private int id;//存储任务id
    private int work_type;//任务类型
    private String resume;//任务描述
    private boolean done =false;//是否已完成
    private boolean check;//是否已被查看
    private ResultDomain result;

    public long getHashcode() {
        return hashcode;
    }

    public void setHashcode(long hashcode) {
        this.hashcode = hashcode;
    }

    public long getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(long timestmp) {
        this.timestmp = timestmp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWork_type() {
        return work_type;
    }

    public void setWork_type(int work_type) {
        this.work_type = work_type;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public ResultDomain getResult() {
        return result;
    }

    public void setResult(ResultDomain result) {
        this.result = result;
    }
}
