package test.ludovick.com.hamsters;

import android.os.AsyncTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import test.ludovick.com.hamsters.interfaces.LoadListener;

public class YamlLoader extends AsyncTask<Void, Void, Void> {

    private String url;
    private File file;
    private LoadListener loadListener;
    private Throwable throwable;

    public YamlLoader(String url, File file, LoadListener listener) {
        this.url = url;
        this.file = file;
        this.loadListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadListener.onStart();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            FileUtils.copyURLToFile(new URL(url), file);
        } catch (IOException e) {
            throwable = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        loadListener.onEnd();
        if (throwable != null) {
            loadListener.onFailure(throwable);
        } else {
            loadListener.onSuccess();
        }
    }
}
