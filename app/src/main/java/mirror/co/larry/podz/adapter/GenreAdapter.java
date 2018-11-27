package mirror.co.larry.podz.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.databinding.GenreListItemBinding;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    Context mContext;
    List<String> genreList;
    LayoutInflater inflater;
    public GenreAdapter(Context context, List<String> list){
        genreList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(inflater == null){
            inflater =  LayoutInflater.from(viewGroup.getContext());
        }
        GenreListItemBinding binding = DataBindingUtil.inflate(inflater,R.layout.genre_list_item, viewGroup, false);

        return new GenreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder genreViewHolder, int i) {
        String genre = genreList.get(i);
        genreViewHolder.binding.tvGenre.setText(genre);
    }

    @Override
    public int getItemCount() {
        if(genreList.size()==0){
            return 0;
        }else{
            return genreList.size();
        }

    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        GenreListItemBinding binding;

        public GenreViewHolder(GenreListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
