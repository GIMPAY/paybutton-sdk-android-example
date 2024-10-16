package com.example.gimpaybuttonexmple

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.paysky.data.model.SuccessfulCardTransaction
import io.paysky.data.model.SuccessfulWalletTransaction
import io.paysky.exception.TransactionException
import io.paysky.ui.PayButton
import io.paysky.ui.PayButton.PaymentTransactionCallback
import io.paysky.util.AllURLsStatus
import io.paysky.util.AppUtils

class MainActivity : AppCompatActivity() {
    //GUI.
    private lateinit var merchantIdEditText: EditText
    private lateinit var terminalIdEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var paymentStatusTextView: TextView
    private lateinit var pay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // find views.
        pay = findViewById(R.id.paybtn)
        merchantIdEditText = findViewById(R.id.merchant_id_editText)
        terminalIdEditText = findViewById(R.id.terminal_id_editText)
        amountEditText = findViewById(R.id.amount_editText)
        paymentStatusTextView = findViewById(R.id.payment_status_textView)
        pay.isEnabled = true
        pay.setOnClickListener(View.OnClickListener { v: View? ->
            val terminalId = terminalIdEditText.text.toString().trim { it <= ' ' }
            val merchantId = merchantIdEditText.text.toString().trim { it <= ' ' }
            val amount = amountEditText.text.toString().trim { it <= ' ' }
            if (terminalId.isEmpty() || merchantId.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this@MainActivity, "check all inputs", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            // add config
            val payButton = PayButton(this@MainActivity)

            // add payments data.
            payButton.setMerchantId(merchantId.toLong().toString()) // Merchant id
            payButton.setTerminalId(terminalId.toLong().toString()) // Terminal  id
            payButton.setAmount(amount.toDouble()) // Amount
            payButton.setCurrencyCode(952) // Currency Code
            payButton.setCustomerEmail("example@example.com")
            payButton.setTransactionReferenceNumber("144232")
            payButton.setMerchantSecureHash("e87a15e7048f97da99d305db75e3241a")
            payButton.setProductionStatus(AllURLsStatus.PRODUCTION)
            payButton.createTransaction(object : PaymentTransactionCallback {
                override fun onCardTransactionSuccess(successfulCardTransaction: SuccessfulCardTransaction) {
                    Toast.makeText(
                        this@MainActivity,
                        "pay success = $successfulCardTransaction",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onWalletTransactionSuccess(successfulWalletTransaction: SuccessfulWalletTransaction) {
                    Toast.makeText(
                        this@MainActivity,
                        "pay success = $successfulWalletTransaction",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onError(e: TransactionException) {
                    Toast.makeText(this@MainActivity, "pay fail = " + e.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
        })

        val appVersion = findViewById<TextView>(R.id.app_version_textView)
        appVersion.text = "PaySDK - GIM PayButton module - Ver.  " + AppUtils.getVersionNumber(this)
    }
}