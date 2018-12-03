package mirror.co.larry.podz.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import mirror.co.larry.podz.ui.MainActivity;
import mirror.co.larry.podz.R;

public class MusicService extends Service implements Player.EventListener {
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFY_ID = 1;
    private SimpleExoPlayer exoPlayer;
    private  final IBinder musicBind = new MusicBinder();
    private boolean isPlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (exoPlayer!=null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        return false;
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
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

    public void playPodcast(String audioUri, String episodeName){
        initializePlayer(audioUri);
        exoPlayer.setPlayWhenReady(true);
        buildNotification(episodeName);
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
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder;

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(MusicService.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(this, CHANNEL_ID);
        }else{
            builder = new Notification.Builder(this);
        }
        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play)
                .setTicker(episodeName)
                .setOngoing(true)
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
