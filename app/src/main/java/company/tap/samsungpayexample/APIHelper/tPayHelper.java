package company.tap.samsungpayexample.APIHelper;

import android.content.Context;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import company.tap.samsungpayexample.BuildConfig;
import company.tap.samsungpayexample.Models.Request.PaymentInfo.PaymentInfo;
import company.tap.samsungpayexample.Models.Response.InitiateOrder.InitiateOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;



public class tPayHelper implements ItPayHttpHelper {
    private final Context _context;
    private final RequestQueue mRequestQueue;
    private final ItPayHttpHelper Ihelper=new ItPayHttpHelper() {
    };
    public tPayHelper(@NonNull final Context context) {
        this._context = context;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(this._context.getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
    }
    public final String TAG = "noonPayHelper";

    protected Map<String, String> getAuthHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization",Ihelper.getAuthorizationHeader());
        return headers;
    }

    //region using google gson
    public void InitiateOrder(company.tap.samsungpayexample.Models.Request.InitiateOrder.InitiateOrder initiateOrder, Consumer<InitiateOrder> OnSuccess) {

        try {
            JsonRequest jsonRequest = new JsonRequest<>(Request.Method.POST
                    , BuildConfig.NOONPAY_ORDER_API
                    , InitiateOrder.class
                    , initiateOrder
                    , getAuthHeaders()
                    , (response) -> {
                InitiateOrder data = response;
                OnSuccess.accept(data);
            }, error -> {
                Log.e("InitiateOrder", error.toString());
            });
            // Add the request to the RequestQueue.
            mRequestQueue.add(jsonRequest);
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
        } finally {
            Log.i(TAG, "request done!");
        }
    }

    public void AddPaymentInfo(company.tap.samsungpayexample.Models.Request.PaymentInfo.PaymentInfo paymentInfo, Consumer<PaymentInfo> OnSuccess) {
        try {
            JsonRequest jsonRequest = new JsonRequest<>(Request.Method.POST
                    , BuildConfig.NOONPAY_ORDER_API
                    , PaymentInfo.class
                    , paymentInfo
                    , getAuthHeaders()
                    , (response) -> {
                PaymentInfo data = response;
                OnSuccess.accept(data);
            }, error -> {
                Log.e("AddPaymentInfo", error.toString());
            });
            // Add the request to the RequestQueue.
            mRequestQueue.add(jsonRequest);
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
        } finally {
            Log.i(TAG, "request done!");
        }
    }
//endregion
}
