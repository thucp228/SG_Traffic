package com.qteam.saigonjams.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qteam.saigonjams.model.AlertPost;
import com.qteam.saigonjams.R;

import java.util.List;

public class AlertRecyclerViewAdapter extends RecyclerView.Adapter<AlertRecyclerViewAdapter.RecyclerViewHolder> {

    private Context context;
    private List<AlertPost> alertPostList;

    public AlertRecyclerViewAdapter(List<AlertPost> alertPostList) {
        this.alertPostList = alertPostList;
    }

    public AlertRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alert_post, viewGroup, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        AlertPost post = alertPostList.get(position);

        holder.tvPosition.setText(post.getPosition());
        holder.tvStatus.setText(post.getStatus());
        holder.tvDate.setText(post.getDate());

        Glide.with(holder.imageView.getContext()).load(post.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return alertPostList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvStatus, tvDate;
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            tvPosition = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvAlertDate);
            imageView = itemView.findViewById(R.id.imgViewPost);
        }
    }
}
