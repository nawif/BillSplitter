package com.example.android.Data;

/**
 * Created by Nawif on 2/20/18.
 */

public class friends {
    String name;
    double total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total =Math.floor(total * 100) / 100;
    }

    public friends(String name, double total) {
        this.name = name;
        this.total = total;
    }
}
