package mirror.co.larry.podz.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import mirror.co.larry.podz.model.Podcast;
import mirror.co.larry.podz.R;
import mirror.co.larry.podz.databinding.PodcastListItemBinding;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {
    List<Podcast> podcastList;
    Context mContext;
    LayoutInflater layoutInflater;
    OnPodcastClickListener mListener;

    public PodcastAdapter(Context context, List<Podcast> list, OnPodcastClickListener listener){
        mContext = context;
        podcastList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PodcastListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.podcast_list_item, viewGroup, false);
        return new PodcastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder podcastViewHolder, int i) {
        Podcast podcast = podcastList.get(i);
        podcastViewHolder.binding.podcastAuthor.setText(podcast.getPublisher());
        podcastViewHolder.binding.podcastDescription.setText(podcast.getDescription());
        podcastViewHolder.binding.podcastName.setText(podcast.getTitle());
        podcastViewHolder.setBinding(podcast);
        Glide.with(mContext)
                .load(podcast.getThumbnail())
                .into(podcastViewHolder.binding.podcastImage);
    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }


    public class PodcastViewHolder extends RecyclerView.ViewHolder {

        PodcastListItemBinding binding;

        public PodcastViewHolder(PodcastListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

        void setBinding(final Podcast podcast){
            binding.podcastListItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.podcastItemClickListener(v, podcast.getId());
                }
            });
        }
    }

    public interface OnPodcastClickListener {
        void podcastItemClickListener(View view, String id);
    }

}
