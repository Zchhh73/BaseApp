package com.zch.base.model;

/**
 * 通用Base组件
 *
 * Description: 基本接口Model
 * Created by zch on 2022/8/1 13:07
 *
 **/
public class BaseNaiveModel<T> {
    protected boolean canModify;
    protected Object maxCount;

    public BaseNaiveModel() {
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public Object getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Object maxCount) {
        this.maxCount = maxCount;
    }
}
