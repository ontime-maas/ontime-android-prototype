package com.example.ontime;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

interface RetrofitService {

    @Headers({
            "Authorization: KakaoAK {여기에 API키를 넣어주세요}"
    })
    @GET("v2/local/search/address.json")
    Call<AddressRepo> getLocalAddress(@Query("query") String address);

}

public class NetRetrofit {

    private static NetRetrofit ourInstance = new NetRetrofit();

    public static NetRetrofit getInstance() {
        return ourInstance;
    }
    private  NetRetrofit(){}
    //https://jsonplaceholder.typicode.com/todos/1
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Retrofit daumkakaoRetrofit = new Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RetrofitService service = retrofit.create(RetrofitService.class);
    RetrofitService daumKakaoService = daumkakaoRetrofit.create(RetrofitService.class);



    public RetrofitService getService(){
        return service;
    }

    public RetrofitService getDaumKakaoService() {
        return daumKakaoService;
    }
}
