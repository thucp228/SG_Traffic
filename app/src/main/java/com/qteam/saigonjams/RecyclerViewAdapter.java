package com.qteam.saigonjams;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    List<ListPost> listPost;

    public RecyclerViewAdapter(List<ListPost> list) {
        this.listPost = list;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car_post, viewGroup, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ListPost post = listPost.get(position);
        holder.tvName.setText(post.getUserName());
        holder.tvPhone.setText(post.getPhoneNumber());
        holder.tvStartPos.setText(post.getStartPosition());
        holder.tvEndPos.setText(post.getEndPosition());
        holder.tvVehicleType.setText(post.getVehicleType());
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvStartPos, tvEndPos, tvVehicleType;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStartPos = itemView.findViewById(R.id.tvStartPos);
            tvEndPos = itemView.findViewById(R.id.tvEndPos);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
        }
    }
}
