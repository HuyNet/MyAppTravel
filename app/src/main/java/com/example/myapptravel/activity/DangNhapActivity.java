package com.example.myapptravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapptravel.R;
import com.example.myapptravel.retrofit.ApiDuLich;
import com.example.myapptravel.retrofit.RetrofitClient;
import com.example.myapptravel.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangnhap,email,password;
    Button button;
    ApiDuLich apiDuLich;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();
    }

    private void initControll() {
        txtdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srt_email = email.getText().toString().trim();
                String srt_pass = password.getText().toString().trim();
                if(TextUtils.isEmpty(srt_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Email! ",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(srt_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập password! ",Toast.LENGTH_SHORT).show();
                }else {

                    //save data
                    Paper.book().write("email", srt_email);
                    Paper.book().write("pass", srt_pass);
                    compositeDisposable.add(apiDuLich.dangnhap(srt_email,srt_pass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Utils.user_current=userModel.getResult().get(0);
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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
                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiDuLich= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiDuLich.class);
        txtdangnhap=findViewById(R.id.txtdangky);
        email=findViewById(R.id.emailEt);
        password=findViewById(R.id.passwordEt);
        button=findViewById(R.id.loginBtn);

        //read data
        if(Paper.book().read("email")!= null && Paper.book().read("pass")!= null){
            email.setText(Utils.user_current.getEmail());
            password.setText(Utils.user_current.getPass());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPass()!= null){
            email.setText(Utils.user_current.getEmail());
            password.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}