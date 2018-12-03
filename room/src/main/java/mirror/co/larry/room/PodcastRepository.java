package mirror.co.larry.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

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

    public void deletePodcast(Podcast podcast){
        new deleteAsyncTask(mPodcastDao).execute(podcast);
    }

    private static class insertAsyncTask extends AsyncTask<Podcast, Void, Void> {

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

    private static class deleteAsyncTask extends AsyncTask<Podcast, Void, Void>{

        private PodcastDao mAsyncTaskDao;

        public deleteAsyncTask(PodcastDao mAsyncTaskDao){
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(Podcast... podcasts) {
            mAsyncTaskDao.deletePodcast(podcasts[0].getId());
            return null;
        }
    }
}