package com.mezyapps.vgreen_autodriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.model.NotificationModel;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<NotificationModel> notificationModelArrayList;

    public NotificationListAdapter(Context mContext, ArrayList<NotificationModel> notificationModelArrayList) {
        this.mContext = mContext;
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @NonNull
    @Override
    public NotificationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification_adapter, parent, false);
        return new NotificationListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.MyViewHolder holder, int position) {
        final NotificationModel notificationModel = notificationModelArrayList.get(position);

        holder.textTitle.setText(notificationModel.getNo_title());
        holder.textDescription.setText(notificationModel.getNo_description());
    }


    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textDescription, textTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.textDescription);
            textTitle = itemView.findViewById(R.id.textTitle);
        }
    }
}


