package mirror.co.larry.podz.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mirror.co.larry.podz.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class InteviewPodcastFragment extends Fragment {


    public InteviewPodcastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inteview_podcast, container, false);
    }

}