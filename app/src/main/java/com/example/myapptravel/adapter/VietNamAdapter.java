package com.example.myapptravel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapptravel.Interface.ItemClickListener;
import com.example.myapptravel.R;
import com.example.myapptravel.activity.ChiTietActivity;
import com.example.myapptravel.model.DuLichMoi;

import java.text.DecimalFormat;
import java.util.List;

public class VietNamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<DuLichMoi> array;
    private static final int VIEW_TYPE_DATA=0;
    private static final int VIEW_TYPE_LOADING=1;

    public VietNamAdapter(Context context, List<DuLichMoi> array) {
        this.context = context;
        this.array = array;
    }


    public class LoadingViewHolde extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolde(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vietnam,parent,false);
            return new MyViewHodel(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolde(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHodel){
            MyViewHodel myViewHodel=(MyViewHodel) holder;
            DuLichMoi duLichMoi=array.get(position);
            ((MyViewHodel) holder).vnten.setText(duLichMoi.getTendulich().trim());
            ((MyViewHodel) holder).vnbatdau.setText(" Ngày bắt đầu :"+duLichMoi.getBatdau());
            ((MyViewHodel) holder).vnketthuc.setText(" Ngày kết thúc :"+duLichMoi.getKetthuc());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            ((MyViewHodel) holder).vngia.setText("Giá :"+decimalFormat.format(Double.parseDouble(duLichMoi.getGiatien()))+"Đ");
            ((MyViewHodel) holder).vngioithieu.setText(" Giới thiệu :"+duLichMoi.getMota());
            ((MyViewHodel) holder).vnID.setText(" ID :"+duLichMoi.getId());
            Glide.with(context).load(duLichMoi.getHinhanh()).into(((MyViewHodel) holder).imagehinhanh);
            myViewHodel.setItemClickListener(new ItemClickListener() {
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
        }else {
            LoadingViewHolde loadingViewHolde=(LoadingViewHolde) holder;
            loadingViewHolde.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHodel extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vnten,vnbatdau,vnketthuc,vngia,vngioithieu,vnID;
        ImageView imagehinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHodel(@NonNull View itemView) {
            super(itemView);
            vnten = itemView.findViewById(R.id.itemVN_ten);
            vnID = itemView.findViewById(R.id.itemVN_id);
            vnbatdau = itemView.findViewById(R.id.itemVN_batdau);
            vnketthuc = itemView.findViewById(R.id.itemVN_ketthuc);
            vngia = itemView.findViewById(R.id.itemVN_gia);
            vngioithieu = itemView.findViewById(R.id.itemVN_gioithieu);
            imagehinhanh = itemView.findViewById(R.id.itemVN_image);
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
