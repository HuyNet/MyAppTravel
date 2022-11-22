package com.example.myapptravel.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapptravel.EventBus.TinhTongEvent;
import com.example.myapptravel.Interface.IitemClickListenner;
import com.example.myapptravel.R;
import com.example.myapptravel.model.Card;
import com.example.myapptravel.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<Card> cardlist;

    public GioHangAdapter(Context context, List<Card> cardlist) {
        this.context = context;
        this.cardlist = cardlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Card cards=cardlist.get(position);
        holder.item_txt_ten.setText(cards.getTenDL());
        holder.item_txt_batdau.setText(cards.getBatdau());
        holder.item_txt_ketthuc.setText(cards.getKetthuc());
        holder.item_txt_soluong.setText(cards.getSoluong()+"");
        Glide.with(context).load(cards.getHinnhanh()).into(holder.item_image_dl);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_txt_giadl.setText("Giá :"+decimalFormat.format((cards.getGiave()))+"Đ");
        long gia = cards.getSoluong()*cards.getGiave();
        holder.item_txt_giadl1.setText(decimalFormat.format(gia));
        holder.setListenner(new IitemClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                Log.d("TAG","onImageClick"+pos+"..."+giatri);
                if(giatri==1){
                    if(cardlist.get(pos).getSoluong()>1){
                        int soluongmoi=cardlist.get(pos).getSoluong()-1;
                        cardlist.get(pos).setSoluong(soluongmoi);

                        holder.item_txt_soluong.setText(cardlist.get(pos).getSoluong()+"");
                        long gia = cardlist.get(pos).getSoluong()*cardlist.get(pos).getGiave();
                        holder.item_txt_giadl1.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else if(cardlist.get(pos).getSoluong()==1){
                        AlertDialog.Builder builder= new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("bạn có muốn chắc chắn xóa vé du lịch này ra khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.cards.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if(giatri==2){
                    if(cardlist.get(pos).getSoluong()<11){
                        int soluongmoi=cardlist.get(pos).getSoluong()+1;
                        cardlist.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_txt_soluong.setText(cardlist.get(pos).getSoluong()+"");
                    long gia = cardlist.get(pos).getSoluong()*cardlist.get(pos).getGiave();
                    holder.item_txt_giadl1.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item_txt_ten,item_txt_batdau,item_txt_ketthuc,item_txt_giadl,item_txt_soluong,item_txt_giadl1;
        ImageView item_image_dl,item_tang,item_giam;
        IitemClickListenner listenner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_txt_ten=itemView.findViewById(R.id.item_txt_ten);
            item_txt_batdau=itemView.findViewById(R.id.item_txt_batdau);
            item_txt_ketthuc=itemView.findViewById(R.id.item_txt_ketthuc);
            item_txt_giadl=itemView.findViewById(R.id.item_txt_giadl);
            item_txt_soluong=itemView.findViewById(R.id.item_txt_soluong);
            item_txt_giadl1=itemView.findViewById(R.id.item_txt_giadl1);
            item_image_dl=itemView.findViewById(R.id.item_image_dl);
            item_giam=itemView.findViewById(R.id.item_giam);
            item_tang=itemView.findViewById(R.id.item_tang);

            //event click
            item_tang.setOnClickListener(this);
            item_giam.setOnClickListener(this);

        }
        public void setListenner(IitemClickListenner listenner){
            this.listenner=listenner;
        }

        @Override
        public void onClick(View v) {
            if(v == item_giam){
                listenner.onImageClick(v,getAdapterPosition(),1);
            }else if(v==item_tang){
                listenner.onImageClick(v,getAdapterPosition(),2);
            }
        }
    }
}
