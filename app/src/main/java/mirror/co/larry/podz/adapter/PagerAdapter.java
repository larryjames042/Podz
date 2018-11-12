package mirror.co.larry.podz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import mirror.co.larry.podz.BestPodcastFragment;
import mirror.co.larry.podz.HotPodcastFragment;
import mirror.co.larry.podz.InteviewPodcastFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

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
                return new HotPodcastFragment();
            case 2:
                return new InteviewPodcastFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
