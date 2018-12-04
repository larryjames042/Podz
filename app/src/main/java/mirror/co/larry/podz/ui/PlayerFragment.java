package mirror.co.larry.podz.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mirror.co.larry.podz.R;
import mirror.co.larry.podz.databinding.FragmentPlayerBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    FragmentPlayerBinding binding;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        View view = binding.getRoot();
        return view;
    }
}
