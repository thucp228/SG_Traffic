package com.qteam.saigonjams.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qteam.saigonjams.model.CarPost;
import com.qteam.saigonjams.R;

import java.util.List;

public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.RecyclerViewHolder> {

    private List<CarPost> carPostList;

    public CarRecyclerViewAdapter(List<CarPost> list) {
        this.carPostList = list;
    }

    @Override
    public @NonNull RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car_post, viewGroup, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        CarPost post = carPostList.get(position);
        holder.tvName.setText(post.getUserName());
        holder.tvPhone.setText(post.getPhoneNumber());
        holder.tvStartPos.setText(post.getStartPosition());
        holder.tvEndPos.setText(post.getEndPosition());
        holder.tvVehicleType.setText(post.getVehicleType());
        holder.tvDate.setText(post.getDate());
    }

    @Override
    public int getItemCount() {
        return carPostList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvStartPos, tvEndPos, tvVehicleType, tvDate;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStartPos = itemView.findViewById(R.id.tvStartPos);
            tvEndPos = itemView.findViewById(R.id.tvEndPos);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            tvDate = itemView.findViewById(R.id.tvCarDate);
        }
    }
}
