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

public class CompleteDeliveryActivity extends AppCompatActivity {

    private ImageView iv_back;
    private RecyclerView recyclerViewCompletedDelivery;
    private ArrayList<CommonDeliveryModel> commonDeliveryModelArrayList = new ArrayList<>();
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private String user_id;
    private CommonDeliveryAdapter commonDeliveryAdapter;
    private ImageView iv_no_data_found,iv_close,iv_search,iv_back_search;
    private RelativeLayout rr_toolbar,rr_toolbar_search;
    private EditText edit_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_delivery);
        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(CompleteDeliveryActivity.this);
        iv_back = findViewById(R.id.iv_back);
        recyclerViewCompletedDelivery = findViewById(R.id.recyclerViewCompletedDelivery);
        iv_no_data_found = findViewById(R.id.iv_no_data_found);

        iv_search=findViewById(R.id.iv_search);
        edit_search=findViewById(R.id.edit_search);
        rr_toolbar = findViewById(R.id.rr_toolbar);
        iv_back_search = findViewById(R.id.iv_back_search);
        iv_close=findViewById(R.id.iv_close);
        rr_toolbar_search = findViewById(R.id.rr_toolbar_search);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CompleteDeliveryActivity.this);
        recyclerViewCompletedDelivery.setLayoutManager(linearLayoutManager);
        user_id = SharedLoginUtils.getUserId(CompleteDeliveryActivity.this);
    }

    private void events() {
        if (NetworkUtils.isNetworkAvailable(CompleteDeliveryActivity.this)) {
            callCompletedOrder();
        } else {
            NetworkUtils.isNetworkNotAvailable(CompleteDeliveryActivity.this);
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
        Call<SuccessModel> call = apiInterface.completedOrder(user_id);
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
                                    commonDeliveryAdapter = new CommonDeliveryAdapter(CompleteDeliveryActivity.this, commonDeliveryModelArrayList);
                                    recyclerViewCompletedDelivery.setAdapter(commonDeliveryAdapter);
                                    commonDeliveryAdapter.notifyDataSetChanged();
                                } else {
                                    iv_no_data_found.setVisibility(View.VISIBLE);
                                }
                            } else {
                                iv_no_data_found.setVisibility(View.VISIBLE);
                                Log.d("RESPONSE", "User Not Registered");
                            }
                        } else {
                            iv_no_data_found.setVisibility(View.VISIBLE);
                            Log.d("RESPONSE", "NO RESPONSE");
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
