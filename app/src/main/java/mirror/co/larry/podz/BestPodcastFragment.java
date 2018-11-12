package mirror.co.larry.podz;


import android.databinding.DataBindingUtil;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mirror.co.larry.podz.Model.Podcast;
import mirror.co.larry.podz.Util.FetchBestPodcast;
import mirror.co.larry.podz.Util.NetworkUtil;
import mirror.co.larry.podz.databinding.FragmentBestPodcastBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestPodcastFragment extends Fragment {

    List<Podcast>  podcastList;

    public BestPodcastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentBestPodcastBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_best_podcast, container, false);
        View view = binding.getRoot();
        podcastList = new ArrayList<>();
        FetchBestPodcast asyncTask = new FetchBestPodcast(new FetchBestPodcast.AsynctaskListener() {
            @Override
            public void onPost(String result) {
                if(result!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("channels");
                        for(int i=0; i< jsonArray.length(); i++){
                            JSONObject podcast = jsonArray.getJSONObject(i);
                            String id = podcast.getString("id");
                            String title = podcast.getString("title");
                            String thumbnail = podcast.getString("thumbnail");
                            String description = podcast.getString("description");
                            String publisher = podcast.getString("publisher");
                            podcastList.add(new Podcast(id,title,description,publisher,thumbnail));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        asyncTask.execute(NetworkUtil.builtBestPodcastUrl());
        return view;
    }

}
