package com.mezyapps.vgreen_autodriver.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.adapter.OrderHistoryDTAdapter;
import com.mezyapps.vgreen_autodriver.api_common.ApiClient;
import com.mezyapps.vgreen_autodriver.api_common.ApiInterface;
import com.mezyapps.vgreen_autodriver.model.CommonDeliveryModel;
import com.mezyapps.vgreen_autodriver.model.OrderHistoryDTModel;
import com.mezyapps.vgreen_autodriver.model.SuccessModel;
import com.mezyapps.vgreen_autodriver.utils.NetworkUtils;
import com.mezyapps.vgreen_autodriver.utils.SharedLoginUtils;
import com.mezyapps.vgreen_autodriver.utils.ShowProgressDialog;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonProductActivity extends AppCompatActivity {

    private CommonDeliveryModel commonDeliveryModel;
    private String order_no, user_id, order_date, name, total_amt, order_status, address, total_mrp, status;
    private ImageView iv_back;
    private TextView textOrderDate, textName, textMobileNumber, textOrderStatus, textTotalItem,
            textOrderNoTitle, textDeliveryAddress, textTotalAmtTopay;
    private RecyclerView recyclerView_product_list;
    private ShowProgressDialog showProgressDialog;
    public static ApiInterface apiInterface;
    private ArrayList<OrderHistoryDTModel> orderHistoryDTModelArrayList = new ArrayList<>();
    private OrderHistoryDTAdapter orderHistoryDTAdapter;
    private RelativeLayout rr_recycle_view;
    int arrayList_size;
    boolean is_visible = false;
    private Button btn_change_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_product);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(CommonProductActivity.this);
        iv_back = findViewById(R.id.iv_back);
        textOrderDate = findViewById(R.id.textOrderDate);
        textName = findViewById(R.id.textName);
        textOrderStatus = findViewById(R.id.textOrderStatus);
        recyclerView_product_list = findViewById(R.id.recyclerView_product_list);
        rr_recycle_view = findViewById(R.id.rr_recycle_view);
        textTotalItem = findViewById(R.id.textTotalItem);
        textOrderNoTitle = findViewById(R.id.textOrderNoTitle);
        textDeliveryAddress = findViewById(R.id.textDeliveryAddress);
        textTotalAmtTopay = findViewById(R.id.textTotalAmtTopay);
        btn_change_status = findViewById(R.id.btn_change_status);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommonProductActivity.this);
        recyclerView_product_list.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        commonDeliveryModel = bundle.getParcelable("ORDER_HD");
        order_no = commonDeliveryModel.getOrder_no();
        order_date = commonDeliveryModel.getDate();
        total_amt = commonDeliveryModel.getTotal_price();
        order_status = commonDeliveryModel.getStatus();
        total_mrp = commonDeliveryModel.getTotal_mrp();
        name = commonDeliveryModel.getCustomer_name();
        address = commonDeliveryModel.getDelivery_address();
        user_id = SharedLoginUtils.getUserId(CommonProductActivity.this);

        if (order_status.equalsIgnoreCase("Processing") || order_status.equalsIgnoreCase("Shipped")) {
            btn_change_status.setVisibility(View.VISIBLE);

            if (order_status.equalsIgnoreCase("Processing")) {
                status = "Shipped";
                btn_change_status.setText("Shipped");
            } else {
                status = "Delivered";
                btn_change_status.setText("Delivered");
            }
        }

        textOrderDate.setText(order_date);
        textOrderNoTitle.setText(order_no);
        textName.setText(name);
        textDeliveryAddress.setText(address);
        textTotalAmtTopay.setText(total_amt);
    }

    private void events() {
        if (NetworkUtils.isNetworkAvailable(CommonProductActivity.this)) {
            callOrderHistoryDT();
        } else {
            NetworkUtils.isNetworkNotAvailable(CommonProductActivity.this);
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textTotalItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_visible) {
                    rr_recycle_view.setVisibility(View.GONE);
                    is_visible = false;
                } else {
                    rr_recycle_view.setVisibility(View.VISIBLE);
                    is_visible = true;
                }
            }
        });
        btn_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(CommonProductActivity.this)) {
                    changeOrderStatus();
                } else {
                    NetworkUtils.isNetworkNotAvailable(CommonProductActivity.this);
                }
            }
        });
    }


    private void changeOrderStatus() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.changeOrderStatus(user_id,order_no, status);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        orderHistoryDTModelArrayList.clear();
                        String message = null, code = null;
                        if (successModule != null) {
                            code = successModule.getCode();
                            if (code.equalsIgnoreCase("1")) {
                                if (order_status.equalsIgnoreCase("Processing")) {
                                    Toast.makeText(CommonProductActivity.this, "Order is shipped to customer", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CommonProductActivity.this, ShippedOrderActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(CommonProductActivity.this, "Order is delivered to customer", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CommonProductActivity.this, CompleteDeliveryActivity.class));
                                    finish();
                                }

                            } else {
                                if (order_status.equalsIgnoreCase("Processing")) {
                                    Toast.makeText(CommonProductActivity.this, "Order  is  not shipped to customer", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CommonProductActivity.this, "Order is not delivered to  customer", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d("RESPONSE","NULL RESPONSE");
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

    private void callOrderHistoryDT() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.orderHistoryDT(order_no);
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgressDialog.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();
                        orderHistoryDTModelArrayList.clear();
                        String message = null, code = null, folder;
                        if (successModule != null) {
                            code = successModule.getCode();
                            folder = successModule.getFolder();
                            if (code.equalsIgnoreCase("1")) {

                                orderHistoryDTModelArrayList = successModule.getOrderHistoryDTModelArrayList();
                                if (orderHistoryDTModelArrayList.size() != 0) {
                                    arrayList_size = orderHistoryDTModelArrayList.size();
                                    textTotalItem.setVisibility(View.VISIBLE);
                                    textTotalItem.setText(String.valueOf(arrayList_size) + " " + "items");
                                    Collections.reverse(orderHistoryDTModelArrayList);
                                    orderHistoryDTAdapter = new OrderHistoryDTAdapter(CommonProductActivity.this, orderHistoryDTModelArrayList, folder);
                                    recyclerView_product_list.setAdapter(orderHistoryDTAdapter);
                                    orderHistoryDTAdapter.notifyDataSetChanged();
                                } else {
                                    orderHistoryDTAdapter.notifyDataSetChanged();
                                }
                            } else {
                                orderHistoryDTAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(CommonProductActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
