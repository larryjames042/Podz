package mirror.co.larry.podz.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import mirror.co.larry.podz.PodzWidget;
import mirror.co.larry.podz.ui.MainActivity;
import mirror.co.larry.podz.R;

public class MusicService extends Service implements Player.EventListener {
    private static final String CHANNEL_ID = "channel_id";
    private static final String ACTION_PLAY = "action_play";
    private static final String ACTION_PAUSE = "action_pause";
    private static final String ACTION_STOP = "action_stop";
    private static final int NOTIFY_ID = 1;
    private SimpleExoPlayer exoPlayer;
    private  final IBinder musicBind = new MusicBinder();
    private boolean isPlaying = false, isActivityVisible = true;
    public String audioUrl, thumbnail, episodeName, podcastName;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
        {
            String action;
            if(intent.getAction() !=null){
                action  = intent.getAction();
                switch (action)
                {
                    case ACTION_STOP:
                        if(isActivityVisible){
                            pausePlayer();
                        }else{
                            stopForegroundService();
                        }
                        Toast.makeText(this, "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PLAY:
                        if(exoPlayer!=null) exoPlayer.setPlayWhenReady(true);
                        Toast.makeText(this, "Play", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PAUSE:
                        pausePlayer();
                        Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
//        stopForeground(true);
    }

    public class MusicBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }

        public SimpleExoPlayer getExoplayerInstance(){
            if(exoPlayer!=null){
                return exoPlayer;
            }else{
                exoPlayer = ExoPlayerFactory.newSimpleInstance(MusicService.this);
                return exoPlayer;
            }
        }
    }

    private  void initializePlayer(String audioUri){
        Uri uri  = Uri.parse(audioUri);
        if(exoPlayer == null){
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        }
        exoPlayer.addListener(this);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        // Prepare the player with the source.
        exoPlayer.prepare(videoSource);
    }

    public void playPodcast(String audioUrl, String episodeName, String thumbnail, String podcastName){
        if(this.audioUrl==null && this.episodeName==null && this.thumbnail==null){
            this.audioUrl = audioUrl;
            this.thumbnail = thumbnail;
            this.episodeName = episodeName;
            this.podcastName = podcastName;
            initializePlayer(audioUrl);
            exoPlayer.setPlayWhenReady(true);
            buildNotification(episodeName);
        }else{
            if(this.audioUrl.equals(audioUrl)){
                if(isPlaying){
                    exoPlayer.setPlayWhenReady(false);
                }else{
                    exoPlayer.setPlayWhenReady(true);
                }
            }else{
                initializePlayer(audioUrl);
                this.audioUrl = audioUrl;
                this.thumbnail = thumbnail;
                this.episodeName = episodeName;
                this.podcastName = podcastName;
                exoPlayer.setPlayWhenReady(true);
                buildNotification(episodeName);
            }
        }

        // update widget when podcast is played
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PodzWidget.class));
        // update all widgets
        PodzWidget.updateLastPlayedEpisode(this, appWidgetManager,episodeName, appWidgetIds);

    }

    public void setActivityVisibility(boolean isActivityVisible){
        if(isActivityVisible){
            this.isActivityVisible = true;
        }else{
            this.isActivityVisible = false;
        }
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public void pausePlayer(){
        if(exoPlayer!=null){
            exoPlayer.setPlayWhenReady(false);
        }
    }

    private void buildNotification(String episodeName){
        // setup intent to launch Activity
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, 0);

        // set up intent to play podcast
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction(ACTION_PLAY);
        PendingIntent playPendIntent = PendingIntent.getService(this, 0, playIntent,0);

        // set up intent to pause podcast
        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pausePendIntent = PendingIntent.getService(this, 0, pauseIntent,0);

        // set up intent to pause podcast
        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent stopPendIntent = PendingIntent.getService(this, 0, closeIntent,0);

        NotificationCompat.Builder builder;
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(MusicService.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            mNotificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        NotificationCompat.Action stopAction = new NotificationCompat.Action(R.drawable.ic_stop_black, getString(R.string.stop), stopPendIntent);
        NotificationCompat.Action pauseAction = new NotificationCompat.Action(R.drawable.ic_pause, getString(R.string.pause), pausePendIntent);
        NotificationCompat.Action playAction = new NotificationCompat.Action(R.drawable.ic_play, getString(R.string.play), playPendIntent);
        // notification builder
        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play)
                .setTicker(episodeName)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .addAction(pauseAction)
                .addAction(playAction)
                .addAction(stopAction)
                .setContentTitle(getString(R.string.playing))
                .setContentText(episodeName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setColor(getColor(R.color.colorPrimary));
        } else {
            builder.setColor(getResources().getColor(R.color.colorPrimary));
        }

        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
    }

    private void stopForegroundService()
    {
        if (exoPlayer!=null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        // Stop foreground service and remove the notification.
        stopForeground(true);
        // Stop the foreground service.
        stopSelf();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playWhenReady && playbackState== Player.STATE_READY){
            isPlaying = true;
        }else if(playbackState== Player.STATE_READY){
            isPlaying = false;
        }else if(playbackState == Player.STATE_ENDED){
            isPlaying = false;
        }
    }
}
