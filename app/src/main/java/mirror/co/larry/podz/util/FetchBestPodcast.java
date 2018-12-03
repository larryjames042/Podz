package mirror.co.larry.podz.util;

import android.os.AsyncTask;

public class FetchBestPodcast extends AsyncTask<String, Void, String> {

    AsynctaskListener mListener;

   public FetchBestPodcast(AsynctaskListener listener){
       this.mListener = listener;
   }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onPre();
    }

    @Override
    protected String doInBackground(String... urls) {

        return NetworkUtil.getPodcast(urls[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s!=null){
            mListener.onPost(s);
        }

    }

    public interface AsynctaskListener{
        void onPost(String result);
        void onPre();

    }
}
