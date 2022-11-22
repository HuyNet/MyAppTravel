package com.example.myapptravel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.myapptravel.R;
import com.example.myapptravel.adapter.DiaDiemAdapter;
import com.example.myapptravel.adapter.DuLichMoiAdapter;
import com.example.myapptravel.model.DiaDiem;
import com.example.myapptravel.model.DuLichMoi;
import com.example.myapptravel.retrofit.ApiDuLich;
import com.example.myapptravel.retrofit.RetrofitClient;
import com.example.myapptravel.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

     Toolbar toolbar;
     ViewFlipper viewFlipper;
     RecyclerView recyclerViewTrangChu;
     NavigationView navigationView;
     ListView listViewTrangChu;
     DrawerLayout drawerLayout;
     DiaDiemAdapter diaDiemAdapter;
     List<DiaDiem> mangdiadiem;
    List<DuLichMoi> mangdulichmoi;
    DuLichMoiAdapter duLichMoiAdapter;
     CompositeDisposable compositeDisposable = new CompositeDisposable();
     ApiDuLich apiDuLich;
     NotificationBadge badge;
     FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiDuLich = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiDuLich.class);
        AnhXa();
        ActionBar();

        if(isConnected(this)){
            ActionViewFlipper();
            getDiaDiem();
            getDuLichMoi();
            getEvebtClick();
        }else {
            Toast.makeText(getApplicationContext(),"khong co Internet, vui long ket noi internet",Toast.LENGTH_LONG).show();
        }
    }

    private void getEvebtClick() {
        listViewTrangChu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent trangchu=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent vietnam=new Intent(getApplicationContext(),VietNamActivity.class);
                        vietnam.putExtra("loai",1);
                        startActivity(vietnam);
                        break;
                    case 2:
                        Intent quocte=new Intent(getApplicationContext(),VietNamActivity.class);
                        quocte.putExtra("loai",2);
                        startActivity(quocte);
                        break;
                }
            }
        });
    }

    private void getDuLichMoi() {
        compositeDisposable.add(apiDuLich.getDuLichMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        duLichMoiModel -> {
                            mangdulichmoi=duLichMoiModel.getResult();
                            duLichMoiAdapter = new DuLichMoiAdapter(getApplicationContext(),mangdulichmoi);
                            recyclerViewTrangChu.setAdapter(duLichMoiAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getDiaDiem() {
        compositeDisposable.add(apiDuLich.getDiadiem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        diaDiemModel -> {
                            if(diaDiemModel.isSuccess()){
                                mangdiadiem = diaDiemModel.getResult();
                                diaDiemAdapter = new DiaDiemAdapter(getApplicationContext(),mangdiadiem);
                                listViewTrangChu.setAdapter(diaDiemAdapter);
                            }
                        }
                ));
    }

    private void ActionViewFlipper() {
        List<String> mangQuangCao= new ArrayList<>();
        mangQuangCao.add("https://zongvietnam.com/wp-content/uploads/2022/01/VN.jpg");
        mangQuangCao.add("https://huesmiletravel.com.vn/wp-content/uploads/2020/04/21-dai-noi-hue.jpg");
        mangQuangCao.add("https://vietjet.net/includes/uploads/2015/04/du-lich-he-kham-pha-bien-dep-nhat-viet-nam.jpg");
        for (int i=0;i<mangQuangCao.size();i++){
            ImageView imageView=new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarTrangChinh);
        viewFlipper=findViewById(R.id.viewFlipper);
        recyclerViewTrangChu=findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerViewTrangChu.setLayoutManager(layoutManager);
        recyclerViewTrangChu.setHasFixedSize(true);
        listViewTrangChu=findViewById(R.id.listViewMenu);
        navigationView=findViewById(R.id.navigationView);
        badge=findViewById(R.id.menusl);
        frameLayout=findViewById(R.id.framegiohang);
        //khoi tao list
        mangdiadiem = new ArrayList<>();
        mangdulichmoi = new ArrayList<>();
        if(Utils.cards ==null){
            Utils.cards= new ArrayList<>();
        }else {
            int totalItem = 0;
            for(int i = 0;i<Utils.cards.size();i++){
                totalItem=totalItem+Utils.cards.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang=new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i = 0;i<Utils.cards.size();i++){
            totalItem=totalItem+Utils.cards.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private  boolean isConnected (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ( (wifi !=null && wifi.isConnected()) || (mobi !=null && mobi.isConnected()) ){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}