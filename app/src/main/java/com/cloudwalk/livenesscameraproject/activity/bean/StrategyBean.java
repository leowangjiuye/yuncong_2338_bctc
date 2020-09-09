package com.cloudwalk.livenesscameraproject.activity.bean;

public class StrategyBean {
    public boolean isCheck;
    public int strategyId;
    public String strategyName;

    public StrategyBean(boolean isCheck, int strategyId, String strategyName) {
        this.isCheck = isCheck;
        this.strategyName = strategyName;
        this.strategyId = strategyId;
    }
}
