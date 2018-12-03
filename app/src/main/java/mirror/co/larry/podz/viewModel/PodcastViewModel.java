package mirror.co.larry.podz.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.sql.PooledConnection;

import mirror.co.larry.podz.database.PodcastRepository;
import mirror.co.larry.podz.model.Podcast;

public class PodcastViewModel extends AndroidViewModel {

    private PodcastRepository mRepository;
    private LiveData<List<Podcast>> mAllPodcast;

    public PodcastViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PodcastRepository(application);
        mAllPodcast = mRepository.getAllPodcast();
    }

    public LiveData<List<Podcast>> getmAllPodcast(){
        return mAllPodcast;
    }

    public void insert(Podcast podcast){
        mRepository.insertPodcast(podcast);
    }

}
