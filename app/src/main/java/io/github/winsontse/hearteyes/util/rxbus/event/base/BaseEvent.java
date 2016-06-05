package io.github.winsontse.hearteyes.util.rxbus.event.base;

/**
 * Created by winson on 16/5/28.
 */
public class BaseEvent {
    private int type;
    private String content;

    public BaseEvent(int type) {
        this.type = type;
    }



    public BaseEvent() {
    }

    public BaseEvent(int type, String msg) {
        this.type = type;
        this.content = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
