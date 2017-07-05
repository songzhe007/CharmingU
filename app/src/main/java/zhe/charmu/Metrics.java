package zhe.charmu;
import java.util.Date;

class Item{

     protected String title;
     protected float amount;
     protected boolean ifHealthy;
     protected float price;

    Item(String _title, float _amount, boolean _ifHealthy, float _price){
        title = _title;
        amount = _amount;
        ifHealthy = _ifHealthy;
        price = _price;
    }
}

class DailyMetrics{

    protected int _id;
    protected float weight;
    protected float bmi;
    protected String date;
    protected float step;

    DailyMetrics(int id, String _date, float _weight, float _bmi, float _step){
        _id = id;
        date = _date;
        weight = _weight;
        bmi = _bmi;
        step = _step;
    }
}

class MonthlyMetrics{

    protected float height;
    protected float budget;
    protected float remain;
    protected float healthy;
    protected float unhealthy;
    protected Date budget_set_date;

    MonthlyMetrics(float _height, float _budget, float _remain,
                   float _healthy, float _unhealthy, Date _date){
        height = _height;
        budget = _budget;
        remain = _remain;
        healthy = _healthy;
        unhealthy = _unhealthy;
        budget_set_date = _date;
    }
}

