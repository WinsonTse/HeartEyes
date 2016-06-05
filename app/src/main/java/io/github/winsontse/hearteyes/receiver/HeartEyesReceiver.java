package io.github.winsontse.hearteyes.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.util.rxbus.event.PushEvent;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.util.GsonUtil;
import io.github.winsontse.hearteyes.util.constant.Extra;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;
import io.github.winsontse.hearteyes.util.rxbus.RxBus;

public class HeartEyesReceiver extends BroadcastReceiver {

    public static final String TAG = "winson";

    public HeartEyesReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "收到推送");
        try {
            if (intent.getAction().equals(SecretConstant.PUSH_ACTION)) {
                String jsonStr = intent.getExtras().getString(SecretConstant.PUSH_LEANCLOUD_DATA);
                PushEvent pushMessage = GsonUtil.getInstance().fromJson(jsonStr, PushEvent.class);
                Log.d(TAG, "json结果:" + jsonStr);
                if (pushMessage != null) {
                    handlePushMessage(pushMessage);
                }
            }
        } catch (Exception e) {

        }
    }

    private void handlePushMessage(PushEvent pushMessage) {
        switch (pushMessage.getType()) {
            case PushEvent.RESTART_INIT_PAGE:
                RxBus.getInstance().post(pushMessage);
                String content = pushMessage.getFrom() + "已经成为你的好朋友";
                showNotification(0, content, content);

                break;

            default:
                showNotification(pushMessage.getType(), pushMessage.getFrom() + ":" + pushMessage.getAlert(), pushMessage.getAlert());
                break;
        }
    }

    private void showNotification(int openPageType, String content, String ticker) {
        Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
        resultIntent.putExtra(Extra.TYPE_NEW_PAGE, openPageType);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(AVOSCloud.applicationContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(
                                AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                        .setContentText(content)
                        .setTicker(ticker);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotifyMgr =
                (NotificationManager) AVOSCloud.applicationContext
                        .getSystemService(
                                Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(SecretConstant.PUSH_NOTIFICATION_ID, mBuilder.build());
    }
}
