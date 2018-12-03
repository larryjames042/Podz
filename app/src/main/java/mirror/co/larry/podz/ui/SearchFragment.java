package mirror.co.larry.podz.ui;


import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
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
import mirror.co.larry.podz.adapter.SearchResultAdapter;
import mirror.co.larry.podz.databinding.FragmentSearchBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, SearchResultAdapter.OnPodcastClickListener {

    private FragmentSearchBinding binding;
    private MenuItem searchItem;
    private SearchView mSearchView;
    private EditText searchEditText;
    private String mQueryText;
    private ProgressDialog progressDialog;
    private List<Podcast> podcastList;
    private SearchResultAdapter searchResultAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View v = binding.getRoot();
        podcastList = new ArrayList<Podcast>();
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.appBar.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.recyclerview_item_offset);
        binding.rvSearchResult.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.rvSearchResult.setHasFixedSize(true);
        binding.rvSearchResult.setLayoutManager(layoutManager);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView)searchItem.getActionView();

        int searchEditTextId = android.support.v7.appcompat.R.id.search_src_text;
        searchEditText = (EditText)mSearchView.findViewById(searchEditTextId);
        searchEditText.setTextColor( getResources().getColor( R.color.grey_900));
        searchEditText.setHintTextColor(getResources().getColor( R.color.grey_500));
        searchEditText.setBackground(getResources().getDrawable(R.drawable.searchview_background));

        mSearchView.setActivated(true);
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.onActionViewExpanded();
        searchItem.expandActionView();
        mSearchView.requestFocus();
        mSearchView.setIconified(false);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mQueryText = s;
                Bundle urlBundle = new Bundle();
                urlBundle.putString("url_string", NetworkUtil.builtPodcastQueryUrl(mQueryText));
                if(getActivity().getSupportLoaderManager().getLoader(2)!=null){
                    getActivity().getSupportLoaderManager().restartLoader(2 ,urlBundle, SearchFragment.this);
                }
                getActivity().getSupportLoaderManager().initLoader(2, urlBundle, SearchFragment.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
                .addToBackStack(null)
                .commit();
    }
}
