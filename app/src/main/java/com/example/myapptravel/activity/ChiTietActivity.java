package com.example.myapptravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapptravel.EventBus.TinhTongEvent;
import com.example.myapptravel.R;
import com.example.myapptravel.model.Card;
import com.example.myapptravel.model.DuLichMoi;
import com.example.myapptravel.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tenDL,batdauDL,ketthucDL,giaDL,chitietDL;
    Button btnthem;
    ImageView  imageHinhanh;
    Spinner spinner;
    Toolbar toolbar;
    DuLichMoi duLichMoi;
    NotificationBadge notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themCrad();
            }
        });
    }

    private void themCrad() {
        if(Utils.cards.size()>0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i=0;i<Utils.cards.size();i++){
                if(Utils.cards.get(i).getIdDL() == duLichMoi.getId()){
                    Utils.cards.get(i).setSoluong(soluong+Utils.cards.get(i).getSoluong());
                    long giave=Long.parseLong(duLichMoi.getGiatien())*Utils.cards.get(i).getSoluong();
                    Utils.cards.get(i).setGiave(giave);
                    flag =true;
                }
            }
            if(flag==false){
                long giave=Long.parseLong(duLichMoi.getGiatien())*soluong;
                Card card=new Card();
                card.setGiave(giave);
                card.setSoluong(soluong);
                card.setIdDL(duLichMoi.getId());
                card.setTenDL(duLichMoi.getTendulich());
                card.setBatdau(duLichMoi.getBatdau());
                card.setKetthuc(duLichMoi.getKetthuc());
                card.setHinnhanh(duLichMoi.getHinhanh());
                Utils.cards.add(card);
            }

        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long giave =Long.parseLong(duLichMoi.getGiatien())+soluong;
            Card card=new Card();
            card.setGiave(giave);
            card.setSoluong(soluong);
            card.setIdDL(duLichMoi.getId());
            card.setTenDL(duLichMoi.getTendulich());
            card.setBatdau(duLichMoi.getBatdau());
            card.setKetthuc(duLichMoi.getKetthuc());
            card.setHinnhanh(duLichMoi.getHinhanh());
            Utils.cards.add(card);
        }
        int totalItem = 0;
        for(int i = 0;i<Utils.cards.size();i++){
            totalItem=totalItem+Utils.cards.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(Utils.cards.size()));
    }

    private void initData() {
        duLichMoi=(DuLichMoi) getIntent().getSerializableExtra("chitiet");
        tenDL.setText(duLichMoi.getTendulich());
        batdauDL.setText(" Ngày bắt đầu :"+duLichMoi.getBatdau());
        ketthucDL.setText(" Ngày kết thúc :"+duLichMoi.getKetthuc());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giaDL.setText("Giá :"+decimalFormat.format(Double.parseDouble(duLichMoi.getGiatien()))+"Đ");
        chitietDL.setText(duLichMoi.getMota());
        Glide.with(getApplicationContext()).load(duLichMoi.getHinhanh()).into(imageHinhanh);
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
        
    }

    private void initView() {
        tenDL = findViewById(R.id.txtten);
        batdauDL=findViewById(R.id.txtbatdau);
        ketthucDL=findViewById(R.id.txtketthuc);
        btnthem=findViewById(R.id.btndatve);
        giaDL=findViewById(R.id.txtgia);
        chitietDL=findViewById(R.id.txtchitiet);
        imageHinhanh=findViewById(R.id.imageChiTiet);
        spinner=findViewById(R.id.spinner);
        toolbar=findViewById(R.id.toolbar_chitiet);
        notificationBadge=findViewById(R.id.badge);
        FrameLayout frameLayoutgiohang=findViewById(R.id.framegiohang);
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang=new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(Utils.cards!=null){
            int totalItem = 0;
            for(int i = 0;i<Utils.cards.size();i++){
                totalItem=totalItem+Utils.cards.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.cards!=null){
            int totalItem = 0;
            for(int i = 0;i<Utils.cards.size();i++){
                totalItem=totalItem+Utils.cards.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }
}