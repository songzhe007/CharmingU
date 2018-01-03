package zhe.charmu.model;

/**
 * Created by songz on 1/2/2018.
 */

public class Item {

    public String title;
    public float amount;
    public boolean ifHealthy;
    public float price;

   public Item(String _title, float _amount, boolean _ifHealthy, float _price){
        title = _title;
        amount = _amount;
        ifHealthy = _ifHealthy;
        price = _price;
    }
}
