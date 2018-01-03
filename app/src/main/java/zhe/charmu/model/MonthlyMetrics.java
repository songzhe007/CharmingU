package zhe.charmu.model;

import java.util.Date;

/**
 * Created by songz on 1/2/2018.
 */

public class MonthlyMetrics {
    public float height;
    public float budget;
    public float remain;
    public float healthy;
    public float unhealthy;
    public Date budget_set_date;

    public MonthlyMetrics(float _height, float _budget, float _remain,
                   float _healthy, float _unhealthy, Date _date){
        height = _height;
        budget = _budget;
        remain = _remain;
        healthy = _healthy;
        unhealthy = _unhealthy;
        budget_set_date = _date;
    }
}
