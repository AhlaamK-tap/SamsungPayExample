package company.tap.samsungpayexample.APIHelper;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


@FunctionalInterface
public interface IShowMessage {
    Context getContext();

    default void showMessage(String message) {
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (Exception error) {
                    error.printStackTrace();
                    Log.e("IShowMessage", error.getMessage());
                }
            }
        };
        mThread.start();
    }
}
