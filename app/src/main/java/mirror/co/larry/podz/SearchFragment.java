package mirror.co.larry.podz;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import mirror.co.larry.podz.databinding.FragmentSearchBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    MenuItem searchItem;
    SearchView mSearchView;
    EditText searchEditText;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View v = binding.getRoot();

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.appBar.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

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

//        Point p = new Point();
//        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(p.x, ViewGroup.LayoutParams.MATCH_PARENT);
//        mSearchView.setLayoutParams(params);

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
}
