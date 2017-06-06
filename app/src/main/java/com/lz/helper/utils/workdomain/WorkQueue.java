package com.lz.helper.utils.workdomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LZ on 2017/1/23.
 */

public class WorkQueue {
    private List<Work> works=new ArrayList<Work>();
    private static WorkQueue instance;

    public static WorkQueue getInstance(){
        if(null==instance){
            instance=new WorkQueue();
        }
        return instance;
    }

    /**向队列中添加工作*/
    public void putWork(Work work){
        works.add(work);
    }

    /**接受工作结果*/
    public void resiveResult(ResultDomain result){
        for(Work work:works){
            if(work.getHashcode()==result.getResult_code()){
                work.setResult(result);
                work.setDone(true);
            }
        }
    }

    /**删除队列中任务*/
    public void deleteWork(Work work){
        works.remove(work);
    }
    /**根据hashcode删除队列中任务*/
    public void deleteWorkByHashCode(int work_hashcode){
        for(Work work:works){
            if(work.getHashcode()==work_hashcode){
                works.remove(work);
                break;
            }
        }
    }
    /**根据id删除队列中任务*/
    public void deleteWorkById(int work_id){
        for(Work work:works){
            if(work.getId()==work_id){
                works.remove(work);
                break;
            }
        }
    }

    /**获取未完成任务*/
    public List<Work> checkAllAlive(){
        List<Work> works2=new ArrayList<Work>();
        for(Work work:works){
            if(!work.isDone()){
                works2.add(work);
            }
        }
        return works2;
    }

    /**获取全部已完成任务*/
    public List<Work> checkAllDone(){
        List<Work> works2=new ArrayList<Work>();
        for(Work work:works){
            if(work.isDone()){
                works2.add(work);
            }
        }
        return works2;
    }
}
