package mirror.co.larry.podz.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import mirror.co.larry.podz.dao.PodcastDao;
import mirror.co.larry.podz.model.Podcast;

@Database(entities = {Podcast.class}, version = 1)
public abstract class PodcastRoomDatabase extends RoomDatabase {
    private static volatile PodcastRoomDatabase INSTANCE;
    public abstract PodcastDao podcastDao();

    static PodcastRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (PodcastRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PodcastRoomDatabase.class,
                            "podcast_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
