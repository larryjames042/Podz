package mirror.co.larry.podz.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Podcast implements Serializable {
    private String id;
    private String title;
    private String description;
    private String publisher;
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
