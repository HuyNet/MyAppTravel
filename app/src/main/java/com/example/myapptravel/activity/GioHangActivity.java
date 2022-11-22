package com.example.myapptravel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapptravel.EventBus.TinhTongEvent;
import com.example.myapptravel.R;
import com.example.myapptravel.adapter.GioHangAdapter;
import com.example.myapptravel.model.Card;
import com.example.myapptravel.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,txttongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter cardAdapter;
//    List<Card> cardList;
    long tongtienve = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhTongTien();

    }

    private void tinhTongTien() {
         tongtienve = 0;
        for(int i=0;i<Utils.cards.size();i++){
            tongtienve=tongtienve+(Utils.cards.get(i).getGiave()*Utils.cards.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txttongtien.setText(decimalFormat.format(tongtienve));


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
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.cards.size()==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else {
            cardAdapter=new GioHangAdapter(getApplicationContext(),Utils.cards);
            recyclerView.setAdapter(cardAdapter);
        }

        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",tongtienve);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        giohangtrong=findViewById(R.id.txtgiohangtrong);
        toolbar=findViewById(R.id.toolbar_giohang);
        recyclerView=findViewById(R.id.recyclerViewGioHang);
        btnmuahang=findViewById(R.id.btnmuahang);
        txttongtien=findViewById(R.id.txttongtien);
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTong(TinhTongEvent event){
        if(event!=null){
            tinhTongTien();
        }
    }
}