package io.github.winsontse.hearteyes.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.avos.avoscloud.AVBroadcastReceiver;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.PushService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.page.main.MainActivity;
import io.github.winsontse.hearteyes.util.constant.SecretConstant;

public class HeartEyesReceiver extends BroadcastReceiver {

    public static final String TAG = "winson";

    public HeartEyesReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "收到推送");
        try {
            if (intent.getAction().equals(SecretConstant.PUSH_ACTION)) {
                JSONObject json = new JSONObject(intent.getExtras().getString(SecretConstant.PUSH_LEANCLOUD_DATA));
                final String message = json.getString("alert");
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(
                                        AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                                .setContentText(message)
                                .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);

                int mNotificationId = 10086;
                NotificationManager mNotifyMgr =
                        (NotificationManager) AVOSCloud.applicationContext
                                .getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        } catch (Exception e) {

        }
    }
}
