package test.ludovick.com.hamsters.interfaces;

import java.util.List;

import test.ludovick.com.hamsters.pojo.Hamster;

public interface OnPresenterCallback {
    void onDataBackSuccess(List<Hamster> list);
    void onDataBackError(List<Hamster> list, Throwable throwable);
}
