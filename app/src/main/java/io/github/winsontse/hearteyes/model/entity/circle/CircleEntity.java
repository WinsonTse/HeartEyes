package io.github.winsontse.hearteyes.model.entity.circle;

import android.support.annotation.IntDef;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.winsontse.hearteyes.model.entity.account.UserEntity;
import io.github.winsontse.hearteyes.model.entity.file.FileEntity;
import io.github.winsontse.hearteyes.model.local.HeartEyesDB;

/**
 * Created by winson on 2017/1/29.
 */

@Table(database = HeartEyesDB.class)
public class CircleEntity extends BaseModel {

    public static final int CIRCLE_TYPE_NORMAL = 0;
    public static final int CIRCLE_TYPE_MINE = 1;

    @IntDef({CIRCLE_TYPE_NORMAL, CIRCLE_TYPE_MINE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CircleType {
    }

    @PrimaryKey
    String objectId;

    @Column
    String name;

    @ForeignKey(saveForeignKeyModel = true, deleteForeignKeyModel = true)
    FileEntity cover;

    @Column
    long loveDay;

    @Column
    int type;

    UserEntity creator;
    UserEntity invitee;


    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public UserEntity getInvitee() {
        return invitee;
    }

    public void setInvitee(UserEntity invitee) {
        this.invitee = invitee;
    }

    public long getLoveDay() {
        return loveDay;
    }

    public void setLoveDay(long loveDay) {
        this.loveDay = loveDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public FileEntity getCover() {
        return cover;
    }

    public void setCover(FileEntity cover) {
        this.cover = cover;
    }

    @CircleType
    public int getType() {
        return type;
    }

    public void setType(@CircleType int type) {
        this.type = type;
    }
}
