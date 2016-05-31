package io.github.winsontse.hearteyes.data.model.leancloud;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by winson on 16/5/31.
 */
public class PushMessage implements Parcelable {
    private int type;
    private String content;

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

    public PushMessage(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public PushMessage(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "type=" + type +
                ", content=" + content +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.content);
    }

    public PushMessage() {
    }

    protected PushMessage(Parcel in) {
        this.type = in.readInt();
        this.content = in.readString();
    }

    public static final Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel source) {
            return new PushMessage(source);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };
}
