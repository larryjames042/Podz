package mirror.co.larry.podz.model;

public class Episode {
    private String audio;
    private String id;
    private String title;
    private String thumbnail;
    private String description;
    private String listenNoteUrl;
    private long pubDate;
    private int audioLength;
    private String podcastName;

    public Episode(String audio, String id, String title, String thumbnail, String description, String listenNoteUrl, long pubDate, int audioLength, String podcastName) {
        this.audio = audio;
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.listenNoteUrl = listenNoteUrl;
        this.pubDate = pubDate;
        this.audioLength = audioLength;
        this.podcastName = podcastName;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListenNoteUrl() {
        return listenNoteUrl;
    }

    public void setListenNoteUrl(String listenNoteUrl) {
        this.listenNoteUrl = listenNoteUrl;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public int getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(int audioLength) {
        this.audioLength = audioLength;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }
}
