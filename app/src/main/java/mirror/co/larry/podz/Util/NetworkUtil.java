package mirror.co.larry.podz.Util;

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
    final static String X_MASHAPE_KEY = "X-Mashape-Key";
    final static String X_MASHAPE_VALUE = "GlySrzrNSTmshoG80vE2H6EER9W2p1wJHyKjsnXb2DqDJ3EPkt";
    final static String ACCEPT_KEY = "Accept";
    final static String ACCEPT_VALUE = "application/json";


    private static  final String LOG_TAG = NetworkUtil.class.getSimpleName();


    public static  String builtBestPodcastUrl(){
        Uri builtURI = Uri.parse(LISTEN_NOTE_BASE_URL).buildUpon()
                .appendPath(BEST_PODCAST)
                .build();

        return builtURI.toString();
    }

    static  String getBestPodcast(String queryString){
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
