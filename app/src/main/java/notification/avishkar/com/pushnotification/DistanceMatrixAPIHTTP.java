package notification.avishkar.com.pushnotification;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rakesh on 11/26/2017.
 */

public abstract class DistanceMatrixAPIHTTP extends AsyncTask<String, Void, String> {

    public void Dist_Dura_Call_BTN() throws IOException {
        URL url = new URL("http://www.android.com/");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    private void readStream(InputStream in) {
    }
}
