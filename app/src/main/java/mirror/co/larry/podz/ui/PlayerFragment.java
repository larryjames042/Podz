package mirror.co.larry.podz.ui;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.databinding.FragmentPlayerBinding;
import mirror.co.larry.podz.services.MusicService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {
    FragmentPlayerBinding binding;
    private Intent playIntent;
    private MusicService musicService;
    private  boolean musicBound = false;
    private SimpleExoPlayer player;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        View view = binding.getRoot();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        binding.adView.loadAd(adRequest);

        return view;
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
            player = binder.getExoplayerInstance();
            binding.playerControlView.setPlayer(player);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onDestroy() {
//        getActivity().stopService(playIntent);
//        musicService = null;
        super.onDestroy();
    }
}