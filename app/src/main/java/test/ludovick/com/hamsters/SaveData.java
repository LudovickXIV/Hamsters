package test.ludovick.com.hamsters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.util.List;

import test.ludovick.com.hamsters.interfaces.OnSharedData;
import test.ludovick.com.hamsters.pojo.Hamster;

import static android.content.Context.MODE_PRIVATE;

public class SaveData extends AsyncTask<Void, Void, Void> {

    private Context context;
    private List<Hamster> list;
    private OnSharedData listener;

    public SaveData(Context context, List<Hamster> list, OnSharedData listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        saveData(list);
        return null;
    }

    private void saveData(List<Hamster> list){
        try {
            SharedPreferences preferences = context.getSharedPreferences("KEY_GRAND", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String data = gson.toJson(list);

            editor.putString("KEY_DATA", data);

            editor.apply();

            listener.onSuccess(list);
        } catch (Throwable throwable){
            listener.onFailure(throwable);
        }
    }
}
