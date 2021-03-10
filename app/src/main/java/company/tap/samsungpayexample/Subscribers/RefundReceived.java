package company.tap.samsungpayexample.Subscribers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import company.tap.samsungpayexample.APIHelper.IShowMessage;
import company.tap.samsungpayexample.APIHelper.ITransformer;
import company.tap.samsungpayexample.APIHelper.Identifiers;
import company.tap.samsungpayexample.MainActivity;

import java.util.ArrayList;
import java.util.Locale;

import static androidx.core.content.ContextCompat.startActivity;


public class RefundReceived extends BroadcastReceiver implements ITransformer, IShowMessage {
    final static String TAG = "RefundReceived";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "On Receive Refund");
        try {
            company.tap.samsungpayexample.Models.Response.Refund.Refund refundResponse = intent.getParcelableExtra(Identifiers.REFUND_SUCCEED);
            if (refundResponse != null && refundResponse.getResultCode() == 0) {
                showMessage("Refund done successfully, transaction Id: " + refundResponse.getResult().getTransactionId());
                // open refund view on success!
                Intent refundDetailsIntent = new Intent(context.getApplicationContext(), company.tap.samsungpayexample.RefundDetails.class);
                ArrayList<String> info = new ArrayList<String>() {
                    {
                        add(String.format(Locale.ENGLISH, "Authorization code: %s", refundResponse.getResult().getAuthorizationCode()));
                        add(String.format(Locale.ENGLISH, "Refunded amount: %s", refundResponse.getResult().getTotalRefundedAmount()));
                        add(String.format(Locale.ENGLISH, "Order Id: %s", refundResponse.getResult().getOrderId()));
                        add(String.format(Locale.ENGLISH, "Order Currency: %s", refundResponse.getResult().getCurrency()));
                        add(String.format(Locale.ENGLISH, "Transaction Id: %s", refundResponse.getResult().getTransactionId()));
                        add(String.format(Locale.ENGLISH, "Order status: %s", refundResponse.getResult().getStatus()));
                    }
                };
                MainActivity.putBagArrayValue(Identifiers.REFUND_SUCCEED_INFO, info);
                if (context instanceof company.tap.samsungpayexample.MainActivity)
                    ActivityCompat.startActivityForResult((company.tap.samsungpayexample.MainActivity) context, refundDetailsIntent, 0, null);
                else
                    startActivity(context.getApplicationContext(), refundDetailsIntent, null);
            } else {
                Intent errorIntent = new Intent("company.tap.samsungpayexample.ERROR_RAISED");
                intent.putExtra(Identifiers.ERROR_MSG, "Failed to capture the order" + (refundResponse == null ? "" : refundResponse.getMessage()));
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
