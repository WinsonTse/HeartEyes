package io.github.winsontse.hearteyes.util.rxbus.event.base;

/**
 * Created by winson on 16/5/28.
 */
public class BaseEvent {
    private int type;
    private String msg;

    public BaseEvent() {
    }

    public BaseEvent(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
