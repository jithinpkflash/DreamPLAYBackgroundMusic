package com.dreamplaydev.musicbackground;
/* Change package name above to your application package name*/

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.content.SharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import android.net.Uri;
/*   Created by DreamPLAY Dev on 25/11/2021.   */

/* Add declaration of this service into the AndroidManifest.xml inside application tag*/

public class DreamPLAYBackgroundMusic extends Service {

    private static final String TAG = "DreamPLAYBackgroundMusic";
    private TimerTask timer;
    private Timer _timer = new Timer();
    private TimerTask notis;
    private MediaPlayer media;
    private SharedPreferences mediaservice;

    MediaPlayer player;
    String path = "";

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
		
		/* BoardcastReceiver to Pause or Resume Audio */
        final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String str = intent.getStringExtra("data");
                    if (player != null) {
                        if (player.isPlaying()) {
                            player.pause();
                        } else {
                            player.start();
                        }
                    }
            }
        };

        registerReceiver(mMessageReceiver, new IntentFilter("com.dplay.SONG_CONTROL"));


		/* BoardcastReceiver to Seek audio */
        final BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String str2 = intent.getStringExtra("data");
                if (intent.getStringExtra("data") != null) {
                    if (player != null) {
                        player.seekTo((int)(Double.parseDouble(intent.getStringExtra("data"))));
                    } else {

                    }

                }

            }
        };
        registerReceiver(mMessageReceiver2, new IntentFilter("com.dplay.SONG_SEEK"));


		/* BoardcastReceiver to Check Audio play status ( playing or pasued ) */
        final BroadcastReceiver mMessageReceiver3 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (player != null) {
                    if (player.isPlaying()) {
                        Intent intents = new Intent("com.dplay.SONG_STATE");
                        intents.putExtra("responce", "playing");
                        sendBroadcast(intents);
                    } else {
                        Intent intents = new Intent("com.dplay.SONG_STATE");
                        intents.putExtra("responce", "paused");
                        sendBroadcast(intents);
                    }
                }


            }
        };
        registerReceiver(mMessageReceiver3, new IntentFilter("com.dplay.SONG_STATE"));


        Toast.makeText(this, "Music player service started", Toast.LENGTH_SHORT).show();
       
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
		
		/* Receive File path from Activity*/
        path = intent.getStringExtra("url");
		
        /* To deal with runtime errors try.. catch block*/
        try {

            player = MediaPlayer.create(this, Uri.parse(path));
            player.setLooping(true); // Set looping
            player.setVolume(100, 100);
            player.start();
			
			/* To send current media position and total duration to activity*/
            notis = new TimerTask() {
                @Override
                public void run() {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (player != null) {
                                if (player.isPlaying()) {
                                    String current = String.valueOf((long)(player.getCurrentPosition()));
                                    String duration = String.valueOf((long)(player.getDuration()));

                                    Intent intent1 = new Intent("com.dplay.SONG_DATA");

                                    intent1.putExtra("total_duration", duration);

                                    intent1.putExtra("current_duration", current);
                                    sendBroadcast(intent1);
                                }
                            }
                        } 
                    });
                }
            };
			
			/* You can update the int value below to change the seekbar update interval*/
            _timer.scheduleAtFixedRate(notis, (int)(1000), (int)(1000));



        } catch (Exception e) {
			/* To show the error message if any happens*/
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return Service.START_STICKY;

    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        Log.i(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
		/* Stop timer and stop media player on service stoped*/
        notis.cancel();
        player.stop();
        player.release();
		/* Tells the activity that service stopped*/
        
        Toast.makeText(this, "Service stopped...", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }
}