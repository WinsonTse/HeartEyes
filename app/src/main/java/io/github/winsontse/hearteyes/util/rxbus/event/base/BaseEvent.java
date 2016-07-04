package io.github.winsontse.hearteyes.util.rxbus.event.base;

/**
 * Created by winson on 16/5/28.
 */
public class BaseEvent {
    private int code;
    private String content;

    public BaseEvent(int type) {
        this.code = type;
    }



    public BaseEvent() {
    }

    public BaseEvent(int type, String msg) {
        this.code = type;
        this.content = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
