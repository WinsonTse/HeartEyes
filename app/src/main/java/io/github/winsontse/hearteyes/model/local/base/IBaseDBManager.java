/*
 * Copyright (c) 2016. Istuary Innovation Group, Ltd.
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and
 *  confidential.
 *  Created on 星期二, 29 十一月 2016 17:32:26 +0800.
 *  ProjectName: ironhide-android; ModuleName: IronHideLibrary; ClassName: IBaseDBManager.java.
 *  Author: Lena; Last Modified: 星期二, 29 十一月 2016 17:32:26 +0800.
 *  This file is originally created by winson.
 */

package io.github.winsontse.hearteyes.model.local.base;

import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import rx.Observable;

public interface IBaseDBManager<T extends BaseModel> {

    T loadSingleSync(String objectId);

    Observable<T> loadSingleObservable(String objectId);

    T saveSync(T t);

    Observable<T> saveObservable(T t);

    void deleteSync(T t);

    Observable<Boolean> deleteObservable(T t);

    Observable<Boolean> deleteByConditionsObservable(SQLCondition... conditions);

    Observable<Boolean> deleteAllObservable();

    Observable<List<T>> loadByConditionsObservable(SQLCondition... conditions);

    Observable<List<T>> loadAllObservable();
}
