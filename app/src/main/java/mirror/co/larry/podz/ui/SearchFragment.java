package mirror.co.larry.podz.ui;


import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mirror.co.larry.podz.App.AnalyticsApplication;
import mirror.co.larry.podz.R;
import mirror.co.larry.podz.model.Podcast;
import mirror.co.larry.podz.util.ItemOffsetDecoration;
import mirror.co.larry.podz.util.NetworkUtil;
import mirror.co.larry.podz.util.PodcastLoader;
import mirror.co.larry.podz.adapter.SearchResultAdapter;
import mirror.co.larry.podz.databinding.FragmentSearchBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, SearchResultAdapter.OnPodcastClickListener {
    private static final String TAG = SearchFragment.class.getSimpleName();
    private FragmentSearchBinding binding;
    private String mQueryText;
    private ProgressDialog progressDialog;
    private List<Podcast> podcastList;
    private SearchResultAdapter searchResultAdapter;
    Tracker mTracker;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "SearchFragmentScreen ");
        mTracker.setScreenName(TAG );
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View v = binding.getRoot();
        podcastList = new ArrayList<>();

        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvSearchResult.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.rvSearchResult.setHasFixedSize(true);
        binding.rvSearchResult.setLayoutManager(layoutManager);

        binding.searchView.setActivated(true);
        binding.searchView.onActionViewExpanded();
        binding.searchView.setIconified(false);
        binding.searchView.requestFocus();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mQueryText = s;
                binding.searchView.clearFocus();
                // check the connection first
                if(NetworkUtil.isOnline(getActivity())){
                    Bundle urlBundle = new Bundle();
                    urlBundle.putString("url_string", NetworkUtil.buildPodcastQueryUrl(mQueryText));
                    if(getActivity().getSupportLoaderManager().getLoader(2)!=null){
                        getActivity().getSupportLoaderManager().restartLoader(2 ,urlBundle, SearchFragment.this);
                    }
                    getActivity().getSupportLoaderManager().initLoader(2, urlBundle, SearchFragment.this);
                }else{
                    Snackbar.make(binding.getRoot(), getString(R.string.check_network_msg), Snackbar.LENGTH_LONG).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        progressDialog.show();
        return new PodcastLoader(getContext(),bundle.getString("url_string"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {
        progressDialog.dismiss();

        if(result!=null){
            Log.d("request_result", result);
            try {
                podcastList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for(int i=0; i< jsonArray.length(); i++){
                    JSONObject podcast = jsonArray.getJSONObject(i);
                    String id = podcast.getString("id");
                    String title = podcast.getString("title_original");
                    String thumbnail = podcast.getString("thumbnail");
                    String description = android.text.Html.fromHtml(podcast.getString("description_original")).toString();
                    String publisher = podcast.getString("publisher_original");
                    podcastList.add(new Podcast(id,title,description,publisher,thumbnail));
                }
                searchResultAdapter = new SearchResultAdapter(getActivity(), podcastList, SearchFragment.this);
                binding.rvSearchResult.setAdapter(searchResultAdapter);


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
                .addToBackStack(TAG)
                .commit();
    }
}
