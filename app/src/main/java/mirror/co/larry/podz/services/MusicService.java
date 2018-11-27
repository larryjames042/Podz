package mirror.co.larry.podz.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import mirror.co.larry.podz.R;

public class MusicService extends Service implements Player.EventListener {

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

    public void playPodcast(String audioUri){
        initializePlayer(audioUri);
        exoPlayer.setPlayWhenReady(true);
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public void pausePlayer(){
        if(exoPlayer!=null){
            exoPlayer.setPlayWhenReady(false);
        }
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
