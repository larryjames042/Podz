package mirror.co.larry.podz.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.exoplayer2.SimpleExoPlayer;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.databinding.ActivityMainBinding;
import mirror.co.larry.podz.services.MusicService;

public class MainActivity extends AppCompatActivity implements EpisodeDetailFragment.OnButtonPlayListener {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private ActivityMainBinding binding;
    private MusicService musicService;
    private  boolean musicBound = false;
    private Intent playIntent;
    private SimpleExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // default Fragment
        loadFragment(new DiscoverFragment());
       binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // Bottom Navigation Items onClick
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    fragment = new DiscoverFragment();
                    break;
                case R.id.navigation_Subscription:
                    fragment = new FavFragment();
                    break;
                case R.id.navigation_Search:
                    fragment = new SearchFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    // Connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            // get service
            musicService = binder.getService();
            // pass list
            musicBound = true;
            exoPlayer = binder.getExoplayerInstance();
            binding.playerView.setPlayer(exoPlayer);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public void onPlayButtonClick(String audioUrl, String audioDuration, String episodeName) {
        binding.playerSmallViewContainer.setVisibility(View.VISIBLE);
        binding.tvEpisodeName.setText(episodeName);
        binding.tvEpisodeName.setSelected(true);
        if(musicService.isPlaying()){
            musicService.pausePlayer();
        }else{
            musicService.playPodcast(audioUrl, episodeName);
        }
    }

}
