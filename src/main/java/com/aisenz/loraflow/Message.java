package com.aisenz.loraflow;

/**
 * Created by Administrator on 2016/12/27.
 */
public class Message {
    private String deveui;
    private int fport;
    private String data;

    public Message(String deveui, int fport, String data) {
        this.deveui = deveui;
        this.fport = fport;
        this.data = data;
    }

    public String getDeveui() {
        return deveui;
    }

    public void setDeveui(String deveui) {
        this.deveui = deveui;
    }

    public int getFport() {
        return fport;
    }

    public void setFport(int fport) {
        this.fport = fport;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
