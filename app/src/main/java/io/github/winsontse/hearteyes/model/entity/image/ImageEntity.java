package io.github.winsontse.hearteyes.model.entity.image;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by winson on 16/6/19.
 */
public class ImageEntity implements Parcelable {
    private String data;
    private String thumbnail;
    private long size;
    private String displayName;
    private String title;
    private long dateAdded;
    private long dateModified;
    private String mineType;
    private int width;
    private int height;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
        dest.writeString(this.thumbnail);
        dest.writeLong(this.size);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeLong(this.dateAdded);
        dest.writeLong(this.dateModified);
        dest.writeString(this.mineType);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public ImageEntity() {
    }

    protected ImageEntity(Parcel in) {
        this.data = in.readString();
        this.thumbnail = in.readString();
        this.size = in.readLong();
        this.displayName = in.readString();
        this.title = in.readString();
        this.dateAdded = in.readLong();
        this.dateModified = in.readLong();
        this.mineType = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel source) {
            return new ImageEntity(source);
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageEntity entity = (ImageEntity) o;

        return data != null ? data.equals(entity.data) : entity.data == null;

    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}
