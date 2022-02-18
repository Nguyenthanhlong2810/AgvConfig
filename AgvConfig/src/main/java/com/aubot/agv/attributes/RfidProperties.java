package com.aubot.agv.attributes;

public class RfidProperties {
    private int id;
    private boolean extraCards;
    private boolean exConnection;
    private int stopTime;
    private int connWaitingTime;

    public RfidProperties(int id, boolean extraCards, boolean exConnection, int stopTime, int connWaitingTime) {
        this.id = id;
        this.extraCards = extraCards;
        this.exConnection = exConnection;
        this.stopTime = stopTime;
        this.connWaitingTime = connWaitingTime;
    }

    public RfidProperties() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExtraCards() {
        return extraCards;
    }

    public void setExtraCards(boolean extraCards) {
        this.extraCards = extraCards;
    }

    public boolean isExConnection() {
        return exConnection;
    }

    public void setExConnection(boolean exConnection) {
        this.exConnection = exConnection;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public int getConnWaitingTime() {
        return connWaitingTime;
    }

    public void setConnWaitingTime(int connWaitingTime) {
        this.connWaitingTime = connWaitingTime;
    }
}
