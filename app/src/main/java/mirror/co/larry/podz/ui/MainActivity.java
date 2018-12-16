package mirror.co.larry.podz.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.Serializable;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.adapter.FavouritePodcastAdapter;
import mirror.co.larry.podz.adapter.PodcastAdapter;
import mirror.co.larry.podz.databinding.ActivityMainBinding;
import mirror.co.larry.podz.services.MusicService;
import mirror.co.larry.podz.util.OnVisibleListener;

public class MainActivity extends AppCompatActivity implements
        EpisodeDetailFragment.OnButtonPlayListener,
        PodcastAdapter.OnPodcastClickListener,
        OnVisibleListener,
        FavouritePodcastAdapter.OnFavPodcastClickListener,
        PlayerFragment.OnPlayerViewVisibleListener {
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
        if(savedInstanceState==null){
            loadFragment(new DiscoverFragment());
        }

//        else{
//            int index = getSupportFragmentManager().getBackStackEntryCount();
//            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
//            String tag = backEntry.getName();
//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//            loadFragment(fragment);
//        }

       binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        binding.playerSmallViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playerSmallViewContainer.setVisibility(View.INVISIBLE);

                if(exoPlayer!=null){
                    PlayerFragment playerFragment = new PlayerFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.content_container, playerFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
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
                case R.id.navigation_favourite:
                    fragment = new FavFragment();
                    break;
                case R.id.navigation_search:
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
            if( exoPlayer.getPlayWhenReady()&& exoPlayer.getPlaybackState()== Player.STATE_READY){
                binding.playerSmallViewContainer.setVisibility(View.VISIBLE);
                binding.tvEpisodeName.setText(musicService.episodeName);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        // bind service
        if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
//        if(playIntent!=null){
//            stopService(playIntent);
//            musicService = null;
//        }
        Log.d("MainServiceActivity: ", "Destroy");
        super.onDestroy();
    }

    @Override
    public void onPlayButtonClick(String audioUrl, String thumbnail, String episodeName) {
        binding.playerSmallViewContainer.setVisibility(View.VISIBLE);
        binding.tvEpisodeName.setText(episodeName);
        binding.tvEpisodeName.setSelected(true);
        musicService.playPodcast(audioUrl, episodeName, thumbnail);
    }

    @Override
    public void podcastItemClickListener(View view, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(PodcastDetailFragment.PODCAST_ID, id);
        Fragment podcastDetailFragment = new PodcastDetailFragment();
        podcastDetailFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, podcastDetailFragment, PodcastDetailFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void favPodcastItemClickListener(View view, String id) {
        binding.bottomNavigation.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString(PodcastDetailFragment.PODCAST_ID, id);
        Fragment podcastDetailFragment = new PodcastDetailFragment();
        podcastDetailFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, podcastDetailFragment, PodcastDetailFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showBottomNav(boolean isShow) {
        if (isShow) {
            binding.bottomNavigation.setVisibility(View.VISIBLE);
        }else{
            binding.bottomNavigation.setVisibility(View.GONE);
        }
        return;
    }

    @Override
    public void onVisible(boolean isVisible) {
        if(isVisible==false){
            binding.playerSmallViewContainer.setVisibility(View.VISIBLE);
        }
    }
}
