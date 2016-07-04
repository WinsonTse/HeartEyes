package io.github.winsontse.hearteyes.util.rxbus.event;

import io.github.winsontse.hearteyes.util.rxbus.event.base.BaseEvent;

/**
 * Created by winson on 16/7/4.
 */

public class MomentEvent extends BaseEvent {
    public static final int REFRESH_MOMENT_LIST = 1;

    public MomentEvent(int type) {
        super(type);
    }
}
