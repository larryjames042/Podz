package mirror.co.larry.podz.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import mirror.co.larry.podz.ui.BestPodcastFragment;
import mirror.co.larry.podz.ui.SelfHelpPodcastFragment;
import mirror.co.larry.podz.ui.NewsAndPoliticPodcastFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    SparseArray<Fragment> registeredFragment = new SparseArray<>();
    public PagerAdapter(FragmentManager fm, int NumOfTabs){
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new BestPodcastFragment();
            case 1:
                return new SelfHelpPodcastFragment();
            case 2:
                return new NewsAndPoliticPodcastFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        registeredFragment.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragment.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position){
        return registeredFragment.get(position);
    }
}
