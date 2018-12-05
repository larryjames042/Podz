package mirror.co.larry.podz.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import mirror.co.larry.podz.App.AnalyticsApplication;
import mirror.co.larry.podz.R;
import mirror.co.larry.podz.adapter.PagerAdapter;
import mirror.co.larry.podz.databinding.FragmentDiscoverBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    public static final String TAG = DiscoverFragment.class.getSimpleName();
    private PagerAdapter pagerAdapter;
    FragmentDiscoverBinding binding;
    Tracker mTracker;
    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "DiscoverFragmentScreen ");
        mTracker.setScreenName(TAG );
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.appBar.toolbar);
        binding.appBar.toolbar.setTitle(R.string.app_name);

        // Set the text for each tab
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_best));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_self_help));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_news_politic));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Using PagerAdapter to manage page views  in fragment
        pagerAdapter = new PagerAdapter(getChildFragmentManager(), binding.tabLayout.getTabCount());
        binding.pager.setAdapter(pagerAdapter);

        // Setting a listener for clicks
        binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        View v = binding.getRoot();
        return v;
    }


}
