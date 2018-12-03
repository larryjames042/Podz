package mirror.co.larry.podz.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mirror.co.larry.podz.ui.EpisodeDetailFragment;
import mirror.co.larry.podz.model.Episode;
import mirror.co.larry.podz.R;
import mirror.co.larry.podz.databinding.PreviousEpisodeListItemBinding;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {
    List<Episode> episodeList;
    Context mContext;
    LayoutInflater layoutInflater;
    OnEpisodeClickListener mListener;
    public EpisodeAdapter(Context context, List<Episode> list, OnEpisodeClickListener listener){
        mContext = context;
        episodeList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PreviousEpisodeListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.previous_episode_list_item, viewGroup, false);

        return new EpisodeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder episodeViewHolder, int i) {
        Episode episode = episodeList.get(i);
        episodeViewHolder.setBinding(episode);
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public class EpisodeViewHolder extends RecyclerView.ViewHolder {
        PreviousEpisodeListItemBinding binding;
        public EpisodeViewHolder(PreviousEpisodeListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }

        public void setBinding(Episode episode){
            long unixSeconds = episode.getPubDate();
            Date date = new java.util.Date(unixSeconds);
            // the format of your date
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sdf.format(date);
            binding.episodeName.setText(episode.getTitle());
            binding.publishDate.setText(formattedDate);
            final Bundle b = new Bundle();
            b.putString(EpisodeDetailFragment.KEY_PODCAST_NAME, episode.getPodcastName());
            b.putString(EpisodeDetailFragment.KEY_AUDIO, episode.getAudio());
            b.putString( EpisodeDetailFragment.KEY_DESCRIPTION , episode.getDescription());
            b.putString( EpisodeDetailFragment.KEY_ID  , episode.getId());
            b.putString( EpisodeDetailFragment.KEY_LISTEN_NOTE_URL , episode.getListenNoteUrl());
            b.putString( EpisodeDetailFragment.KEY_THUMBNAIL , episode.getThumbnail());
            b.putString(EpisodeDetailFragment.KEY_TITLE  , episode.getTitle());
            b.putInt( EpisodeDetailFragment.KEY_AUDIO_LENGTH , episode.getAudioLength());
            b.putString( EpisodeDetailFragment.KEY_PUB_DATE, formattedDate);

            binding.episodeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.episodeOnClick(v, b);
                }
            });
        }
    }

    public interface OnEpisodeClickListener {
        void episodeOnClick(View view, Bundle bundle);
    }
}
