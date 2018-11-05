package test.ludovick.com.hamsters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import test.ludovick.com.hamsters.interfaces.OnSharedData;
import test.ludovick.com.hamsters.pojo.Hamster;

import static android.content.Context.MODE_PRIVATE;

public class GetSavedData extends AsyncTask<Void, Void, Void> {

    private Context context;
    private OnSharedData listener;

    public GetSavedData(Context context, OnSharedData listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        loadData();
        return null;
    }

    private void loadData(){
        try {
            SharedPreferences preferences = context.getSharedPreferences("KEY_GRAND", MODE_PRIVATE);
            Gson gson = new Gson();
            String data = preferences.getString("KEY_DATA", null);

            Type type = new TypeToken<List<Hamster>>() {
            }.getType();
            List<Hamster> list = gson.fromJson(data, type);

            listener.onSuccess(list);
        } catch (Throwable throwable){
            listener.onFailure(throwable);
        }
    }
}

