package com.company.models;

public class ModelEvent {
    private Long id;
    private String nameEvent;
    private String timeEvent;
    private int temperature;
    private int processor;
    private int usedMemory;
    private int freeMemory;
    private boolean sent;
    private String sentTime;
    private String additInfo;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(int usedMemory) {
        this.usedMemory = usedMemory;
    }

    public int getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(int freeMemory) {
        this.freeMemory = freeMemory;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getAdditInfo() {
        return additInfo;
    }

    public void setAdditInfo(String additInfo) {
        this.additInfo = additInfo;
    }
}
