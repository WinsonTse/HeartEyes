package io.github.winsontse.hearteyes.util.rxbus.event.base;

/**
 * Created by winson on 16/5/28.
 */
public class BaseEvent {
    private int code;
    private String content;
    private int postion;

    public BaseEvent(int type) {
        this.code = type;
    }


    public BaseEvent() {
    }

    public BaseEvent(int code, int postion) {
        this.code = code;
        this.postion = postion;
    }

    public BaseEvent(int type, String msg) {
        this.code = type;
        this.content = msg;
    }

    public BaseEvent(int code, String content, int postion) {
        this.code = code;
        this.content = content;
        this.postion = postion;
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

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }
}
