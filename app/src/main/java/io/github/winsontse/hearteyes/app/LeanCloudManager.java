package io.github.winsontse.hearteyes.app;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.util.constant.SecretConstant;

/**
 * Created by winson on 16/7/17.
 */

public class LeanCloudManager {
    private Context context;
    public static final int NETWORK_TIME_OUT = 15 * 1000;

    @Inject
    public LeanCloudManager(Context context) {
        this.context = context;
    }

    public void init() {
        AVOSCloud.initialize(context, SecretConstant.LEANCLOUD_APP_ID, SecretConstant.LEANCLOUD_APP_KEY);
        AVOSCloud.setNetworkTimeout(NETWORK_TIME_OUT);
    }
}
