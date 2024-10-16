package com.example.gimpaybuttonexmple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.paysky.data.model.SuccessfulCardTransaction;
import io.paysky.data.model.SuccessfulWalletTransaction;
import io.paysky.exception.TransactionException;
import io.paysky.ui.PayButton;
import io.paysky.util.AllURLsStatus;
import io.paysky.util.AppUtils;


public class MainActivity extends AppCompatActivity {

    //GUI.
    private EditText merchantIdEditText, terminalIdEditText, amountEditText;
    private TextView paymentStatusTextView;
    private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find views.
        pay = findViewById(R.id.paybtn);
        merchantIdEditText = findViewById(R.id.merchant_id_editText);
        terminalIdEditText = findViewById(R.id.terminal_id_editText);
        amountEditText = findViewById(R.id.amount_editText);
        paymentStatusTextView = findViewById(R.id.payment_status_textView);
        pay.setEnabled(true);
        pay.setOnClickListener(v -> {
            String terminalId = terminalIdEditText.getText().toString().trim();
            String merchantId = merchantIdEditText.getText().toString().trim();
            String amount = amountEditText.getText().toString().trim();
            if (terminalId.isEmpty() || merchantId.isEmpty() || amount.isEmpty()) {
                Toast.makeText(MainActivity.this, "check all inputs", Toast.LENGTH_SHORT).show();
                return;
            }

            // add config
            PayButton payButton = new PayButton(MainActivity.this);

            // add payments data.
            payButton.setMerchantId(String.valueOf(Long.valueOf(merchantId))); // Merchant id
            payButton.setTerminalId(String.valueOf(Long.valueOf(terminalId))); // Terminal  id
            payButton.setAmount(Double.parseDouble(amount)); // Amount
            payButton.setCurrencyCode(952); // Currency Code
            payButton.setCustomerEmail("example@example.com");
            payButton.setTransactionReferenceNumber("1234");
            payButton.setMerchantSecureHash("e87a15e7048f97da99d305db75e3241a");
            payButton.setProductionStatus(AllURLsStatus.PRODUCTION);
            payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                @Override
                public void onCardTransactionSuccess(SuccessfulCardTransaction successfulCardTransaction) {
                    Toast.makeText(MainActivity.this, "pay success = " + successfulCardTransaction, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onWalletTransactionSuccess(SuccessfulWalletTransaction successfulWalletTransaction) {
                    Toast.makeText(MainActivity.this, "pay success = " + successfulWalletTransaction, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(TransactionException e) {
                    Toast.makeText(MainActivity.this, "pay fail = " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        TextView appVersion = findViewById(R.id.app_version_textView);
        appVersion.setText("PaySDK - GIM PayButton module - Ver.  " + AppUtils.getVersionNumber(this));
    }
}