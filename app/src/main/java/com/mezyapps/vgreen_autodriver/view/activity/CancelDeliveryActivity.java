package com.mezyapps.vgreen_autodriver.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.adapter.CommonDeliveryAdapter;
import com.mezyapps.vgreen_autodriver.api_common.ApiClient;
import com.mezyapps.vgreen_autodriver.api_common.ApiInterface;
import com.mezyapps.vgreen_autodriver.model.CommonDeliveryModel;
import com.mezyapps.vgreen_autodriver.model.SuccessModel;
import com.mezyapps.vgreen_autodriver.utils.NetworkUtils;
import com.mezyapps.vgreen_autodriver.utils.SharedLoginUtils;
import com.mezyapps.vgreen_autodriver.utils.ShowProgressDialog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelDeliveryActivity extends AppCompatActivity {
    private ImageView iv_back,iv_close,iv_search,iv_back_search;
    private RecyclerView recyclerViewCancelDelivery;
    private ArrayList<CommonDeliveryModel> commonDeliveryModelArrayList = new ArrayList<>();
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private String user_id;
    private CommonDeliveryAdapter commonDeliveryAdapter;
    private ImageView iv_no_data_found;
    private RelativeLayout rr_toolbar,rr_toolbar_search;
    private EditText edit_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_delivery);
        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(CancelDeliveryActivity.this);
        iv_back = findViewById(R.id.iv_back);
        iv_no_data_found = findViewById(R.id.iv_no_data_found);
        recyclerViewCancelDelivery = findViewById(R.id.recyclerViewCancelDelivery);

        iv_search=findViewById(R.id.iv_search);
        edit_search=findViewById(R.id.edit_search);
        rr_toolbar = findViewById(R.id.rr_toolbar);
        iv_back_search = findViewById(R.id.iv_back_search);
        iv_close=findViewById(R.id.iv_close);
        rr_toolbar_search = findViewById(R.id.rr_toolbar_search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CancelDeliveryActivity.this);
        recyclerViewCancelDelivery.setLayoutManager(linearLayoutManager);
        user_id = SharedLoginUtils.getUserId(CancelDeliveryActivity.this);
    }

    private void events() {
        if (NetworkUtils.isNetworkAvailable(CancelDeliveryActivity.this)) {
            callCompletedOrder();
        } else {
            NetworkUtils.isNetworkNotAvailable(CancelDeliveryActivity.this);
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_back_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_toolbar.setVisibility(View.GONE);
                rr_toolbar_search.setVisibility(View.VISIBLE);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rr_toolbar_search.setVisibility(View.GONE);
                rr_toolbar.setVisibility(View.VISIBLE);
                edit_search.setText("");
            }
        });
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(commonDeliveryModelArrayList.size()!=0) {
                    commonDeliveryAdapter.getFilter().filter(edit_search.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void callCompletedOrder() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.cancelOrder(user_id);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        String message = null, code = null;
                        if (successModule != null) {
                            message = successModule.getMsg().toUpperCase();
                            code = successModule.getCode();
                            commonDeliveryModelArrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                commonDeliveryModelArrayList = successModule.getCommonDeliveryModelArrayList();
                                if (commonDeliveryModelArrayList.size() != 0) {
                                    Collections.reverse(commonDeliveryModelArrayList);
                                    iv_no_data_found.setVisibility(View.GONE);
                                    commonDeliveryAdapter = new CommonDeliveryAdapter(CancelDeliveryActivity.this, commonDeliveryModelArrayList);
                                    recyclerViewCancelDelivery.setAdapter(commonDeliveryAdapter);
                                    commonDeliveryAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    iv_no_data_found.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Log.d("RESPONSE", "NO RESPONSE");
                                iv_no_data_found.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Log.d("RESPONSE", "NO RESPONSE");
                            iv_no_data_found.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgressDialog.dismissDialog();
                t.printStackTrace();
            }
        });
    }
}

