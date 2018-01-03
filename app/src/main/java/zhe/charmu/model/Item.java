package zhe.charmu.model;

/**
 * Created by songz on 1/2/2018.
 */

public class Item {

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
