package com.mezyapps.vgreen_autodriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.model.CommonDeliveryModel;
import com.mezyapps.vgreen_autodriver.view.activity.CommonProductActivity;

import java.util.ArrayList;

public class CommonDeliveryAdapter extends RecyclerView.Adapter<CommonDeliveryAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<CommonDeliveryModel> commonDeliveryModelArrayList;
    private ArrayList<CommonDeliveryModel> arrayListFiltered;

    public CommonDeliveryAdapter(Context mContext, ArrayList<CommonDeliveryModel> commonDeliveryModelArrayList) {
        this.mContext = mContext;
        this.commonDeliveryModelArrayList = commonDeliveryModelArrayList;
        this.arrayListFiltered = commonDeliveryModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pending_delivrey_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final CommonDeliveryModel commonDeliveryModel = commonDeliveryModelArrayList.get(position);

        String order_no = commonDeliveryModel.getOrder_no();
        String order_date = commonDeliveryModel.getDate();
        String total_price = commonDeliveryModel.getTotal_price();
        String status = commonDeliveryModel.getStatus();
        String customer_name = commonDeliveryModel.getCustomer_name();
        String address = commonDeliveryModel.getDelivery_address();
        String mobile_no = commonDeliveryModel.getMobile_no();
        holder.textOrderNo.setText(order_no);
        holder.textOrderDate.setText(order_date);
        holder.textTotalPrice.setText(total_price);
        holder.textPartyName.setText(customer_name);
        holder.textAddress.setText(address);
        holder.textView_mobileNo.setText(mobile_no);
        holder.textStatus.setText(status);


        if (status.equalsIgnoreCase("cancelled")) {
            holder.textStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        } else if (status.equalsIgnoreCase("delivered")) {
            holder.textStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            holder.textStatus.setTextColor(mContext.getResources().getColor(R.color.blue));
        }
        holder.iv_phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ commonDeliveryModel.getMobile_no()));
                mContext.startActivity(intent);
            }
        });

        holder.cardView_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommonProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ORDER_HD", (Parcelable) commonDeliveryModelArrayList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commonDeliveryModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textOrderNo, textOrderDate, textTotalPrice, textStatus, textPartyName,
                textAddress,textView_mobileNo;
        private CardView cardView_order_history;
        private ImageView iv_phone_call;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderNo = itemView.findViewById(R.id.textOrderNo);
            textOrderDate = itemView.findViewById(R.id.textOrderDate);
            textTotalPrice = itemView.findViewById(R.id.textTotalPrice);
            textStatus = itemView.findViewById(R.id.textStatus);
            cardView_order_history = itemView.findViewById(R.id.cardView_order_history);
            textPartyName = itemView.findViewById(R.id.textPartyName);
            textAddress = itemView.findViewById(R.id.textAddress);
            textView_mobileNo = itemView.findViewById(R.id.textView_mobileNo);
            iv_phone_call = itemView.findViewById(R.id.iv_phone_call);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().replaceAll("\\s", "").toLowerCase().trim();
                if (charString.isEmpty() || charSequence.equals("")) {
                    commonDeliveryModelArrayList = arrayListFiltered;
                } else {
                    ArrayList<CommonDeliveryModel> filteredList = new ArrayList<>();
                    for (int i = 0; i < commonDeliveryModelArrayList.size(); i++) {
                        String order_no = commonDeliveryModelArrayList.get(i).getOrder_no().replaceAll("\\s", "").toLowerCase().trim();
                        String customer_name = commonDeliveryModelArrayList.get(i).getCustomer_name().replaceAll("\\s", "").toLowerCase().trim();
                        if ((order_no.contains(charString)||customer_name.contains(charString))) {
                            filteredList.add(commonDeliveryModelArrayList.get(i));
                        }
                    }
                    if (filteredList.size() > 0) {
                        commonDeliveryModelArrayList = filteredList;
                    } else {
                        commonDeliveryModelArrayList = arrayListFiltered;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = commonDeliveryModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                commonDeliveryModelArrayList = (ArrayList<CommonDeliveryModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
