package test.ludovick.com.hamsters.interfaces;

public interface LoadListener {
    void onStart();
    void onEnd();
    void onSuccess();
    void onFailure(Throwable throwable);
}
