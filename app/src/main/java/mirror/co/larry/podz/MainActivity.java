package mirror.co.larry.podz;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import mirror.co.larry.podz.adapter.PodcastAdapter;
import mirror.co.larry.podz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // default Fragment
        loadFragment(new DiscoverFragment());
       binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // Bottom Navigation Items onClick
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    fragment = new DiscoverFragment();
                    break;
                case R.id.navigation_Subscription:
                    fragment = new FavFragment();
                    break;
                case R.id.navigation_Search:
                    fragment = new SearchFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

//    @Override
//    public void podcastItemClickListener(View view, String id) {
//        Bundle bundle = new Bundle();
//        bundle.putString(PodcastDetailFragment.PODCAST_ID, id);
//        Fragment podcastDetailFragment = new PodcastDetailFragment();
//        podcastDetailFragment.setArguments(bundle);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.content_container, podcastDetailFragment, PodcastDetailFragment.TAG)
//                .addToBackStack(PodcastDetailFragment.TAG)
//                .commit();
//    }

}
