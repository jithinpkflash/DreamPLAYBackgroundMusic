**On Create ( Initialisation )**
````
registerReceiver(SongDataReceiver,new IntentFilter("com.dplay.SONG_DATA"));

registerReceiver(SongStateReceiver,new IntentFilter("com.dplay.SONG_STATE"));
````
**More Block ( Receive )**

Allign asd blocks as shown bellow

<img src="https://github.com/jithinpkflash/DreamPLAYBackgroundMusic/blob/f58296ab6d15b83643a2f4bc4744a42c66be6e9b/IMG_20211129_164325.jpg" width ="100%">

**ASD 1**
````
}
private BroadcastReceiver SongDataReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {

duration = intent.getStringExtra("total_duration");
current = intent.getStringExtra("current_duration");
````
__// Your Seekbar and Time textview__

**ASD 2**
````
}
   };
private BroadcastReceiver SongStateReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
state = intent.getStringExtra("responce");
````
__// Set button state ( Pause or playing )__

**ASD 3**
````
}
   };
{
````

**Start or Stop Service**
````
if(button1.getText().equals("Start"))
{
Intent myService = new Intent(MainActivity.this, DreamPLAYBackgroundMusic.class);
myService.putExtra("url", url);
startService(myService);
button1.setText("Stop");
                   
}else {

Intent myService = new Intent(MainActivity.this, DreamPLAYBackgroundMusic.class);
stopService(myService);
button1.setText("Start");
                    
}
````
**PLAY / PAUSE button**
````
Intent intent=new Intent("com.dplay.SONG_CONTROL");
intent.putExtra("data", "playpause");
sendBroadcast(intent);
````

**REQUEST State : ( Get playing or paused )**
````
Intent intent=new Intent("com.dplay.SONG_STATE");
intent.putExtra("data", "state");
sendBroadcast(intent);
````
**SEEK BAR on stop touch : ( To seek music )**
````
Intent intent=new Intent("com.dplay.SONG_SEEK");
intent.putExtra("data", pos);
sendBroadcast(intent);
````
__// pos is a string variable which we need to assign cureent Seekbar position__

**Andorid manifest**
````
<service
    android:name="DreamPLAYBackgroundMusic" >
</service>
````
WATCH VIDEO ON YOUTUBE
<A href="https://youtube.com/c/DreamPLAYdevYT"> WATCH </a>
