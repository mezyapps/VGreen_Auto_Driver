package com.mezyapps.vgreen_autodriver.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mezyapps.vgreen_autodriver.R;
import com.mezyapps.vgreen_autodriver.api_common.ApiClient;
import com.mezyapps.vgreen_autodriver.api_common.ApiInterface;
import com.mezyapps.vgreen_autodriver.model.SuccessModel;
import com.mezyapps.vgreen_autodriver.utils.NetworkUtils;
import com.mezyapps.vgreen_autodriver.utils.SharedLoginUtils;
import com.mezyapps.vgreen_autodriver.utils.ShowProgressDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private ImageView iv_back;
    private Button btn_update;
    private TextInputEditText edit_password, edit_confirm_password;
    private ShowProgressDialog showProgressDialog;
    private String user_id, password, confirm_password;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        find_View_IdS();
        events();
    }

    private void find_View_IdS() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        edit_password = findViewById(R.id.edit_password);
        btn_update = findViewById(R.id.btn_update);
        iv_back = findViewById(R.id.iv_back);
        edit_confirm_password = findViewById(R.id.edit_confirm_password);
        showProgressDialog = new ShowProgressDialog(ChangePasswordActivity.this);

        user_id = SharedLoginUtils.getUserId(ChangePasswordActivity.this);
    }

    private void events() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (NetworkUtils.isNetworkAvailable(ChangePasswordActivity.this)) {
                        callChangePassword();
                    } else {
                        NetworkUtils.isNetworkNotAvailable(ChangePasswordActivity.this);
                    }
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean validation() {
        password = edit_password.getText().toString().trim();
        confirm_password = edit_confirm_password.getText().toString().trim();

        if (password.equalsIgnoreCase("")) {
            edit_password.setError("Enter Password");
            edit_password.requestFocus();
            return false;
        } else if (confirm_password.equalsIgnoreCase("")) {
            edit_confirm_password.setError("Enter  Confirm Password");
            edit_confirm_password.requestFocus();
            return false;
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(this, "Password And Confirm Password Not Match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void callChangePassword() {
        showProgressDialog.showDialog();
        Call<SuccessModel> call = apiInterface.chnagePassword(user_id, password);
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
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(ChangePasswordActivity.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                                ((MainActivity)getApplicationContext()).logoutApplication();
                                edit_confirm_password.setText("");
                                edit_password.setText("");
                                edit_password.requestFocus();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Password Not Change", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                    }


                } catch (
                        Exception e) {
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
