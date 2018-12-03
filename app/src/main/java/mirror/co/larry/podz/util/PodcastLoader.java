package mirror.co.larry.podz.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class PodcastLoader extends AsyncTaskLoader<String>{

    String requestUrl;

    public PodcastLoader(@NonNull Context context, String url) {
        super(context);
        requestUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtil.getPodcast(requestUrl);
    }
}
