package com.qteam.saigonjams.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qteam.saigonjams.model.Notification;
import com.qteam.saigonjams.R;

import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.RecyclerViewHolder> {

    private List<Notification> notificationList;

    public NotificationRecyclerViewAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public @NonNull RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications, viewGroup, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Notification post = notificationList.get(position);

        holder.tvPosition.setText(post.getPosition());
        holder.tvStatus.setText(post.getStatus());
        holder.tvDate.setText(post.getDate());

        switch (post.getStatus()) {
            case "Thông thoáng":
                holder.tvStatus.setTextColor(Color.parseColor("#32CD32"));
                break;
            case "Đông xe":
                holder.tvStatus.setTextColor(Color.parseColor("#FFA500"));
                break;
            case "Ùn tắc":
                holder.tvStatus.setTextColor(Color.parseColor("#FF4500"));
                break;
            case "Ngập nước":
                holder.tvStatus.setTextColor(Color.parseColor("#8B4513"));
                break;
            case "Tai nạn":
                holder.tvStatus.setTextColor(Color.parseColor("#FF0000"));
                break;
        }

        Glide.with(holder.imageView.getContext()).load(post.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvStatus, tvDate;
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tvPosition = itemView.findViewById(R.id.tv_location);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDate = itemView.findViewById(R.id.tv_posted_time);
            imageView = itemView.findViewById(R.id.image_view_notification);
        }
    }
}
