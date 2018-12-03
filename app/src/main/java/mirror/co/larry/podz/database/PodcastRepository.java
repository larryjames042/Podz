package mirror.co.larry.podz.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import mirror.co.larry.podz.dao.PodcastDao;
import mirror.co.larry.podz.model.Podcast;

public class PodcastRepository {
    private PodcastDao mPodcastDao;
    private LiveData<List<Podcast>> mAllPodcast;

    public PodcastRepository(Application application){
        PodcastRoomDatabase db = PodcastRoomDatabase.getDatabase(application);
        mPodcastDao = db.podcastDao();
        mAllPodcast = mPodcastDao.getAllPodcast();
    }

    public LiveData<List<Podcast>> getAllPodcast(){
        return mAllPodcast;
    }

    public void insertPodcast(Podcast podcast){
        new insertAsyncTask(mPodcastDao).execute(podcast);
    }

    private static class insertAsyncTask extends AsyncTask<Podcast, Void, Void>{

        private PodcastDao mAsyncTaskDao;

        public insertAsyncTask(PodcastDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Podcast... podcasts) {
            mAsyncTaskDao.insertPodcast(podcasts[0]);
            return null;
        }
    }
}
