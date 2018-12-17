package mirror.co.larry.podz.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.model.Podcast;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.util.NetworkUtil;
import mirror.co.larry.podz.util.OnVisibleListener;
import mirror.co.larry.podz.util.PodcastLoader;
import mirror.co.larry.podz.adapter.PodcastAdapter;
import mirror.co.larry.podz.databinding.FragmentBestPodcastBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class BestPodcastFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    public static final String TAG = "BestPodcastFragment";
    private List<Podcast>  podcastList;
    private PodcastAdapter podcastAdapter;
    private FragmentBestPodcastBinding binding;
    private ProgressDialog progressDialog;

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
        binding.rvBestPodcast.setFocusable(true);
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvBestPodcast.addItemDecoration(decoration);

        // check connection
        if(NetworkUtil.isOnline(getActivity())){
            Bundle urlBundle = new Bundle();
            urlBundle.putString("url_string", NetworkUtil.buildBestPodcastUrl());
            if(getActivity().getSupportLoaderManager().getLoader(0)!=null){
                getActivity().getSupportLoaderManager().initLoader(0 ,null, this);
            }
            getActivity().getSupportLoaderManager().initLoader(0, urlBundle, this);
        }else{
            Glide.with(getContext()).load(R.drawable.ic_offline).into(binding.errorImageView);
            binding.rvBestPodcast.setBackgroundResource(R.drawable.ic_offline);
            Snackbar.make(binding.getRoot(), getString(R.string.check_network_msg), Snackbar.LENGTH_LONG).show();
        }


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
                podcastAdapter = new PodcastAdapter(getActivity(), podcastList, (PodcastAdapter.OnPodcastClickListener) getActivity());
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
