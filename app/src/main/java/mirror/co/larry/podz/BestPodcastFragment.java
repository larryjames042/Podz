package mirror.co.larry.podz;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Network;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mirror.co.larry.podz.Model.Podcast;
import mirror.co.larry.podz.Util.FetchBestPodcast;
import mirror.co.larry.podz.Util.ItemOffsetDecoration;
import mirror.co.larry.podz.Util.NetworkUtil;
import mirror.co.larry.podz.adapter.PodcastAdapter;
import mirror.co.larry.podz.databinding.FragmentBestPodcastBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestPodcastFragment extends Fragment implements PodcastAdapter.OnPodcastClickListener {
    public static final String TAG = "BestPodcastFragment";
    List<Podcast>  podcastList;
    PodcastAdapter podcastAdapter;
    FragmentBestPodcastBinding binding;
    ProgressDialog progressDialog;

    public BestPodcastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycle : ", "Best onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("lifecycle : ", "Best onStop");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle : ", "Best onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_best_podcast, container, false);
        View view = binding.getRoot();
        progressDialog = new ProgressDialog(getActivity());
        podcastList = new ArrayList<>();
        progressDialog.setTitle(getActivity().getString(R.string.loading_title));
        FetchBestPodcast asyncTask = new FetchBestPodcast(new FetchBestPodcast.AsynctaskListener() {

            @Override
            public void onPre() {
                progressDialog.show();
            }

            @Override
            public void onPost(String result) {
                progressDialog.dismiss();
                if(result!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("channels");
                        for(int i=0; i< jsonArray.length(); i++){
                            JSONObject podcast = jsonArray.getJSONObject(i);
                            String id = podcast.getString("id");
                            String title = podcast.getString("title");
                            String thumbnail = podcast.getString("thumbnail");
                            String description = android.text.Html.fromHtml(podcast.getString("description")).toString();
                            String publisher = podcast.getString("publisher");
                            podcastList.add(new Podcast(id,title,description,publisher,thumbnail));
                        }
                        podcastAdapter = new PodcastAdapter(getActivity(), podcastList, BestPodcastFragment.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                        binding.rvBestPodcast.setHasFixedSize(true);
                        binding.rvBestPodcast.setLayoutManager(layoutManager);
                        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
                        binding.rvBestPodcast.addItemDecoration(decoration);
                        binding.rvBestPodcast.setAdapter(podcastAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        asyncTask.execute(NetworkUtil.builtBestPodcastUrl());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void podcastItemClickListener(View view, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(PodcastDetailFragment.PODCAST_ID, id);
        Fragment podcastDetailFragment = new PodcastDetailFragment();
        podcastDetailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, podcastDetailFragment)
                .addToBackStack(TAG)
                .commit();
    }
}
