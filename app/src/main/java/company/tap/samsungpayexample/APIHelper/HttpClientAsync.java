package company.tap.samsungpayexample.APIHelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import company.tap.samsungpayexample.MainActivity;
import company.tap.samsungpayexample.Models.Response.InitiateOrder.InitiateOrder;
import company.tap.samsungpayexample.Models.Response.PaymentInfo.PaymentInfo;
import company.tap.samsungpayexample.Models.Response.ProcessAuthentication.ProcessAuthentication;
import company.tap.samsungpayexample.Models.Response.Refund.Refund;
import company.tap.samsungpayexample.Models.Response.Sale.Sale;

import java.util.ArrayList;



public class HttpClientAsync extends AsyncTask<TaskRequest, Void, Object> {
    private static final String TAG = "HttpClientAsync";
    private ItPayHttpHelper itPayHttpHelper = new ItPayHttpHelper() {
    };
    private final ThreadLocal<Context> context;

    public HttpClientAsync(final Context context) {
        this.context = new ThreadLocal<Context>() {
            @Override
            protected Context initialValue() {
                return context;
            }
        };
    }

    private final IPostHttpClient httpClient = new IPostHttpClient() {
        @Override
        public Context getContext() {
            return context.get();
        }

        @Override
        public ITransformer ITransformResult() {
            return new ITransformer() {
            };
        }

        @Override
        public String getAuthorizationHeader() {
            return itPayHttpHelper.getAuthorizationHeader();
        }

        @Override
        public String getHttpUrl() {
            return itPayHttpHelper.getOrderAPIUrl();
        }
    };


    @Override
    protected Object doInBackground(TaskRequest... taskRequests) {
        for (TaskRequest request : taskRequests) {
            try {
                return httpClient.PostData(request.getModel(), request.getOutType());
            } catch (Exception error) {
                error.printStackTrace(System.err);
                Intent intent = new Intent("com.noonpay.sample.samsungPay.ERROR_RAISED");
                intent.putExtra(Identifiers.ERROR_MSG, error.getMessage());
                LocalBroadcastManager.getInstance(context.get().getApplicationContext()).sendBroadcast(intent);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object response) {
        if (response != null) {
            //broadcast event
            ArrayList<String> paymentEvents;
            switch (response.getClass().getName()) {
                case "com.noonpay.sample.samsungPay.noonpayModels.Response.InitiateOrder.InitiateOrder": {
                    Intent intent = new Intent("com.noonpay.sample.samsungPay.ORDER_INITIATED");
                    intent.setPackage("com.noonpay.sample.samsungPay");
                    intent.putExtra(Identifiers.ORDER_INITIATED,(InitiateOrder) response);
                    paymentEvents = MainActivity.getBagArrayValue(Identifiers.PAYMENT_EVENTS);
                    if (paymentEvents == null)
                        paymentEvents = new ArrayList<>();
                    paymentEvents.add(Identifiers.ORDER_INITIATED);
                   MainActivity.putBagArrayValue(Identifiers.PAYMENT_EVENTS, paymentEvents);
                    LocalBroadcastManager.getInstance(context.get()).sendBroadcast(intent);
                    break;
                }

                case "com.noonpay.sample.samsungPay.noonpayModels.Response.PaymentInfo.PaymentInfo": {
                    Intent intent = new Intent("com.noonpay.sample.samsungPay.PAYMENT_INFO");
                    intent.setPackage("com.noonpay.sample.samsungPay");
                    PaymentInfo paymentInfo = (PaymentInfo) response;
                    intent.putExtra(Identifiers.PAYMENT_INFO, paymentInfo);
                    paymentEvents =company.tap.samsungpayexample.MainActivity.getBagArrayValue(Identifiers.PAYMENT_EVENTS);
                    paymentEvents.add(Identifiers.PAYMENT_INFO);
                   company.tap.samsungpayexample.MainActivity.putBagValue(Identifiers.PAYMENT_METHOD, paymentInfo.getResult().getMethod());
                   company.tap.samsungpayexample.MainActivity.putBagArrayValue(Identifiers.PAYMENT_EVENTS, paymentEvents);
                    LocalBroadcastManager.getInstance(context.get()).sendBroadcast(intent);
                    break;
                }
                case "com.noonpay.sample.samsungPay.noonpayModels.Response.ProcessAuthentication.ProcessAuthentication": {
                    Intent intent = new Intent("com.noonpay.sample.samsungPay.ORDER_AUTHENTICATED");
                    intent.setPackage("com.noonpay.sample.samsungPay");
                    intent.putExtra(Identifiers.ORDER_AUTHENTICATED, (ProcessAuthentication) response);
                    paymentEvents =company.tap.samsungpayexample.MainActivity.getBagArrayValue(Identifiers.PAYMENT_EVENTS);
                    paymentEvents.add(Identifiers.ORDER_AUTHENTICATED);
                   company.tap.samsungpayexample.MainActivity.putBagArrayValue(Identifiers.PAYMENT_EVENTS, paymentEvents);
                    LocalBroadcastManager.getInstance(context.get()).sendBroadcast(intent);
                    break;
                }
                case "com.noonpay.sample.samsungPay.noonpayModels.Response.Sale.Sale": {
                    Intent intent = new Intent("com.noonpay.sample.samsungPay.PAYMENT_SUCCEED");
                    intent.setPackage("com.noonpay.sample.samsungPay");
                    intent.putExtra(Identifiers.PAYMENT_SUCCEED, (Sale) response);
                    paymentEvents =company.tap.samsungpayexample.MainActivity.getBagArrayValue(Identifiers.PAYMENT_EVENTS);
                    paymentEvents.add(Identifiers.PAYMENT_SUCCEED);
                   company.tap.samsungpayexample.MainActivity.putBagArrayValue(Identifiers.PAYMENT_EVENTS, paymentEvents);
                    LocalBroadcastManager.getInstance(context.get()).sendBroadcast(intent);
                    break;
                }
                case "com.noonpay.sample.samsungPay.noonpayModels.Response.Refund.Refund": {
                    Intent intent = new Intent("com.noonpay.sample.samsungPay.REFUND_SUCCEED");
                    intent.setPackage("com.noonpay.sample.samsungPay");
                    intent.putExtra(Identifiers.REFUND_SUCCEED, (Refund) response);
                    paymentEvents =company.tap.samsungpayexample.MainActivity.getBagArrayValue(Identifiers.PAYMENT_EVENTS);
                    paymentEvents.add(Identifiers.REFUND_SUCCEED);
                   company.tap.samsungpayexample.MainActivity.putBagArrayValue(Identifiers.PAYMENT_EVENTS, paymentEvents);
                    LocalBroadcastManager.getInstance(context.get()).sendBroadcast(intent);
                    break;
                }
            }

        }
    }

}
