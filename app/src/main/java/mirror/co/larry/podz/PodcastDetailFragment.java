package mirror.co.larry.podz;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mirror.co.larry.podz.Model.Episode;
import mirror.co.larry.podz.Util.FetchBestPodcast;
import mirror.co.larry.podz.Util.ItemOffsetDecoration;
import mirror.co.larry.podz.Util.ItemOffsetDecorationGenre;
import mirror.co.larry.podz.Util.NetworkUtil;
import mirror.co.larry.podz.Util.UiUtil;
import mirror.co.larry.podz.adapter.EpisodeAdapter;
import mirror.co.larry.podz.adapter.GenreAdapter;
import mirror.co.larry.podz.databinding.FragmentPodcastDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodcastDetailFragment extends Fragment implements EpisodeAdapter.OnEpisodeClickListener {
    public static final String TAG = "PodcastDetailFragment";
    public static final String PODCAST_ID = "podcast_id";
    FragmentPodcastDetailBinding binding;
    List<Episode> episodeList;
    List<String> genresList;
    GenreAdapter genreAdapter;
    EpisodeAdapter episodeAdapter;
    String podcastId, podcastTitle, thumbnail, country, podcastDescription, website, listennoteUrl, publisher;

    public PodcastDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_podcast_detail,container, false);
        View view = binding.getRoot();
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        String id = "";
        episodeList = new ArrayList<>();
        genresList = new ArrayList<>();
        if(getArguments().containsKey(PODCAST_ID)){
            id = getArguments().getString(PODCAST_ID);
            Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        }

        FetchBestPodcast asyncTask = new FetchBestPodcast(new FetchBestPodcast.AsynctaskListener() {
            @Override
            public void onPost(String result) {
                if(result!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        podcastId = jsonObject.getString("id");
                        podcastTitle = jsonObject.getString("title");
                        thumbnail = jsonObject.getString("thumbnail");
                        country = jsonObject.getString("country");
                        podcastDescription = jsonObject.getString("description");
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
                                .into(UiUtil.getRoundedImageTarget(getContext(), binding.podcastThumbnail, 14));
                        binding.podcastDetailContent.labelAbtPodcast.setText(R.string.about_podcast);
                        binding.podcastDetailContent.podcastDescription.setText(podcastDescription);
                        binding.podcastDetailContent.previousEpisode.setText(R.string.previous_episode);
                        binding.podcastDetailContent.latestEpisode.setText(R.string.latest_episode);

                        //genre list
                        genreAdapter = new GenreAdapter(getContext(), genresList);
                        binding.podcastDetailContent.rvPodcastGenre.setHasFixedSize(true);
                        binding.podcastDetailContent.rvPodcastGenre.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        ItemOffsetDecorationGenre offsetDec = new ItemOffsetDecorationGenre(getContext(),R.dimen.genre_offset);
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
            public void onPre() {

            }
        });
        asyncTask.execute(NetworkUtil.builtPodcastDetailUrl(id));
        return view;
    }

    @Override
    public void episodeOnClick(View view, Bundle bundle) {
        EpisodeDetailFragment fragment = new EpisodeDetailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, fragment)
                .addToBackStack(TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if(item.getItemId() == android.R.id.home){
//            getActivity().getSupportFragmentManager().popBackStack();
//        }
        return super.onOptionsItemSelected(item);
    }
}
