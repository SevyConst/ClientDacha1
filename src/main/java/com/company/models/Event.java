package com.company.models;

public class Event {
    private long id;
    private String nameEvent;
    private long timeEvent;
    private int temperature;
    private int processor;
    private int usedMemory;
    private int freeMemory;
    private boolean sent;
    private long sentTime;
    private boolean sentApproved;
    private long sentApprovedTime;
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

    public long getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(long timeEvent) {
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

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public boolean isSentApproved() {
        return sentApproved;
    }

    public void setSentApproved(boolean sentApproved) {
        this.sentApproved = sentApproved;
    }

    public long getSentApprovedTime() {
        return sentApprovedTime;
    }

    public void setSentApprovedTime(long sentApprovedTime) {
        this.sentApprovedTime = sentApprovedTime;
    }

    public String getAdditInfo() {
        return additInfo;
    }

    public void setAdditInfo(String additInfo) {
        this.additInfo = additInfo;
    }
}
