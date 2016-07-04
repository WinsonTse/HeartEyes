package io.github.winsontse.hearteyes.util.rxbus.event;

import android.os.Parcel;
import android.os.Parcelable;

import io.github.winsontse.hearteyes.util.rxbus.event.base.BaseEvent;

/**
 * Created by winson on 16/5/31.
 */
public class PushEvent extends BaseEvent implements Parcelable {

    /**
     * 重启刷新main
     */
    public static final int RESTART_INIT_PAGE = 1001;
    /**
     * 重启刷新main并通知朋友
     */
    public static final int RESTART_AND_NOTIFY_FRIEND = 1002;


    private int type;
    private String from;
    private String alert;
    private String content;

    public PushEvent(int type) {
        this.type = type;
    }


    public PushEvent(int type, String alert) {
        this.type = type;
        this.alert = alert;
    }

    public PushEvent(int type, String alert, String content) {
        this.type = type;
        this.alert = alert;
        this.content = content;
    }

    public PushEvent(int type, String from, String alert, String content) {
        this.type = type;
        this.from = from;
        this.alert = alert;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.from);
        dest.writeString(this.alert);
        dest.writeString(this.content);
    }

    protected PushEvent(Parcel in) {
        this.type = in.readInt();
        this.from = in.readString();
        this.alert = in.readString();
        this.content = in.readString();
    }

    public static final Creator<PushEvent> CREATOR = new Creator<PushEvent>() {
        @Override
        public PushEvent createFromParcel(Parcel source) {
            return new PushEvent(source);
        }

        @Override
        public PushEvent[] newArray(int size) {
            return new PushEvent[size];
        }
    };
}



