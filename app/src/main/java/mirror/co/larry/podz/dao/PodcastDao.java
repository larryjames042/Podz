package mirror.co.larry.podz.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import mirror.co.larry.podz.model.Podcast;

@Dao
public interface PodcastDao {
    @Query("SELECT * FROM PODCAST_TABLE ORDER BY title ASC")
    LiveData<List<Podcast>> getAllPodcast();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPodcast(Podcast podcast);

    @Query("DELETE FROM podcast_table WHERE :podcastId = id")
    void deletePodcast(int podcastId);

    @Query("DELETE FROM podcast_table")
    void deleteAll();
}
