package company.tap.samsungpayexample.Subscribers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import company.tap.samsungpayexample.APIHelper.HttpClientAsync;
import company.tap.samsungpayexample.APIHelper.IShowMessage;
import company.tap.samsungpayexample.APIHelper.ITransformer;
import company.tap.samsungpayexample.APIHelper.Identifiers;
import company.tap.samsungpayexample.APIHelper.TaskRequest;
import company.tap.samsungpayexample.Models.Request.InitiateOrder.InitiateOrder;



public class SamsungCardVerifiedReceived extends BroadcastReceiver implements ITransformer, IShowMessage {
    final static String TAG = "SamsungCardVerifiedReceived";
    Context context;

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "On Receive [Samsung verification]");
        try {
            InitiateOrder initiateOrder = intent.getParcelableExtra(Identifiers.ORDER_SUBMITTED);
            HttpClientAsync httpClient = new HttpClientAsync(context);

            TaskRequest taskRequest = new TaskRequest<>(initiateOrder,
                    company.tap.samsungpayexample.Models.Response.InitiateOrder.InitiateOrder.class);
            httpClient.execute(taskRequest);

            showMessage("Order submitted to noonPay! successfully");
        } finally {
            LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(this);
        }
    }

    @Override
    public Context getContext() {
        return this.context;
    }
}
