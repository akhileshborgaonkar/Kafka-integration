package com.aim.kafka.domain;

import java.io.Serializable;

public class PayloadBean implements Serializable {
    public static final long serialVersionUID = 42L;

    private String topic;
    private String data;

    public PayloadBean(){
    //default
    }

    public PayloadBean(String topic, String data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
