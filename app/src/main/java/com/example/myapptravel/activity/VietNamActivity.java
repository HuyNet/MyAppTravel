package com.example.myapptravel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.myapptravel.R;
import com.example.myapptravel.adapter.VietNamAdapter;
import com.example.myapptravel.model.DuLichMoi;
import com.example.myapptravel.retrofit.ApiDuLich;
import com.example.myapptravel.retrofit.RetrofitClient;
import com.example.myapptravel.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VietNamActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiDuLich apiDuLich;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page =1;
    int loai;
    VietNamAdapter vietNamAdapter;
    List<DuLichMoi> duLichMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler=new Handler();
    boolean isLoanding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viet_nam);
        apiDuLich= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiDuLich.class);
        loai = getIntent().getIntExtra("loai",1);
        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoading();
    }

    private void addEventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoanding == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==duLichMoiList.size()-1){
                        isLoanding=true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                duLichMoiList.add(null);
                vietNamAdapter.notifyItemInserted(duLichMoiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                duLichMoiList.remove(duLichMoiList.size()-1);
                vietNamAdapter.notifyItemRemoved(duLichMoiList.size());
                page=page+1;
                getData(page);
                vietNamAdapter.notifyDataSetChanged();
                isLoanding=false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiDuLich.getDuLichVN(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        duLichMoiModel -> {
                            if(duLichMoiModel.isSuccess()){
                                if(vietNamAdapter==null){
                                    duLichMoiList=duLichMoiModel.getResult();
                                    vietNamAdapter=new VietNamAdapter(getApplicationContext(),duLichMoiList);
                                    recyclerView.setAdapter(vietNamAdapter);
                                }else {
                                    int Vitri=duLichMoiList.size()-1;
                                    int soluongadd=duLichMoiModel.getResult().size();
                                    for (int i=0;i<soluongadd;i++){
                                        duLichMoiList.add(duLichMoiModel.getResult().get(i));
                                    }
                                    vietNamAdapter.notifyItemRangeChanged(Vitri,soluongadd);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"het du lieu",Toast.LENGTH_LONG).show();
                                isLoanding=true;
                            }


                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void Anhxa() {
        toolbar=findViewById(R.id.toolbar_VN);
        recyclerView=findViewById(R.id.recycler_view_VN);
//        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        duLichMoiList=new ArrayList<>();
    }
}