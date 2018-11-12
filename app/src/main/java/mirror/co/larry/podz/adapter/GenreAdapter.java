package mirror.co.larry.podz.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    List<String> genreList;
    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder genreViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
