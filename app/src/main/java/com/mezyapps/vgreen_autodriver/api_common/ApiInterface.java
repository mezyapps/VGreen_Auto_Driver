package com.mezyapps.vgreen_autodriver.api_common;


import com.mezyapps.vgreen_autodriver.model.SuccessModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(EndApi.WS_LOGIN)
    @FormUrlEncoded
    Call<SuccessModel> login(@Field("mobile_no") String mobile_no,
                             @Field("password") String password);


    @POST(EndApi.WS_PENDING_ORDER)
    @FormUrlEncoded
    Call<SuccessModel> pendingOrder(@Field("user_id") String user_id);

    @POST(EndApi.WS_COMPLETED_ORDER)
    @FormUrlEncoded
    Call<SuccessModel> completedOrder(@Field("user_id") String user_id);

    @POST(EndApi.WS_SHIPPED_ORDER)
    @FormUrlEncoded
    Call<SuccessModel> shippedOrder(@Field("user_id") String user_id);


    @POST(EndApi.WS_CANCEL_ORDER)
    @FormUrlEncoded
    Call<SuccessModel> cancelOrder(@Field("user_id") String user_id);

    @POST(EndApi.WS_CHANGE_PASSWORD)
    @FormUrlEncoded
    Call<SuccessModel> chnagePassword(@Field("user_id") String user_id,
                                      @Field("password") String password);

    @POST(EndApi.ORDER_HISTORY_DT)
    @FormUrlEncoded
    Call<SuccessModel> orderHistoryDT(@Field("order_id") String order_id);

    @POST(EndApi.WS_CHANGE_ORDER_STATUS)
    @FormUrlEncoded
    Call<SuccessModel> changeOrderStatus(@Field("user_id") String user_id,
                                         @Field("order_id") String order_id,
                                         @Field("status") String status);

    @POST(EndApi.NOTIFICATION)
    @FormUrlEncoded
    Call<SuccessModel> notificationList(@Field("user_id") String user_id);

}
