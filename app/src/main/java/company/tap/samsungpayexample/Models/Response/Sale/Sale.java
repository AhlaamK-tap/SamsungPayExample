package company.tap.samsungpayexample.Models.Response.Sale;

import android.os.Parcel;
import android.os.Parcelable;

import company.tap.samsungpayexample.Models.GeneralResponse;


public class Sale extends GeneralResponse<SaleResponse> implements Parcelable {
    public Sale() {

    }

    protected Sale(Parcel in) {
    }

    public static final Creator<Sale> CREATOR = new Creator<Sale>() {
        @Override
        public Sale createFromParcel(Parcel in) {
            return new Sale(in);
        }

        @Override
        public Sale[] newArray(int size) {
            return new Sale[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
