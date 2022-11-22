package com.example.myapptravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapptravel.R;
import com.example.myapptravel.retrofit.ApiDuLich;
import com.example.myapptravel.retrofit.RetrofitClient;
import com.example.myapptravel.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText emailEt,passwordEt,passwordEt1,sdtet,UnameEt;
    Button dangkyBtn;
    ApiDuLich apiDuLich;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
    }

    private void initView() {
        apiDuLich= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiDuLich.class);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        passwordEt1 = findViewById(R.id.passwordEt1);
        dangkyBtn = findViewById(R.id.dangkyBtn);
        sdtet = findViewById(R.id.sdtet);
        UnameEt = findViewById(R.id.UnameEt);
    }
    private void initControll() {
        dangkyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangky();
            }
        });
    }

    private void dangky() {
        String srt_email = emailEt.getText().toString().trim();
        String srt_pass = passwordEt.getText().toString().trim();
        String srt_repass = passwordEt1.getText().toString().trim();
        String srt_sdt = sdtet.getText().toString().trim();
        String srt_username = UnameEt.getText().toString().trim();
        if(TextUtils.isEmpty(srt_email)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Email! ",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(srt_username)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập User Name! ",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(srt_pass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Password! ",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(srt_repass)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Repassword! ",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(srt_sdt)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập số điện thoại! ",Toast.LENGTH_SHORT).show();
        }else {
            if(srt_pass.equals(srt_repass)){
                compositeDisposable.add(apiDuLich.dangky(srt_email,srt_pass,srt_username,srt_sdt)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        Utils.user_current.setEmail(srt_email);
                                        Utils.user_current.setPass(srt_pass);
                                        Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(),"Pass chưa khớp! ",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}