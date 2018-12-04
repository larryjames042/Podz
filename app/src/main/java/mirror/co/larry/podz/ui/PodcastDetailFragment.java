package mirror.co.larry.podz.ui;


import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.model.Episode;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.util.ItemOffsetDecorationGenre;
import mirror.co.larry.podz.util.NetworkUtil;
import mirror.co.larry.podz.util.PodcastLoader;
import mirror.co.larry.podz.util.UiUtil;
import mirror.co.larry.podz.adapter.EpisodeAdapter;
import mirror.co.larry.podz.adapter.GenreAdapter;
import mirror.co.larry.podz.databinding.FragmentPodcastDetailBinding;
import mirror.co.larry.podz.viewModel.PodcastViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodcastDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, EpisodeAdapter.OnEpisodeClickListener {
    public static final String TAG = "PodcastDetailFragment";
    public static final String PODCAST_ID = "podcast_id";
    private FragmentPodcastDetailBinding binding;
    private List<Episode> episodeList;
    private List<String> genresList;
    private GenreAdapter genreAdapter;
    private EpisodeAdapter episodeAdapter;
    private ProgressDialog progressDialog;
    private String podcastId, podcastTitle, thumbnail, country, podcastDescription, website, listennoteUrl, publisher;
    private PodcastViewModel viewModel;


    public PodcastDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle : ", "Detail onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lifecycle : ", "Detail onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_podcast_detail,container, false);
        viewModel = ViewModelProviders.of(this).get(PodcastViewModel.class);
        View view = binding.getRoot();
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getActivity().getString(R.string.loading_title));
        String id = "";
        episodeList = new ArrayList<>();
        genresList = new ArrayList<>();

        if(getArguments().containsKey(PODCAST_ID)){
            id = getArguments().getString(PODCAST_ID);
        }
        // check the internet connection
        if(NetworkUtil.isOnline(getActivity())){
            Bundle urlBundle = new Bundle();
            urlBundle.putString("url_string", NetworkUtil.buildPodcastDetailUrl(id));
            if(getActivity().getSupportLoaderManager().getLoader(1)!=null){
                getActivity().getSupportLoaderManager().restartLoader(1 ,urlBundle, this);
            }
            getActivity().getSupportLoaderManager().initLoader(1, urlBundle, this);
        }else{
            Snackbar.make(binding.getRoot(), getString(R.string.check_network_msg), Snackbar.LENGTH_LONG).show();
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // insert podcast to favourite
       binding.podcastDetailContent.btFavourite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mirror.co.larry.room.Podcast podcast = new mirror.co.larry.room.Podcast(podcastId, podcastTitle, podcastDescription,publisher, thumbnail);
               viewModel.insertPodcast(podcast);
               Snackbar.make(binding.podcastDetailMainLayout, getString(R.string.add_fav_msg) , Snackbar.LENGTH_SHORT).show();
           }
       });
    }

    @Override
    public void episodeOnClick(View view, Bundle bundle) {
        EpisodeDetailFragment fragment = new EpisodeDetailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, fragment, EpisodeDetailFragment.TAG)
                .addToBackStack(TAG)
                .commit();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        progressDialog.show();
        return new PodcastLoader(getContext(), bundle.getString("url_string"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {
        progressDialog.dismiss();
        if(result!=null){
            binding.podcastDetailMainLayout.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(result);
                podcastId = jsonObject.getString("id");
                podcastTitle = jsonObject.getString("title");
                thumbnail = jsonObject.getString("thumbnail");
                country = jsonObject.getString("country");
                podcastDescription = android.text.Html.fromHtml(jsonObject.getString("description")).toString();
                website = jsonObject.getString("website");
                listennoteUrl = jsonObject.getString("listennotes_url");
                publisher = jsonObject.getString("publisher");

                JSONArray genres = jsonObject.getJSONArray("genres");
                JSONArray episodes = jsonObject.getJSONArray("episodes");
                for(int i=0 ;i<genres.length();i++){
                    genresList.add(genres.getString(i));
                }

                for(int i=0; i<episodes.length(); i++){
                    JSONObject episodeObj = episodes.getJSONObject(i);
                    String audio = episodeObj.getString("audio");
                    String id = episodeObj.getString("id");
                    String title = episodeObj.getString("title");
                    String thumbnail = episodeObj.getString("thumbnail");
                    String description = android.text.Html.fromHtml(episodeObj.getString("description")).toString();
                    String listenNoteUrl = episodeObj.getString("listennotes_url");
                    int audioLength = episodeObj.getInt("audio_length");
                    long pubDate = episodeObj.getLong("pub_date_ms");

                    episodeList.add(new Episode(audio,id,title,thumbnail, description,listenNoteUrl,pubDate, audioLength, podcastTitle));
                }

                // fill the view with data

                Glide.with(getActivity())
                        .asBitmap()
                        .load(thumbnail)
                        .into(UiUtil.getRoundedImageTarget(getActivity(), binding.podcastThumbnail, 14));
                binding.podcastDetailContent.labelAbtPodcast.setText(R.string.about_podcast);
                binding.podcastDetailContent.podcastDescription.setText(podcastDescription);
                binding.podcastDetailContent.previousEpisode.setText(R.string.previous_episode);
                binding.podcastDetailContent.latestEpisode.setText(R.string.latest_episode);

                //genre list
                genreAdapter = new GenreAdapter(getActivity(), genresList);
                binding.podcastDetailContent.rvPodcastGenre.setHasFixedSize(true);
                binding.podcastDetailContent.rvPodcastGenre.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                ItemOffsetDecorationGenre offsetDec = new ItemOffsetDecorationGenre(getActivity(),R.dimen.genre_offset);
                binding.podcastDetailContent.rvPodcastGenre.addItemDecoration(offsetDec);
                binding.podcastDetailContent.rvPodcastGenre.setAdapter(genreAdapter);

                //get the last Episode
                final Episode lastestEpisode = episodeList.get(0);
                // change time format
                long unixSeconds = lastestEpisode.getPubDate();
                Date date = new java.util.Date(unixSeconds);
                SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                final String formattedDate = sdf.format(date);
                // latest episode
                binding.podcastDetailContent.episodeItem.episodeName.setText(lastestEpisode.getTitle());
                binding.podcastDetailContent.episodeItem.publishDate.setText(formattedDate);
                binding.podcastDetailContent.episodeItem.episodeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b = new Bundle();
                        b.putString(EpisodeDetailFragment.KEY_AUDIO, lastestEpisode.getAudio());
                        b.putString( EpisodeDetailFragment.KEY_DESCRIPTION , lastestEpisode.getDescription());
                        b.putString( EpisodeDetailFragment.KEY_ID  , lastestEpisode.getId());
                        b.putString( EpisodeDetailFragment.KEY_LISTEN_NOTE_URL , lastestEpisode.getListenNoteUrl());
                        b.putString( EpisodeDetailFragment.KEY_THUMBNAIL , lastestEpisode.getThumbnail());
                        b.putString(EpisodeDetailFragment.KEY_TITLE  , lastestEpisode.getTitle());
                        b.putInt( EpisodeDetailFragment.KEY_AUDIO_LENGTH , lastestEpisode.getAudioLength());
                        b.putString( EpisodeDetailFragment.KEY_PUB_DATE, formattedDate);
                        EpisodeDetailFragment fragment = new EpisodeDetailFragment();
                        fragment.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_container, fragment)
                                .addToBackStack(TAG)
                                .commit();
                    }
                });

                // previous episode
                episodeList.remove(0);
                episodeAdapter = new EpisodeAdapter(getActivity(), episodeList, PodcastDetailFragment.this );
                binding.podcastDetailContent.rvPreviousEpisode.setHasFixedSize(true);
                binding.podcastDetailContent.rvPreviousEpisode.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
                binding.podcastDetailContent.rvPreviousEpisode.addItemDecoration(decoration);
                binding.podcastDetailContent.rvPreviousEpisode.setAdapter(episodeAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycle : ", "Detail onResume");
        if(getUserVisibleHint()){

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("lifecycle : ", "Detail onStop");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("lifecycle : ", "Detail onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle : ", "Detail onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("lifecycle : ", "Detail onDestroyView");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("lifecycle : ", "Detail onAttach");
    }
}
