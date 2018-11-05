package test.ludovick.com.hamsters.interfaces;

import java.util.List;

import test.ludovick.com.hamsters.pojo.Hamster;

public interface OnHamstersDownloadListener {
    void onStart();
    void onSuccess(List<Hamster> list);
    void onFailure(Throwable t);
}
