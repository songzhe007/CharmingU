package zhe.charmu.model;

/**
 * Created by songz on 1/2/2018.
 */

public class DailyMetrics  {

    protected int _id;
    protected float weight;
    protected float bmi;
    protected String date;
    protected float step;

   public  DailyMetrics(int id, String _date, float _weight, float _bmi, float _step){
        _id = id;
        date = _date;
        weight = _weight;
        bmi = _bmi;
        step = _step;
    }
}
