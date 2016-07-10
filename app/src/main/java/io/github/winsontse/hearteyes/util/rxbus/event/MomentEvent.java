package io.github.winsontse.hearteyes.util.rxbus.event;

import com.avos.avoscloud.AVObject;

import io.github.winsontse.hearteyes.util.rxbus.event.base.BaseEvent;

/**
 * Created by winson on 16/7/4.
 */

public class MomentEvent extends BaseEvent {
    public static final int REFRESH_MOMENT_LIST = 1;
    public static final int UPDATE_MOMENT_LIST_ITEM = 2;
    private AVObject avObject;

    public MomentEvent(int type) {
        super(type);
    }

    public MomentEvent(int code, int postion, AVObject avObject) {
        super(code, postion);
        this.avObject = avObject;
    }

    public AVObject getAvObject() {
        return avObject;
    }

    public void setAvObject(AVObject avObject) {
        this.avObject = avObject;
    }
}
