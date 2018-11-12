package mirror.co.larry.podz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mirror.co.larry.podz.Model.Podcast;
import mirror.co.larry.podz.R;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {
    List<Podcast> podcastList;
    Context mContext;

    public PodcastAdapter(Context context, List<Podcast> list){
        mContext = context;
        podcastList = list;
    }

    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.podcast_list_item, viewGroup, false);
        return  new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder podcastViewHolder, int i) {
        Podcast podcast = podcastList.get(i);

    }

    @Override
    public int getItemCount() {
        return podcastList.size();
    }

    public class PodcastViewHolder extends RecyclerView.ViewHolder {
        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
