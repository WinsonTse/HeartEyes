package io.github.winsontse.hearteyes.model.entity.account;

import android.support.annotation.IntDef;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.winsontse.hearteyes.model.entity.file.FileEntity;
import io.github.winsontse.hearteyes.model.local.HeartEyesDB;

/**
 * Created by winson on 2017/1/29.
 */

@Table(database = HeartEyesDB.class)
public class UserEntity extends BaseModel {

    @IntDef({USER_TYPE_NORMAL, USER_TYPE_LOGIN, USER_TYPE_FRIEND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserType {
    }

    public static final int USER_TYPE_NORMAL = 0;
    public static final int USER_TYPE_LOGIN = 1;
    public static final int USER_TYPE_FRIEND = 2;

    @PrimaryKey
    String objectId;

    @Column
    String nickname;

    @ForeignKey(saveForeignKeyModel = true, deleteForeignKeyModel = true)
    FileEntity avatar;

    @Column
    int type;

    public FileEntity getAvatar() {
        return avatar;
    }

    public void setAvatar(FileEntity avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @UserType
    public int getType() {
        return type;
    }

    public void setType(@UserType int type) {
        this.type = type;
    }
}
