package com.mezyapps.vgreen_autodriver.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.utils.SharedLoginUtils;
import com.mezyapps.vgreen_autodriver.view.activity.CancelDeliveryActivity;
import com.mezyapps.vgreen_autodriver.view.activity.CompleteDeliveryActivity;
import com.mezyapps.vgreen_autodriver.view.activity.PendingDeliveryActivity;
import com.mezyapps.vgreen_autodriver.view.activity.ShippedOrderActivity;

public class HomeFragment extends Fragment {
    private Context mContext;
    private CardView cardView_pending_delivery,cardViewCompleteDelivery,cardView_cancel_delivery,cardViewShippedDelivery;
    private TextView textName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mContext=getActivity();
        find_View_IDS(view);
        events();
        return view;
    }

    private void find_View_IDS(View view) {
        cardView_pending_delivery=view.findViewById(R.id.cardView_pending_delivery);
        cardViewCompleteDelivery=view.findViewById(R.id.cardViewCompleteDelivery);
        cardView_cancel_delivery=view.findViewById(R.id.cardView_cancel_delivery);
        cardViewShippedDelivery=view.findViewById(R.id.cardViewShippedDelivery);
        textName=view.findViewById(R.id.textName);
        String name= SharedLoginUtils.getUserName(mContext);
        textName.setText("Hello "+name);
    }
    private void events() {
        cardView_pending_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PendingDeliveryActivity.class));
            }
        });
        cardViewCompleteDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CompleteDeliveryActivity.class));
            }
        });
        cardView_cancel_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CancelDeliveryActivity.class));
            }
        });
        cardViewShippedDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ShippedOrderActivity.class));
            }
        });
    }

}
