package test.ludovick.com.hamsters;

import android.os.AsyncTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import test.ludovick.com.hamsters.interfaces.DownloadHamsters;
import test.ludovick.com.hamsters.interfaces.OnHamstersDownloadListener;
import test.ludovick.com.hamsters.pojo.Hamster;

public class GetHamsters extends AsyncTask <Void, Void, Void>{

    private String mURL;
    private String urlEnd;
    private OnHamstersDownloadListener listener;

    public GetHamsters(String url, OnHamstersDownloadListener listener) {
        this.mURL = url;
        this.urlEnd = urlEnd;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(1700);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DownloadHamsters service = retrofit.create(DownloadHamsters.class);
            service.listHamster().enqueue(new Callback<List<Hamster>>() {
                @Override
                public void onResponse(Call<List<Hamster>> call, Response<List<Hamster>> response) {
                    listener.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<List<Hamster>> call, Throwable t) {
                    listener.onFailure(t);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
