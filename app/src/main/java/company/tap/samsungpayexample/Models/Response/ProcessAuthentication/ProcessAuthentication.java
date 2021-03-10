package company.tap.samsungpayexample.Models.Response.ProcessAuthentication;

import android.os.Parcel;
import android.os.Parcelable;

import company.tap.samsungpayexample.Models.GeneralResponse;


public class ProcessAuthentication extends GeneralResponse<ProcessAuthenticationResponse> implements Parcelable {
    public ProcessAuthentication() {

    }

    protected ProcessAuthentication(Parcel in) {
    }

    public static final Creator<ProcessAuthentication> CREATOR = new Creator<ProcessAuthentication>() {
        @Override
        public ProcessAuthentication createFromParcel(Parcel in) {
            return new ProcessAuthentication(in);
        }

        @Override
        public ProcessAuthentication[] newArray(int size) {
            return new ProcessAuthentication[size];
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
