package com.mezyapps.vgreen_autodriver.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.api_common.ApiClient;
import com.mezyapps.vgreen_autodriver.api_common.ApiInterface;
import com.mezyapps.vgreen_autodriver.model.SuccessModel;
import com.mezyapps.vgreen_autodriver.model.UserProfileModel;
import com.mezyapps.vgreen_autodriver.utils.NetworkUtils;
import com.mezyapps.vgreen_autodriver.utils.SharedLoginUtils;
import com.mezyapps.vgreen_autodriver.utils.ShowProgressDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_email,edit_password;
    private Button btn_login;
    private String mobile_no, password;
    public static ApiInterface apiInterface;
    private ShowProgressDialog showProgressDialog;
    private ArrayList<UserProfileModel> userProfileModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        find_View_IdS();
        events();
    }

    private void find_View_IdS()
    {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog = new ShowProgressDialog(LoginActivity.this);
        btn_login=findViewById(R.id.btn_login);
        edit_email=findViewById(R.id.edit_email);
        edit_password=findViewById(R.id.edit_password);
    }

    private void events() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                        calllogin();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(LoginActivity.this);
                    }
                }

            }
        });
    }
    private boolean validation() {
        mobile_no = edit_email.getText().toString().trim();
        password = edit_password.getText().toString().trim();
        if (mobile_no.equalsIgnoreCase("")) {
            edit_email.setError("Enter Username");
            edit_email.requestFocus();
            return false;
        } else if (password.equalsIgnoreCase("")) {

            edit_password.setError("Enter Password");
            edit_password.requestFocus();
            return false;
        }
        return true;
    }

    private void calllogin() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.login(mobile_no, password);
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
                            userProfileModelArrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                userProfileModelArrayList = successModule.getUserProfileModelArrayList();
                                if (userProfileModelArrayList.size() != 0) {
                                    String id = userProfileModelArrayList.get(0).getId();
                                    String name = userProfileModelArrayList.get(0).getDriver_name();
                                    String mobile = userProfileModelArrayList.get(0).getDriver_mobile_no();
                                    String vehicle_no = userProfileModelArrayList.get(0).getVehicle_no();
                                    String location = userProfileModelArrayList.get(0).getLocation();
                                    String route = userProfileModelArrayList.get(0).getRoute();

                                    SharedLoginUtils.putLoginSharedUtils(LoginActivity.this);
                                    SharedLoginUtils.addUserId(LoginActivity.this, id, name, mobile, vehicle_no, location, route);
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();


                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "User Not Registered", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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
