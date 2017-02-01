package io.github.winsontse.hearteyes.model.local;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by winson on 2017/2/1.
 */

@Database(name = HeartEyesDB.DB_NAME, version = HeartEyesDB.DB_VERSION)
public interface HeartEyesDB {
    String DB_NAME = "hearteyes_db";
    int DB_VERSION = 1;
}
