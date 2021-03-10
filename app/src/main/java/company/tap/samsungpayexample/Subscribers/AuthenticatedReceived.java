package company.tap.samsungpayexample.Subscribers;


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
import company.tap.samsungpayexample.Models.Request.Sale.Sale;
import company.tap.samsungpayexample.Models.Response.ProcessAuthentication.ProcessAuthentication;

public class AuthenticatedReceived extends BroadcastReceiver implements ITransformer, IShowMessage {
    final static String TAG = "AuthenticatedReceived";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "On Receive [Payment Authentication]");
        try {
            ProcessAuthentication authenticator = intent.getParcelableExtra(Identifiers.ORDER_AUTHENTICATED);
            if (authenticator != null && authenticator.getResultCode() == 0) {
                showMessage("Order authenticated successfully, for merchant " + authenticator.getResult().getMerchant());
                //preparing for sale (authorize & capture)
                Sale saleRequest = BuildSaleRequest(authenticator.getResult().getOrderId());

                HttpClientAsync httpClient = new HttpClientAsync(context);

                TaskRequest taskRequest = new TaskRequest<>(saleRequest,
                        company.tap.samsungpayexample.Models.Response.Sale.Sale.class);

                httpClient.execute(taskRequest);
            } else {
                Intent errorIntent = new Intent("company.tap.samsungpayexample.ERROR_RAISED");
                intent.putExtra(Identifiers.ERROR_MSG, "Failed to process authentication info!");
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
