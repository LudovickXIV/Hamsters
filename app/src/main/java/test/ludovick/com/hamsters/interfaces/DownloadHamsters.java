package test.ludovick.com.hamsters.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Query;
import test.ludovick.com.hamsters.pojo.Hamster;

public interface DownloadHamsters {

    @GET("/porn/test3/")
    Call<List<Hamster>> listHamster();
}
