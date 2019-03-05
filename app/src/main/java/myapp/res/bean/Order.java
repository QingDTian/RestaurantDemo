package myapp.res.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order implements Serializable {
    public class PS implements Serializable {
        public Product product;
        public int count;

    }

    private Restaurant restaurant;
    private int count;
    private int id;
    private User user;
    private float price;
    private List<PS> ps;
    //public List<Product> products = new ArrayList<>();
    public HashMap<Product, Integer> productIntegerHashMap = new HashMap();


    public void addProduct(Product product) {
        Integer integer = productIntegerHashMap.get(product);
        if (integer == null || integer <= 0) {
            productIntegerHashMap.put(product, 1);
        } else {
            productIntegerHashMap.put(product, count++);

        }
    }

    public void removeProduct(Product product) {
        Integer integer = productIntegerHashMap.get(product);
        if (integer > 0) {
            productIntegerHashMap.put(product, count--);
        }

    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<PS> getPs() {
        return ps;
    }

    public void setPs(List<PS> ps) {
        this.ps = ps;
    }



}
