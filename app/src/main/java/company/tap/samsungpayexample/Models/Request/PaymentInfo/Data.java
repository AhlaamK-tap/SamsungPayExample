package company.tap.samsungpayexample.Models.Request.PaymentInfo;



import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    private String returnUrl;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    protected Data(Parcel in) {
        this.returnUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(returnUrl);
    }

    public int describeContents() {
        return 0;
    }

}