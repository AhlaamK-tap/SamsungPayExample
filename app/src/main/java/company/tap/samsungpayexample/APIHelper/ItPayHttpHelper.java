package company.tap.samsungpayexample.APIHelper;

import android.util.Base64;

import company.tap.samsungpayexample.BuildConfig;


 

public interface ItPayHttpHelper {
    default String getAuthorizationHeader() {
        return BuildConfig.NOONPAY_AUTH_SCHEME + " " + Base64.encodeToString((BuildConfig.NOONPAY_BUSINESS_ID + ":" + BuildConfig.NOONPAY_KEY).getBytes(), Base64.DEFAULT);
    }

    default String getOrderAPIUrl() {
        return BuildConfig.NOONPAY_ORDER_API;
    }
}
