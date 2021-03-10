package company.tap.samsungpayexample.Models.Response.PaymentInfo;

import android.os.Parcel;
import android.os.Parcelable;

import company.tap.samsungpayexample.Models.GeneralResponse;



public class PaymentInfo extends GeneralResponse<PaymentInfoResponse> implements Parcelable {


    public PaymentInfo() {

    }

    protected PaymentInfo(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentInfo> CREATOR = new Creator<PaymentInfo>() {
        @Override
        public PaymentInfo createFromParcel(Parcel in) {
            return new PaymentInfo(in);
        }

        @Override
        public PaymentInfo[] newArray(int size) {
            return new PaymentInfo[size];
        }
    };
}
