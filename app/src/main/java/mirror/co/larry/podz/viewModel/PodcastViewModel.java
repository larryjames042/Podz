package mirror.co.larry.podz.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import mirror.co.larry.room.Podcast;

public class PodcastViewModel extends AndroidViewModel {

    private mirror.co.larry.room.PodcastRepository mRepostory;
    private LiveData<List<mirror.co.larry.room.Podcast>> mAllPodcast;

    public PodcastViewModel(@NonNull Application application) {
        super(application);
        mRepostory = new mirror.co.larry.room.PodcastRepository(application);
        mAllPodcast = mRepostory.getAllPodcast();
    }

    public LiveData<List<mirror.co.larry.room.Podcast>> getAllPodcast(){
        return mAllPodcast;
    }

    public void insertPodcast(mirror.co.larry.room.Podcast podcast){
        mRepostory.insertPodcast(podcast);
    }

    public void deletePodcast(Podcast podcast){
        mRepostory.deletePodcast(podcast);
    }
}
