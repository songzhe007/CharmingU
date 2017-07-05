package zhe.charmu;

import android.app.Application;

import java.util.Date;

public class Globals extends Application {

    private static Globals instance;

    MonthlyMetrics monthMetrics;
    boolean ifTodaySet;

    private Globals(){
        ifTodaySet = false;
    }

    public MonthlyMetrics getMonthMetrics(){
        return this.monthMetrics;
    }
    public boolean getIfTodaySet(){
        return this.ifTodaySet;
    }

    public void setMonthMetrics(MonthlyMetrics _monthMetrics){
        monthMetrics = _monthMetrics;
    }
    public void setToday(){
        ifTodaySet = true;
    }

    public void setHeight(float height){
        monthMetrics.height = height;
    }
    public void setBudget(float budget){
        monthMetrics.budget = budget;
    }
    public void setRemain(float remain){
        monthMetrics.remain = remain;
    }
    public void setHealthy(float healthy){
        monthMetrics.healthy = healthy;
    }
    public void setUnhealthy(float unhealthy){
        monthMetrics.unhealthy = unhealthy;
    }
    public void setDate(){
        monthMetrics.budget_set_date = new Date();
    }

    public static synchronized Globals getInstance(){
        if(instance == null){
            instance = new Globals();
        }
        return instance;
    }
}
