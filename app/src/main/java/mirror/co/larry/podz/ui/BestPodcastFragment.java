package mirror.co.larry.podz.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.model.Podcast;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.util.NetworkUtil;
import mirror.co.larry.podz.util.PodcastLoader;
import mirror.co.larry.podz.adapter.PodcastAdapter;
import mirror.co.larry.podz.databinding.FragmentBestPodcastBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestPodcastFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, PodcastAdapter.OnPodcastClickListener {
    public static final String TAG = "BestPodcastFragment";
    private List<Podcast>  podcastList;
    private PodcastAdapter podcastAdapter;
    private FragmentBestPodcastBinding binding;
    private ProgressDialog progressDialog;
    private boolean isAlreadyLoaded = false;

    public BestPodcastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lifecycle : ", "Best onResume");

        Bundle urlBundle = new Bundle();
        urlBundle.putString("url_string", NetworkUtil.builtBestPodcastUrl());
        if(getActivity().getSupportLoaderManager().getLoader(0)!=null){
            getActivity().getSupportLoaderManager().initLoader(0 ,null, this);
        }
        getActivity().getSupportLoaderManager().initLoader(0, urlBundle, this);
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
    public void onStop() {
        super.onStop();
        Log.d("lifecycle : ", "Best onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle : ", "Best onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("lifecycle : ", "Best onDestroyView");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("lifecycle : ", "Best onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lifecycle : ", "Best onCreateView");
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_best_podcast, container, false);
        View view = binding.getRoot();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getActivity().getString(R.string.loading_title));
        podcastList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.rvBestPodcast.setHasFixedSize(true);
        binding.rvBestPodcast.setLayoutManager(layoutManager);
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvBestPodcast.addItemDecoration(decoration);

        return view;
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
                podcastAdapter = new PodcastAdapter(getActivity(), podcastList, BestPodcastFragment.this);
                binding.rvBestPodcast.setAdapter(podcastAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
