package mirror.co.larry.podz;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import mirror.co.larry.podz.Util.HelperUtil;
import mirror.co.larry.podz.databinding.FragmentEpisodeDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodeDetailFragment extends Fragment {

    public static final String KEY_AUDIO = "audio";
    public static final String KEY_ID = "id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THUMBNAIL = "thumbnail";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUDIO_LENGTH = "audioLength";
    public static final String KEY_PUB_DATE = "pubDate";
    public static final String KEY_LISTEN_NOTE_URL = "listenNoteUrl";
    public static final String KEY_PODCAST_NAME = "podcastName";

    String id, title, audio, description, thumbnail, listenNoteUrl, pubDate, podcastName;
    int audioLength;
    FragmentEpisodeDetailBinding binding;

    OnButtonPlayListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_episode_detail, container, false);
        View v = binding.getRoot();
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

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(mListener == null){
            mListener = (OnButtonPlayListener) context;
        }
    }

    interface OnButtonPlayListener{
        void onPlayButtonClick(String audioUrl, String audioDuration, String episodeName);
    }



}
