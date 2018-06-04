package com.alquaid.BillSplitter.Data;

/**
 * Created by Nawif on 2/20/18.
 */

public class nameAndTotal {
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

    public nameAndTotal(String name, double total) {
        this.name = name;
        this.total = total;
    }
    public nameAndTotal(nameAndTotal NameAndTotal) {
        this.name = NameAndTotal.name;
        this.total = NameAndTotal.total;
    }
}
