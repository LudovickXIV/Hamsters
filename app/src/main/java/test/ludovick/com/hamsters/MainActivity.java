package test.ludovick.com.hamsters;

import android.graphics.drawable.AnimationDrawable;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import test.ludovick.com.hamsters.interfaces.LoadListener;
import test.ludovick.com.hamsters.interfaces.OnHamsterDataPass;
import test.ludovick.com.hamsters.interfaces.OnPresenterCallback;
import test.ludovick.com.hamsters.pojo.Hamster;

public class MainActivity extends AppCompatActivity implements OnPresenterCallback, OnHamsterDataPass {

    public static final String HAMSTER_KEY = "hamsterKey";

    private Presenter presenter;
    private Toolbar mToolbar;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ImageView hamsterProgress;
    private AnimationDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hamsterProgress = findViewById(R.id.imgv);
        drawable = (AnimationDrawable)hamsterProgress.getBackground();

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);

        manager = getSupportFragmentManager();

        presenter = new Presenter(this,this, drawable);
        new YamlLoader(this.getResources().getString(R.string.yaml_url),
                new File(getCacheDir() + File.separator + "hamsters-api.yaml"),
                new LoadListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onEnd() {
                    }
                    @Override
                    public void onSuccess() {
                        try {
                            Yaml yaml = new Yaml();
                            File file = new File(getCacheDir() + File.separator + "hamsters-api.yaml");
                            InputStream inputStream = new FileInputStream(file);
                            Map<String,Object> model = (Map<String, Object>) yaml.load(inputStream);

                            String schemes = model.get("schemes").toString().replace('[', ' ').replace(']', ' ').trim();
                            String host = model.get("host").toString();
                            String basePath = model.get("basePath").toString();
                            String path = model.get("paths").toString();
                            path = path.substring(path.indexOf("{") + 1, path.indexOf("=")) + "/";
                            String urlbase = schemes + "://" + host + basePath + path;

                            presenter.getHamstersData(urlbase);
                        } catch (IOException e){
                        }
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                    }
                }).execute();
    }

    @Override
    public void onDataBackSuccess(List<Hamster> list) {
        hamsterProgress.setVisibility(View.GONE);
        listHamsterFragment(list);
    }

    @Override
    public void onDataBackError(List<Hamster> list, Throwable throwable) {
        hamsterProgress.setVisibility(View.INVISIBLE);
        if (list != null) {
            Log.d("TAG", "OK");
            listHamsterFragment(list);
        } else {
            Hamster hamsterError = new Hamster();
            hamsterError.setTitle("Error, try again later");
            hamsterError.setDescription(throwable.getMessage());
            hamsterError.setImage("null");
            fullDescriptionHamsterFragment(hamsterError);
            Log.d("TAG", "NonOk");
        }
    }


    private void fullDescriptionHamsterFragment(Hamster hamster){
        Bundle bundle = new Bundle();
        bundle.putParcelable(HAMSTER_KEY, hamster);

        HamsterFragment fragment = new HamsterFragment();
        fragment.setArguments(bundle);
        transaction = manager.beginTransaction();
        transaction
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.mFragment, fragment)
                .addToBackStack("listFragment")
                .commit();
    }

    private void listHamsterFragment(List<Hamster> list){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(HAMSTER_KEY, (ArrayList<? extends Parcelable>) list);

        HamstersListFragment fragment = new HamstersListFragment();
        fragment.setArguments(bundle);
        transaction = manager.beginTransaction();
        transaction
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.mFragment, fragment, "listFragment")
                .commit();
    }

    @Override
    public void onHamsterPass(Hamster hamster) {
        fullDescriptionHamsterFragment(hamster);
    }

    @Override
    public void onBackPressed() {
        if(manager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            manager.popBackStack();
        }
    }
}
