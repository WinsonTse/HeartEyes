package io.github.winsontse.hearteyes.util.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hao.xie on 16/5/10.
 */

@Scope
@Documented
@Retention(RUNTIME)
public @interface BaseActivityScope {
}

