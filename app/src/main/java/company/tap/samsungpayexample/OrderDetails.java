package company.tap.samsungpayexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import company.tap.samsungpayexample.APIHelper.HttpClientAsync;
import company.tap.samsungpayexample.APIHelper.IShowMessage;
import company.tap.samsungpayexample.APIHelper.ITransformer;
import company.tap.samsungpayexample.APIHelper.Identifiers;
import company.tap.samsungpayexample.APIHelper.TaskRequest;
import company.tap.samsungpayexample.Models.Response.Sale.Sale;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class OrderDetails extends Activity implements ITransformer, IShowMessage {
    private Sale saleResponse = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        final Button home_button = findViewById(R.id.home_button);
        home_button.setOnClickListener(this::onHomeClick);
        final Button refund_button = findViewById(R.id.refund_btn);
        refund_button.setOnClickListener(this::OnRefundClick);
        //print info
        saleResponse = (Sale) MainActivity.getBagParcelableValue(Identifiers.PAYMENT_SUCCEED_RESPONSE);
        if (saleResponse == null && saleResponse.getResultCode() == 0)
            refund_button.setEnabled(false);
        else
            refund_button.setEnabled(true);
        ArrayList<String> info = MainActivity.getBagArrayValue(Identifiers.PAYMENT_SUCCEED_INFO);
        TextView textView = findViewById(R.id.info_view);
        if (info != null) {
            String data = info.stream()
                    .collect(Collectors.joining("\n"));
            textView.setText(data);
        } else {
            textView.setText("Payment done!");
        }
    }

    public void onHomeClick(View view) {
        Intent intent = new Intent(getApplicationContext(), company.tap.samsungpayexample.MainActivity.class);
        startActivity(intent);
    }

    public void OnRefundClick(View view) {
        final Button refund_button = findViewById(R.id.refund_btn);
        refund_button.setEnabled(false);
        final ProgressBar progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
        //call refund
        company.tap.samsungpayexample.Models.Request.Refund.Refund refundInfo = BuildRefundRequest(
                saleResponse.getResult().getOrderId(),
                saleResponse.getResult().getCapturedAmount(),
                saleResponse.getResult().getCurrency(),
                saleResponse.getResult().getTransactionId());
        HttpClientAsync httpClient = new HttpClientAsync(getContext());

        TaskRequest taskRequest = new TaskRequest<>(refundInfo,
                company.tap.samsungpayexample.Models.Response.Refund.Refund.class);
        httpClient.execute(taskRequest);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
