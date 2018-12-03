package mirror.co.larry.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Podcast.class}, version = 1)
public abstract class PodcastRoomDatabase extends RoomDatabase {
    private static volatile PodcastRoomDatabase INSTANCE;
    public abstract PodcastDao podcastDao();

    public static PodcastRoomDatabase getDatabase(final Context context){
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
