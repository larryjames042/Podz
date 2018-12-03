package mirror.co.larry.podz.util;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public  class NetworkUtil {
    final static String LISTEN_NOTE_BASE_URL = "https://listennotes.p.mashape.com/api/v1";
    final static String BEST_PODCAST = "best_podcasts";
    final static String QUERY_KEY = "q";
    final static String TYPE_KEY = "type";
    final static String PODCAST = "podcasts";
    final static String SEARCH = "search";
    final static String GENRE_ID = "genre_id";
    final static String X_MASHAPE_KEY = "X-Mashape-Key";
    final static String X_MASHAPE_VALUE = "GlySrzrNSTmshoG80vE2H6EER9W2p1wJHyKjsnXb2DqDJ3EPkt";
    final static String ACCEPT_KEY = "Accept";
    final static String ACCEPT_VALUE = "application/json";
    final static String SELF_HELP = "90";
    final static String NEWS_POLITIC = "99";



    private static  final String LOG_TAG = NetworkUtil.class.getSimpleName();


    public static  String buildBestPodcastUrl(){
        Uri builtURI = Uri.parse(LISTEN_NOTE_BASE_URL).buildUpon()
                .appendPath(BEST_PODCAST)
                .build();

        return builtURI.toString();
    }

    public static  String buildSelfHelpPodcastUrl(){
        Uri builtURI = Uri.parse(LISTEN_NOTE_BASE_URL).buildUpon()
                .appendPath(BEST_PODCAST)
                .appendQueryParameter(GENRE_ID, SELF_HELP)
                .build();

        return builtURI.toString();
    }

    public static  String buildNewsAndPoliticPodcastUrl(){
        Uri builtURI = Uri.parse(LISTEN_NOTE_BASE_URL).buildUpon()
                .appendPath(BEST_PODCAST)
                .appendQueryParameter(GENRE_ID, NEWS_POLITIC)
                .build();

        return builtURI.toString();
    }

    public static String buildPodcastDetailUrl(String podcastId){
        Uri builtURI = Uri.parse(LISTEN_NOTE_BASE_URL).buildUpon()
                .appendPath(PODCAST)
                .appendPath(podcastId)
                .build();

        return builtURI.toString();
    }

    public static String buildPodcastQueryUrl(String queryText){
        Uri builtURI = Uri.parse(LISTEN_NOTE_BASE_URL).buildUpon()
                .appendPath(SEARCH)
                .appendQueryParameter(QUERY_KEY, queryText)
                .appendQueryParameter(TYPE_KEY, "podcast")
                .build();

        return builtURI.toString();
    }


    // helper method to parse JSON
    static  String getPodcast(String queryString){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String podcastJSONString = null;
        try{
            URL requestUrl = new URL(queryString);
            urlConnection = (HttpURLConnection)requestUrl.openConnection();
            urlConnection.setRequestProperty(X_MASHAPE_KEY, X_MASHAPE_VALUE);
            urlConnection.setRequestProperty(ACCEPT_KEY, ACCEPT_VALUE);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream==null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine())!=null){
                buffer.append(line+"\n");
            }

            if(buffer.length()==0){
                return null;
            }
            podcastJSONString = buffer.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return podcastJSONString;

    }
}
