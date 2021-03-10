package company.tap.samsungpayexample.Subscribers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import company.tap.samsungpayexample.APIHelper.HttpClientAsync;
import company.tap.samsungpayexample.APIHelper.IShowMessage;
import company.tap.samsungpayexample.APIHelper.ITransformer;
import company.tap.samsungpayexample.APIHelper.Identifiers;
import company.tap.samsungpayexample.APIHelper.TaskRequest;
import company.tap.samsungpayexample.MainActivity;
import company.tap.samsungpayexample.Models.Request.ProcessAuthentication.ProcessAuthentication;
import company.tap.samsungpayexample.Models.Response.PaymentInfo.PaymentInfo;

import static androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance;



public class PaymentInfoReceived extends BroadcastReceiver implements ITransformer, IShowMessage {
    final static String TAG = "PaymentInfoReceived";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "On Receive [Payment Info]");
        try {
            PaymentInfo paymentInfo = intent.getParcelableExtra(Identifiers.PAYMENT_INFO);
            if (paymentInfo != null && paymentInfo.getResultCode() == 0) {
                showMessage("Payment info added with Id: " + paymentInfo.getResult().getPaymentInfoId());
                //preparing for authenticator
                ProcessAuthentication authenticator = BuildAuthenticator(MainActivity.getBagValue(Identifiers.SPAY_PAYMENT_VERIFICATION_DATA),
                        paymentInfo.getResult().getOrderId(),
                        MainActivity.getBagValue(Identifiers.PAYMENT_METHOD),
                        null);
                HttpClientAsync httpClient = new HttpClientAsync(context);

                TaskRequest taskRequest = new TaskRequest<>(authenticator,
                       company.tap.samsungpayexample.Models.Response.ProcessAuthentication.ProcessAuthentication.class);

                httpClient.execute(taskRequest);
            } else {
                Intent errorIntent = new Intent("com.noonpay.sample.samsungPay.ERROR_RAISED");
                intent.putExtra(Identifiers.ERROR_MSG, "Failed to add payment info!");
               getInstance(context.getApplicationContext()).sendBroadcast(errorIntent);
            }
        } finally {
            getInstance(context.getApplicationContext()).unregisterReceiver(this);
        }
    }

    @Override
    public Context getContext() {
        return this.context;
    }
}
