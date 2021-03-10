package company.tap.samsungpayexample.Subscribers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import company.tap.samsungpayexample.APIHelper.IShowMessage;
import company.tap.samsungpayexample.APIHelper.ITransformer;
import company.tap.samsungpayexample.APIHelper.Identifiers;
import company.tap.samsungpayexample.OrderDetails;

import java.util.ArrayList;
import java.util.Locale;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;




public class SaleReceived extends BroadcastReceiver implements ITransformer, IShowMessage {
    final static String TAG = "SaleReceived";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "On Receive [Payment done]");
        try {
            company.tap.samsungpayexample.Models.Response.Sale.Sale saleResponse = intent.getParcelableExtra(Identifiers.PAYMENT_SUCCEED);
            if (saleResponse != null && saleResponse.getResultCode() == 0) {
                company.tap.samsungpayexample.MainActivity.putBagParcelableValue(Identifiers.PAYMENT_SUCCEED_RESPONSE, saleResponse);
                showMessage("Order done successfully, authorization code: " + saleResponse.getResult().getAuthorizationCode());
                // open order details on success!
                Intent orderDetailsIntent = new Intent(context.getApplicationContext(), OrderDetails.class);
                ArrayList<String> info = new ArrayList<String>() {
                    {
                        add(String.format(Locale.ENGLISH, "Authorization code: %s", saleResponse.getResult().getAuthorizationCode()));
                        add(String.format(Locale.ENGLISH, "Captured amount: %s", saleResponse.getResult().getCapturedAmount()));
                        add(String.format(Locale.ENGLISH, "Order Id: %s", saleResponse.getResult().getOrderId()));
                        add(String.format(Locale.ENGLISH, "Order Currency: %s", saleResponse.getResult().getCurrency()));
                        add(String.format(Locale.ENGLISH, "Transaction Id: %s", saleResponse.getResult().getTransactionId()));
                        add(String.format(Locale.ENGLISH, "Order status: %s", saleResponse.getResult().getStatus()));
                    }
                };
                company.tap.samsungpayexample.MainActivity.putBagArrayValue(Identifiers.PAYMENT_SUCCEED_INFO, info);
                if (context instanceof company.tap.samsungpayexample.MainActivity)
              startActivityForResult((company.tap.samsungpayexample.MainActivity) context, orderDetailsIntent, 0, null);
                else
                    startActivity(context.getApplicationContext(), orderDetailsIntent, null);
            } else {
                Intent errorIntent = new Intent("company.tap.samsungpayexample.ERROR_RAISED");
                intent.putExtra(Identifiers.ERROR_MSG, "Failed to capture the order" + (saleResponse == null ? "" : saleResponse.getMessage()));
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