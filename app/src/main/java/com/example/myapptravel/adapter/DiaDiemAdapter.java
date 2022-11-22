package com.example.myapptravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapptravel.R;
import com.example.myapptravel.model.DiaDiem;

import java.util.List;

public class DiaDiemAdapter extends BaseAdapter {
    List<DiaDiem> array;
    Context context;

    public DiaDiemAdapter(Context context,List<DiaDiem> array ) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder{
        TextView texttendiadiem;
        ImageView imagehinhanh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_diadiem,null);
            viewHolder.texttendiadiem=convertView.findViewById(R.id.item_tendiadiem);
            viewHolder.imagehinhanh=convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.texttendiadiem.setText(array.get(position).getTendiadiem());
        Glide.with(context).load(array.get(position).getHinhanh()).into(viewHolder.imagehinhanh);
        return convertView;
    }
}
