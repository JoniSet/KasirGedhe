package com.example.kasirroti.Adapter;

import android.content.Context;
import android.media.MediaCodec;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kasirroti.Model.ListGambar;
import com.example.kasirroti.R;

import java.util.List;


public class SliderImageAdapter extends RecyclerView.Adapter<SliderImageAdapter.ViewHolder> {

    private List<ListGambar> listGambar;
    private ViewPager2 viewPager2;

    public SliderImageAdapter(List<ListGambar> listGambar, ViewPager2 viewPager2) {
        this.listGambar = listGambar;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view       = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImage(listGambar.get(position));
        if (position == listGambar.size()- 2){
            viewPager2.post(runnable);
        }

    }



    @Override
    public int getItemCount() {
        return listGambar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView a;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView       = itemView.findViewById(R.id.image_slider);
        }

        void setImage(ListGambar sliderItems){
            //use glide or picasso in case you get image from internet
            imageView.setImageResource(sliderItems.getGambar());
        }
    }

    private Runnable runnable  = new Runnable() {
        @Override
        public void run() {
            listGambar.addAll(listGambar);
            notifyDataSetChanged();
        }
    };
}
