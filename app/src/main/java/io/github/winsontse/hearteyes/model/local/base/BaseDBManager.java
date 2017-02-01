/*
 * Copyright (c) 2016. Istuary Innovation Group, Ltd.
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and
 *  confidential.
 *  Created on 星期二, 29 十一月 2016 17:32:13 +0800.
 *  ProjectName: ironhide-android; ModuleName: IronHideLibrary; ClassName: BaseDBManager.java.
 *  Author: Lena; Last Modified: 星期二, 29 十一月 2016 17:32:13 +0800.
 *  This file is originally created by winson.
 */

package io.github.winsontse.hearteyes.model.local.base;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import io.github.winsontse.hearteyes.model.local.HeartEyesDB;
import rx.Observable;
import rx.Subscriber;

public class BaseDBManager<T extends BaseModel> implements IBaseDBManager<T> {
    private Class<T> clazz;
    private Property<String> primaryKey;

    public BaseDBManager(Class<T> clazz, Property<String> primaryKey) {
        this.clazz = clazz;
        this.primaryKey = primaryKey;
    }

    protected DatabaseDefinition getDatabaseDefinition() {
        return FlowManager.getDatabase(HeartEyesDB.class);
    }

    protected DatabaseWrapper getDatabase() {
        return getDatabaseDefinition().getWritableDatabase();
    }

    protected From<T> selectFrom() {
        return SQLite.select().from(clazz);
    }

    protected From<T> deleteFrom() {
        return SQLite.delete().from(clazz);
    }

    protected Update<T> updateFrom() {
        return SQLite.update(clazz);
    }

    @Override
    public T loadSingleSync(String objectId) {
        return selectFrom().where(primaryKey.eq(objectId)).querySingle();
    }

    @Override
    public Observable<T> loadSingleObservable(final String objectId) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(loadSingleSync(objectId));
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public T saveSync(T t) {
        t.save();
        return t;
    }

    @Override
    public Observable<T> saveObservable(final T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                try {
                    T result = saveSync(t);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public void deleteSync(T t) {
        t.delete();
    }

    @Override
    public Observable<Boolean> deleteObservable(final T t) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    deleteSync(t);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> deleteByConditionsObservable(final SQLCondition... conditions) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    From<T> from = deleteFrom();
                    if (conditions != null) {
                        from.where(conditions).execute();
                    } else {
                        from.execute();
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAllObservable() {
        return deleteByConditionsObservable();
    }

    @Override
    public Observable<List<T>> loadAllObservable() {
        return loadByConditionsObservable();
    }

    @Override
    public Observable<List<T>> loadByConditionsObservable(final SQLCondition... conditions) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                subscriber.onStart();
                try {
                    From<T> from = selectFrom();
                    if (conditions != null) {
                        subscriber.onNext(from.where(conditions).queryList());
                    } else {
                        subscriber.onNext(from.queryList());
                    }
                    subscriber.onCompleted();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
