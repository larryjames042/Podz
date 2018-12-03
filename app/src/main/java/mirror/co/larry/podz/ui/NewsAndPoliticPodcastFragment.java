package mirror.co.larry.podz.ui;


import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.adapter.PodcastAdapter;
import mirror.co.larry.podz.databinding.FragmentNewsPoliticPodcastBinding;
import mirror.co.larry.podz.model.Podcast;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.util.NetworkUtil;
import mirror.co.larry.podz.util.PodcastLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsAndPoliticPodcastFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> , PodcastAdapter.OnPodcastClickListener{

    public static final String TAG = "NewsAndPoliticPodcastFragment";
    private List<Podcast> podcastList;
    private PodcastAdapter podcastAdapter;
    private FragmentNewsPoliticPodcastBinding binding;
    private ProgressDialog progressDialog;


    public NewsAndPoliticPodcastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle urlBundle = new Bundle();
        urlBundle.putString("url_string", NetworkUtil.buildNewsAndPoliticPodcastUrl());
        if(getActivity().getSupportLoaderManager().getLoader(4)!=null){
            getActivity().getSupportLoaderManager().initLoader(4 ,null, this);
        }
        getActivity().getSupportLoaderManager().initLoader(4, urlBundle, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news_politic_podcast, container, false);
        View view = binding.getRoot();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getActivity().getString(R.string.loading_title));
        podcastList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.rvNewsPoliticPodcast.setHasFixedSize(true);
        binding.rvNewsPoliticPodcast.setLayoutManager(layoutManager);
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvNewsPoliticPodcast.addItemDecoration(decoration);

        return view;
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
            try {
                podcastList.clear();
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
                podcastAdapter = new PodcastAdapter(getActivity(), podcastList, NewsAndPoliticPodcastFragment.this);
                binding.rvNewsPoliticPodcast.setAdapter(podcastAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

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
