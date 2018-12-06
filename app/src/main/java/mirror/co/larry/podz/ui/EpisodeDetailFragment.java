package mirror.co.larry.podz.ui;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.util.HelperUtil;
import mirror.co.larry.podz.databinding.FragmentEpisodeDetailBinding;
import mirror.co.larry.podz.util.OnVisibleListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodeDetailFragment extends Fragment {
    public static final String TAG = "EpisodeDetailFragment";
    public static final String KEY_AUDIO = "audio";
    public static final String KEY_ID = "id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUDIO_LENGTH = "audioLength";
    public static final String KEY_PUB_DATE = "pubDate";
    public static final String KEY_LISTEN_NOTE_URL = "listenNoteUrl";
    public static final String KEY_PODCAST_NAME = "podcastName";

    private String id, title, audio, description, thumbnail, listenNoteUrl, pubDate, podcastName;
    private int audioLength;
    private FragmentEpisodeDetailBinding binding;
    private OnVisibleListener mVisibleListener;
    private OnButtonPlayListener mListener;

    public EpisodeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id = getArguments().getString(KEY_ID);
            audio = getArguments().getString(KEY_AUDIO);
            audioLength = getArguments().getInt(KEY_AUDIO_LENGTH);
            title = getArguments().getString(KEY_TITLE);
            pubDate = getArguments().getString(KEY_PUB_DATE);
            thumbnail = getArguments().getString(KEY_THUMBNAIL);
            listenNoteUrl = getArguments().getString(KEY_LISTEN_NOTE_URL);
            description = getArguments().getString(KEY_DESCRIPTION);
            podcastName = getArguments().getString(KEY_PODCAST_NAME);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_episode_detail, container, false);
        View v = binding.getRoot();
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.podcastName.setText(podcastName);
        binding.episodeDuration.setText(HelperUtil.convertSecToHr(audioLength));
        binding.episodeUploadDate.setText(String.valueOf(pubDate));
        binding.episodeName.setText(title);
        binding.episodeDescription.setText(description);
        binding.episodeDesLabel.setText(getString(R.string.about_episode));
        Glide.with(getActivity())
                .load(thumbnail)
                .into(binding.episodeThumbnail);

        binding.btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayButtonClick(audio, HelperUtil.convertSecToHr(audioLength), title);
            }
        });
        mVisibleListener.showBottomNav(false);

        // share episode
        binding.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, listenNoteUrl);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        // link
        binding.btLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(listenNoteUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(mListener == null){
            mListener = (OnButtonPlayListener) context;
        }
        if(mVisibleListener == null){
            mVisibleListener = (OnVisibleListener) context;
        }
    }

    interface OnButtonPlayListener{
        void onPlayButtonClick(String audioUrl, String audioDuration, String episodeName);
    }
}
