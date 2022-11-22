package com.example.myapptravel.retrofit;

import com.example.myapptravel.model.DiaDiemModel;
import com.example.myapptravel.model.DuLichMoiModel;
import com.example.myapptravel.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiDuLich {
    @GET("getdiemdulich.php")
    Observable<DiaDiemModel> getDiadiem();

    @GET("getdulichmoi.php")
    Observable<DuLichMoiModel> getDuLichMoi();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<DuLichMoiModel> getDuLichVN(
        @Field("page") int page,
        @Field("loai") int loai
    );
    @POST("dangky.php")
    @FormUrlEncoded
    Observable<UserModel> dangky(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("sdt") String sdt
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("password") String password
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );


}
