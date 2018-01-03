package zhe.charmu.model;

/**
 * Created by songz on 1/2/2018.
 */

public class DailyMetrics  {

    public int _id;
    public float weight;
    public float bmi;
    public String date;
    public float step;

   public  DailyMetrics(int id, String _date, float _weight, float _bmi, float _step){
        _id = id;
        date = _date;
        weight = _weight;
        bmi = _bmi;
        step = _step;
    }
}
