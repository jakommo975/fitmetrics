package com.example.bmi_adam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    ArrayList<HomePageItem> homePageItemArrayList;

    public ViewPagerAdapter(ArrayList<HomePageItem> homePageItemArrayList) {
        this.homePageItemArrayList = homePageItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_page_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HomePageItem viewPagerItem = homePageItemArrayList.get(position);

        holder.textView.setText(viewPagerItem.text);
        if(viewPagerItem.withButton){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(viewPagerItem.onClickListener);
        } else{
            holder.button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return homePageItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.navButton);
        }
    }

}