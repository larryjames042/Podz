package mirror.co.larry.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "podcast_table")
public class Podcast {
    @PrimaryKey
    @ColumnInfo
    @NonNull
    private String id;
    @ColumnInfo
    @NonNull
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private String publisher;
    @ColumnInfo
    private String thumbnail;

    public Podcast(String id, String title, String description, String publisher, String thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
