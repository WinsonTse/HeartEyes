package io.github.winsontse.hearteyes.util.rxbus.event;

import io.github.winsontse.hearteyes.util.rxbus.event.base.BaseEvent;

/**
 * Created by winson on 16/5/28.
 */
public class UidEvent extends BaseEvent {
    private String uid;

    public UidEvent(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UidEvent{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
