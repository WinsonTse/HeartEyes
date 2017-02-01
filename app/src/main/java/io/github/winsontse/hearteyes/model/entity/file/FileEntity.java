package io.github.winsontse.hearteyes.model.entity.file;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import io.github.winsontse.hearteyes.model.local.HeartEyesDB;

/**
 * Created by winson on 2017/2/1.
 */

@Table(database = HeartEyesDB.class)
public class FileEntity extends BaseModel {
    @PrimaryKey
    String id;
    @Column
    String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
