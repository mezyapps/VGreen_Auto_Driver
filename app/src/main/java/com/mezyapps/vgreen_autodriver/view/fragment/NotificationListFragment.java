package com.mezyapps.vgreen_autodriver.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.adapter.NotificationListAdapter;
import com.mezyapps.vgreen_autodriver.api_common.ApiClient;
import com.mezyapps.vgreen_autodriver.api_common.ApiInterface;
import com.mezyapps.vgreen_autodriver.model.NotificationModel;
import com.mezyapps.vgreen_autodriver.model.SuccessModel;
import com.mezyapps.vgreen_autodriver.utils.NetworkUtils;
import com.mezyapps.vgreen_autodriver.utils.SharedLoginUtils;
import com.mezyapps.vgreen_autodriver.utils.ShowProgressDialog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerViewNotification;
    private ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    private NotificationListAdapter notificationListAdapter;
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private ImageView iv_no_data_found;
    private String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
        mContext = getActivity();
        find_View_IDs(view);
        events();
        return view;
    }

    private void find_View_IDs(View view) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(mContext);
        recyclerViewNotification = view.findViewById(R.id.recyclerViewNotification);
        iv_no_data_found = view.findViewById(R.id.iv_no_data_found);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerViewNotification.setLayoutManager(linearLayoutManager);
        user_id= SharedLoginUtils.getUserId(mContext);
    }

    private void events() {
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            callNotificationList();
        } else {
            NetworkUtils.isNetworkNotAvailable(mContext);
        }
    }

    private void callNotificationList() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.notificationList(user_id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        notificationModelArrayList.clear();
                        String message = null, code = null, folder = null;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder = successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {

                                notificationModelArrayList = successModule.getNotificationModelArrayList();
                                if (notificationModelArrayList.size() != 0) {
                                    iv_no_data_found.setVisibility(View.GONE);
                                    Collections.reverse(notificationModelArrayList);
                                    notificationListAdapter = new NotificationListAdapter(mContext, notificationModelArrayList);
                                    recyclerViewNotification.setAdapter(notificationListAdapter);
                                    notificationListAdapter.notifyDataSetChanged();
                                } else {
                                    iv_no_data_found.setVisibility(View.VISIBLE);
                                    notificationListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                iv_no_data_found.setVisibility(View.VISIBLE);
                                notificationListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(mContext, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgressDialog.dismissDialog();
            }
        });

    }
}
