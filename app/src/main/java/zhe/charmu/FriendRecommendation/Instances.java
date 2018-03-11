package zhe.charmu.FriendRecommendation;


import zhe.charmu.model.User;
public class Instances {

    private double Cal;
    private double degree;
    private User user;
    public Instances(double Cal, double degree, User user){
        this.Cal = Cal;
        this.degree = degree;
        this.user = user;
    }
    public double getCal(){
        return this.Cal;
    }
    public double getDegree(){
        return this.degree;
    }
    public User getUser(){
        return this.user;
    }
}
