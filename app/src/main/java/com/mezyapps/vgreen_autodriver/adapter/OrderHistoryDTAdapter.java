package com.mezyapps.vgreen_autodriver.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.api_common.BaseApi;
import com.mezyapps.vgreen_autodriver.model.OrderHistoryDTModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderHistoryDTAdapter extends RecyclerView.Adapter<OrderHistoryDTAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList;
    private String folder;

    public OrderHistoryDTAdapter(Context mContext, ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList, String folder) {
        this.mContext = mContext;
        this.orderHistoryDTModelArrayList = orderHistoryDTModelArrayList;
        this.folder = folder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_history_product_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderHistoryDTModel orderHistoryDTModel = orderHistoryDTModelArrayList.get(position);

        holder.textProductName.setText(orderHistoryDTModel.getProduct_name());
        String unit = orderHistoryDTModel.getUnit() + " " + orderHistoryDTModel.getUnit_name();
        String rate = "Rs " + orderHistoryDTModel.getTotal_price();
        String mrp = "MRP " + orderHistoryDTModel.getTotal_mrp();
        String qty = "QTY " + orderHistoryDTModel.getQty();
        holder.textUnitName.setText(unit);
        holder.textMrp.setText(mrp);
        holder.textPrice.setText(rate);
        holder.textQty.setText(qty);
        holder.textMrp.setPaintFlags(holder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        final String imagePath = BaseApi.BASE_URL + folder + orderHistoryDTModel.getProd_image();
        Picasso.with(mContext).load(imagePath).into(holder.iv_product_image);

    }

    @Override
    public int getItemCount() {
        return orderHistoryDTModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textProductName, textUnitName, textMrp, textPrice, textQty;
        private ImageView iv_product_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.textProductName);
            textUnitName = itemView.findViewById(R.id.textUnitName);
            textMrp = itemView.findViewById(R.id.textMrp);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQty = itemView.findViewById(R.id.textQty);
            iv_product_image = itemView.findViewById(R.id.iv_product_image);
        }
    }
}
