package com.marsianets.orderbook;

public class Client {

    private String name;
    private String phone;

    public Client() {
        this.setName("");
        this.setPhone("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
