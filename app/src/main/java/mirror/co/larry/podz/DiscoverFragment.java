package mirror.co.larry.podz;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mirror.co.larry.podz.adapter.PagerAdapter;
import mirror.co.larry.podz.databinding.FragmentDiscoverBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentDiscoverBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.appBar.toolbar);
        binding.appBar.toolbar.setTitle(R.string.app_name);

        // Set the text for each tab
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_best));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_hot));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_interview));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Using PagerAdapter to manage page views  in fragment
        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), binding.tabLayout.getTabCount());
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
