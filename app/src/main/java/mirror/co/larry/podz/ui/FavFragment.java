package mirror.co.larry.podz.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import mirror.co.larry.podz.App.AnalyticsApplication;
import mirror.co.larry.podz.R;
import mirror.co.larry.podz.adapter.FavouritePodcastAdapter;
import mirror.co.larry.podz.databinding.FragmentFavBinding;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.util.OnVisibleListener;
import mirror.co.larry.podz.util.RecyclerViewItemTouchHelper;
import mirror.co.larry.podz.viewModel.PodcastViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment implements FavouritePodcastAdapter.OnFavPodcastClickListener, RecyclerViewItemTouchHelper.RecyclerItemTouchHelperListener {
    public static final String TAG = FavFragment.class.getSimpleName();
    PodcastViewModel mViewModel;
    FragmentFavBinding binding;
    FavouritePodcastAdapter adapter;
    List<mirror.co.larry.room.Podcast> podcastList;
    Tracker mTracker;
    OnVisibleListener mListener;

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "FavFragmentScreen ");
        mTracker.setScreenName(TAG );
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_fav, container, false);
        View view = binding.getRoot();

        //set up toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.favourite));

//         set recyclerview decoration and layout
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvFavPodcast.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.rvFavPodcast.setHasFixedSize(true);
        binding.rvFavPodcast.setLayoutManager(layoutManager);
        adapter = new FavouritePodcastAdapter(getContext(), (FavouritePodcastAdapter.OnFavPodcastClickListener) getActivity());

        mViewModel = ViewModelProviders.of(this).get(PodcastViewModel.class);

        mViewModel.getAllPodcast().observe(this, new Observer<List<mirror.co.larry.room.Podcast>>() {
            @Override
            public void onChanged(@Nullable List<mirror.co.larry.room.Podcast> podcasts) {
                //get a copy of fav podcast list
                podcastList = podcasts;
                adapter.setPodcast(podcasts);
            }
        });
        binding.rvFavPodcast.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback callback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(callback).attachToRecyclerView(binding.rvFavPodcast);

        // set true when Fav fragment is visible.
        mListener.showBottomNav(true);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(mListener == null){
            mListener = (OnVisibleListener)context;
        }
    }

    //    Fav Podcast onClick --->  PodcastDetailFragment
    @Override
    public void favPodcastItemClickListener(View view, String id) {
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

    // remove from favourite list
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        mirror.co.larry.room.Podcast podcast = podcastList.get(position);
        mViewModel.deletePodcast(podcast);
        Snackbar.make(binding.favFragmentContainer, getString(R.string.remove_fav_msg) , Snackbar.LENGTH_SHORT).show();
    }
}
