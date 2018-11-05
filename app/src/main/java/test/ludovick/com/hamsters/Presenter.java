package test.ludovick.com.hamsters;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import java.util.List;

import test.ludovick.com.hamsters.interfaces.OnHamstersDownloadListener;
import test.ludovick.com.hamsters.interfaces.OnPresenterCallback;
import test.ludovick.com.hamsters.interfaces.OnSharedData;
import test.ludovick.com.hamsters.pojo.Hamster;

public class Presenter {

    private Context context;
    private OnPresenterCallback listener;
    private AnimationDrawable drawable;

    public Presenter(Context context, OnPresenterCallback listener, AnimationDrawable animation) {
        this.context = context;
        this.listener = listener;
        this.drawable = animation;
    }

    public void getHamstersData(String url) {
        new GetHamsters(url, new OnHamstersDownloadListener() {
            @Override
            public void onStart() {
                drawable.start();
            }

            @Override
            public void onSuccess(List<Hamster> list) {
                drawable.stop();
                listener.onDataBackSuccess(list);
                new SaveData(context, list, new OnSharedData() {
                    @Override
                    public void onSuccess(List<Hamster> list1) {
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                }).execute();
            }

            @Override
            public void onFailure(final Throwable t) {
                new GetSavedData(context, new OnSharedData() {
                    @Override
                    public void onSuccess(List<Hamster> list) {
                        drawable.stop();
                        listener.onDataBackError(list, t);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        drawable.stop();
                        listener.onDataBackError(null, t);
                    }
                }).execute();
            }
        }).execute();
    }
}
