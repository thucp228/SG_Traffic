package com.qteam.saigonjams.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qteam.saigonjams.activity.MainActivity;
import com.qteam.saigonjams.model.Sharing;
import com.qteam.saigonjams.R;

import java.util.List;

public class SharingRecyclerViewAdapter extends RecyclerView.Adapter<SharingRecyclerViewAdapter.RecyclerViewHolder> {
    private List<Sharing> sharingList;

    public SharingRecyclerViewAdapter(List<Sharing> list) {
        this.sharingList = list;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvPhone, tvStartPos, tvEndPos, tvVehicleType, tvDate;
        Button btnCall;

        private RecyclerViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone_number);
            tvStartPos = itemView.findViewById(R.id.tv_start_position);
            tvEndPos = itemView.findViewById(R.id.tv_end_position);
            tvVehicleType = itemView.findViewById(R.id.tv_transport);
            tvDate = itemView.findViewById(R.id.tv_shared_time);
            btnCall = itemView.findViewById(R.id.btn_call);

            btnCall.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == btnCall.getId())
                call();
        }

        private void call() {
            String number = "tel:" + tvPhone.getText().toString().trim();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(number));

            if (ActivityCompat.checkSelfPermission(MainActivity.context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                MainActivity.context.startActivity(callIntent);
        }
    }

    @Override
    public @NonNull RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sharing, viewGroup, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        Sharing post = sharingList.get(position);
        holder.tvName.setText(post.getUserName());
        holder.tvPhone.setText(post.getPhoneNumber());
        holder.tvStartPos.setText(post.getStartPosition());
        holder.tvEndPos.setText(post.getEndPosition());
        holder.tvVehicleType.setText(post.getVehicleType());
        holder.tvDate.setText(post.getDate());

    }

    @Override
    public int getItemCount() {
        return sharingList.size();
    }

    public void filterList(List<Sharing> filteredList) {
        sharingList = filteredList;
        notifyDataSetChanged();
    }
}
