package com.example.myapptravel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapptravel.Interface.ItemClickListener;
import com.example.myapptravel.R;
import com.example.myapptravel.activity.ChiTietActivity;
import com.example.myapptravel.model.DuLichMoi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
public class DuLichMoiAdapter extends RecyclerView.Adapter<DuLichMoiAdapter.MyViewHolder> {
    Context context;
    List<DuLichMoi> array;

    public DuLichMoiAdapter(Context context, List<DuLichMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dulich_moi,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DuLichMoi duLichMoi=array.get(position);
        holder.txtTen.setText(duLichMoi.getTendulich());
        holder.txtBatDau.setText(" Ngày bắt đầu :"+duLichMoi.getBatdau());
        holder.txtKetThuc.setText(" Ngày kết thúc :"+duLichMoi.getKetthuc());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá :"+decimalFormat.format(Double.parseDouble(duLichMoi.getGiatien()))+"Đ");
        Glide.with(context).load(duLichMoi.getHinhanh()).into(holder.imageHinhAnh);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet",duLichMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTen,txtBatDau,txtKetThuc,txtGia;
        ImageView imageHinhAnh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.itemDl_ten);
            txtBatDau = itemView.findViewById(R.id.itemDl_batdau);
            txtKetThuc = itemView.findViewById(R.id.itemDl_ketthuc);
            txtGia = itemView.findViewById(R.id.itemDl_gia);
            imageHinhAnh = itemView.findViewById(R.id.itemDl_image);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
