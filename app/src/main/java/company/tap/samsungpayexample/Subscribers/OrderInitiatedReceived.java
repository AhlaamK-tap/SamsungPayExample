package company.tap.samsungpayexample.Subscribers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import company.tap.samsungpayexample.APIHelper.HttpClientAsync;
import company.tap.samsungpayexample.APIHelper.IShowMessage;
import company.tap.samsungpayexample.APIHelper.ITransformer;
import company.tap.samsungpayexample.APIHelper.Identifiers;
import company.tap.samsungpayexample.APIHelper.TaskRequest;
import company.tap.samsungpayexample.MainActivity;
import company.tap.samsungpayexample.Models.Request.PaymentInfo.PaymentInfo;
import company.tap.samsungpayexample.Models.Response.InitiateOrder.InitiateOrder;

import java.util.Optional;



public class OrderInitiatedReceived extends BroadcastReceiver implements ITransformer, IShowMessage {
    final static String TAG = "OrderInitiatedReceived";
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "On Receive [Order Initiated]");
        try {

            InitiateOrder order = intent.getParcelableExtra(Identifiers.ORDER_INITIATED);
            String orderId, paymentMethod = "SamsungPay";//default value for testing
            //TODO: filter should be revised.
            if (order != null && order.getResultCode() == 0) {
                showMessage("OrderId: " + order.getResult().getOrderId());
                orderId = order.getResult().getOrderId();
               MainActivity.putBagValue(Identifiers.ORDER_ID, orderId);
                Optional<String> option = order
                        .getResult()
                        .getPaymentMethods()
                        .stream()
                        .findFirst();
                if (option.isPresent())
                    paymentMethod = option.get();
               PaymentInfo paymentInfo = BuildPaymentInfo(orderId, paymentMethod);
                HttpClientAsync httpClient = new HttpClientAsync(context);

                TaskRequest taskRequest = new TaskRequest<>(paymentInfo,
                        company.tap.samsungpayexample.Models.Response.PaymentInfo.PaymentInfo.class);
                httpClient.execute(taskRequest);
            } else {
                Intent errorIntent = new Intent("com.noonpay.sample.samsungPay.ERROR_RAISED");
                intent.putExtra(Identifiers.ERROR_MSG, "Failed to initialize payment order!");
                LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(errorIntent);
            }
        } finally {
            LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(this);
        }
    }

    @Override
    public Context getContext() {
        return this.context;
    }
}
