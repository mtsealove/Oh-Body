package com.mtsealove.github.ohbody.Services;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.mtsealove.github.ohbody.Database.DateF;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;
import com.mtsealove.github.ohbody.MainActivity;
import com.mtsealove.github.ohbody.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WalkService extends Service {
    private Thread mainThread;
    public static Intent serviceIntent = null;
    PersonalDbHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int count = 0;

    public WalkService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        ReadCount();

        mainThread = new Thread(new SensorRun());
        mainThread.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        serviceIntent = null;
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new PersonalDbHelper(getApplicationContext(), PersonalDbHelper.WalkTable, null, 1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //저장되어 있는 걸음 수 업데이트
    private void ReadCount() {
        DateF dateF = new DateF();
        database = dbHelper.getReadableDatabase();
        String query = "select WalkCount from " + PersonalDbHelper.WalkTable + " where Date='" + dateF.getDate() + "'";
        cursor = database.rawQuery(query, null);
        if (cursor.moveToNext()) {
            int c = cursor.getInt(0);
            count = c;
        }
    }

    //노티 보내기
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle("Service test")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    //스레드를 통해 돌릴 센서 리스너
    private class SensorRun implements Runnable {
        SensorManager sm;
        SensorEventListener accL;
        SensorEventListener oriL;
        Sensor oriSensor;
        Sensor accSensor;
        private long lastTime;
        private float speed;
        private float lastX;
        private float lastY;
        private float lastZ;
        private float x, y, z;
        private static final int SHAKE_THRESHOLD = 800;

        public SensorRun() {
            sm = (SensorManager) getSystemService(SENSOR_SERVICE);    // SensorManager 인스턴스를 가져옴
            oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);    // 방향 센서
            accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            oriL = new oriListener();        // 방향 센서 리스너 인스턴스
            accL = new accListener();

            sm.registerListener(accL, accSensor, SensorManager.SENSOR_DELAY_NORMAL);    // 가속도 센서 리스너 오브젝트를 등록
            sm.registerListener(oriL, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);    // 방향 센서 리스너 오브젝트를 등록
        }

        private class accListener implements SensorEventListener {
            public void onSensorChanged(SensorEvent event) {  // 가속도 센서 값이 바뀔때마다 호출됨
                long currentTime = System.currentTimeMillis();
                long gabOfTime = (currentTime - lastTime);

                String str;

                if (gabOfTime > 100) {
                    lastTime = currentTime;
                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];

                    speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        // 이벤트발생!!
                        count++;
                        str = String.format("%d", count);
                        Log.e("걸음", str);
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        DateF dateF = new DateF();
                        database = dbHelper.getReadableDatabase();
                        String query = "select WalkCount from " + PersonalDbHelper.WalkTable + " where Date='" + dateF.getDate() + "'";
                        cursor = database.rawQuery(query, null);
                        database = dbHelper.getWritableDatabase();
                        //데이터가 이미 존재하는 경우에는
                        if (cursor.moveToNext()) {
                            Log.e("쿼리", "데이터 업데이트");
                            dbHelper.UpdateWalkData(database, count);
                        } else {//첫 걸음 시
                            Log.e("쿼리", "데이터 생성");
                            dbHelper.CreateWalkData(database);
                        }
                        if (count % 10 == 0) sendNotification("test");
                    }

                    lastX = event.values[0];
                    lastY = event.values[1];
                    lastZ = event.values[2];
                }

            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }

        private class oriListener implements SensorEventListener {
            public void onSensorChanged(SensorEvent event) {  // 방향 센서 값이 바뀔때마다 호출됨
                /*
                Log.i("SENSOR", "Orientation changed.");
                Log.i("SENSOR", "  Orientation X: " + event.values[0]
                        + ", Orientation Y: " + event.values[1]
                         + ", Orientation Z: " + event.values[2]);
                 */
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }


        @Override
        public void run() {
            sm.registerListener(accL, accSensor, SensorManager.SENSOR_DELAY_NORMAL);    // 가속도 센서 리스너 오브젝트를 등록
            sm.registerListener(oriL, oriSensor, SensorManager.SENSOR_DELAY_NORMAL);    // 방향 센서 리스너 오브젝트를 등록
        }
    }
}