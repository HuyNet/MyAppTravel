package com.example.myapptravel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapptravel.R;
import com.example.myapptravel.retrofit.ApiDuLich;
import com.example.myapptravel.retrofit.RetrofitClient;
import com.example.myapptravel.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTongTien,txtsdt,txtemail;
    EditText txtdiachi;
    AppCompatButton btnthanhtoan;
    ApiDuLich apiDuLich;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    long  tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
         totalItem = 0;
        for(int i = 0;i<Utils.cards.size();i++){
            totalItem=totalItem+Utils.cards.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien= getIntent().getLongExtra("tongtien",0);
        txtTongTien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getSdt());



        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srt_diachi = txtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(srt_diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ! ",Toast.LENGTH_SHORT).show();
                }else {
                    String srt_email =Utils.user_current.getEmail();
                    String srt_sdt=Utils.user_current.getSdt();
                    int id=Utils.user_current.getId();
                    Log.d("test",new Gson().toJson(Utils.cards));
                    compositeDisposable.add(apiDuLich.createOder(srt_email,srt_sdt,String.valueOf(tongtien),id,srt_diachi,totalItem,new Gson().toJson(Utils.cards))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(),"thanh cong ",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        apiDuLich= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiDuLich.class);
        toolbar=findViewById(R.id.toolbar_thanhtoan);
        txtTongTien=findViewById(R.id.txtTongTien);
        txtsdt=findViewById(R.id.txtsdt);
        txtemail=findViewById(R.id.txtemail);
        txtdiachi=findViewById(R.id.txtdiachi);
        btnthanhtoan=findViewById(R.id.btnThanhToan);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}