package mirror.co.larry.podz.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.adapter.FavouritePodcastAdapter;
import mirror.co.larry.podz.databinding.FragmentFavBinding;
import mirror.co.larry.podz.model.Podcast;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.viewModel.PodcastViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment implements FavouritePodcastAdapter.OnPodcastClickListener  {

    PodcastViewModel mViewModel;
    FragmentFavBinding binding;
    FavouritePodcastAdapter adapter;
    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_fav, container, false);
        View view = binding.getRoot();

//         set recyclerview decoration and layout
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvFavPodcast.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.rvFavPodcast.setHasFixedSize(true);
        binding.rvFavPodcast.setLayoutManager(layoutManager);
        adapter = new FavouritePodcastAdapter(getContext(), this);

        mViewModel = ViewModelProviders.of(this).get(PodcastViewModel.class);
        mViewModel.getmAllPodcast().observe(this, new Observer<List<Podcast>>() {
            @Override
            public void onChanged(@Nullable List<Podcast> podcasts) {
                adapter.setPodcast(podcasts);
            }
        });
        binding.rvFavPodcast.setAdapter(adapter);
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }

//     Podcast onClick --->  PodcastDetailFragment

    @Override
    public void podcastItemClickListener(View view, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(PodcastDetailFragment.PODCAST_ID, id);
        Fragment podcastDetailFragment = new PodcastDetailFragment();
        podcastDetailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, podcastDetailFragment, PodcastDetailFragment.TAG)
                .addToBackStack(null)
                .commit();
    }
}
