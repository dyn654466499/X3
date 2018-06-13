package com.terminus.facerecord.events;

public class BaseEvent {
    protected int eventFlag;
    protected Object eventObject;

    public int getEventFlag() {
        return eventFlag;
    }

    public BaseEvent setEventFlag(int eventFlag) {
        this.eventFlag = eventFlag;
        return this;
    }

    public Object getEventObject() {
        return eventObject;
    }

    public BaseEvent setEventObject(Object object) {
        this.eventObject = object;
        return this;
    }
}
