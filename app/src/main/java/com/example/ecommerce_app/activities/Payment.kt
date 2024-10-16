package com.example.ecommerce_app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce_app.PaymobService
import com.example.ecommerce_app.R
import org.json.JSONObject


class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)

        // Get the total price from the Intent
        val totalPrice = intent.getDoubleExtra("total_price", 0.0)
        Log.d("PaymentActivity", "Total Price: $totalPrice")

        // Display the total price in the total_amount_value TextView using string resource
      //  val totalAmountValue = findViewById<TextView>(R.id.total_amount_value)
         // Use the resource string
     //   totalAmountValue.text = getString(R.string.total_amount_format, totalPrice)

        // Display the total price in the total_amount_value TextView
      //  totalAmountValue.text = String.format("%.2f", totalPrice) + " EGP"  // Format to 2 decimal places

        // Handle the payment process
        handlePayment()

        //back icon
        val backIcon = findViewById<ImageView>(R.id.backIcon)
        backIcon.setOnClickListener {
           onBackPressed() // Or finish() in an activity
       }

    }
    private fun handlePayment() {
        // Step 1: Authenticate
        PaymobService.getAuthToken(
            onSuccess = { token ->
                val totalPrice = intent.getDoubleExtra("total_price", 0.0)

                // Create billing data
                val billingDataMap = mapOf(
                    "email" to "customer@example.com", // Replace with the actual email
                    "phone_number" to "01234567890",   // Replace with the actual phone number
                    "first_name" to "John",             // Replace with the actual first name
                    "last_name" to "Doe",                // Replace with the actual last name
                    "country" to "Egypt",               // Replace with the actual country
                    "city" to "Cairo",                  // Replace with the actual city
                    "state" to "Cairo",                 // Replace with the actual state (if applicable)
                    "zip" to "12345",                   // Replace with the actual zip code (if applicable)
                    "street" to "123 Example St"       // Replace with the actual street address
                )
                // Convert the Map to JSONObject
                val billingData = JSONObject(billingDataMap)

                // Step 2: Create Order
                PaymobService.createOrder(token, totalPrice, billingData,
                    onSuccess = { orderId ->
                        // Step 3: Create Payment Key
                        PaymobService.createPaymentKey(token, orderId, totalPrice,
                            onSuccess = { paymentKey ->
                                runOnUiThread {
                                    openPaymentWebView(paymentKey)
                                }
                            },
                            onFailure = { error ->
                                runOnUiThread {
                                    Toast.makeText(this, "Failed to create payment key", Toast.LENGTH_SHORT).show()
                                    Log.e("PaymentActivity", "Error: $error")
                                }
                            }
                        )
                    },
                    onFailure = { error ->
                        runOnUiThread {
                            Toast.makeText(this, "Failed to create order", Toast.LENGTH_SHORT).show()
                            Log.e("PaymentActivity", "Error: $error")
                        }
                    }
                )
            },
            onFailure = { error ->
                runOnUiThread {
                    Toast.makeText(this, "Failed to authenticate", Toast.LENGTH_SHORT).show()
                    Log.e("PaymentActivity", "Error: $error")
                }
            }
        )
    }

    ////////////////////////////////2
    private fun openPaymentWebView(paymentKey: String) {
        val webView = findViewById<WebView>(R.id.paymentWebView)
        webView.settings.javaScriptEnabled = true
        webView.visibility = View.VISIBLE // Make the WebView visible

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // Handle payment success or failure based on the URL
                if (url.contains("success_url")) {
                    Toast.makeText(this@Payment, "Payment Successful!", Toast.LENGTH_SHORT).show()
                    finish() // Close activity or handle post-payment actions
                } else if (url.contains("failure_url")) {
                    Toast.makeText(this@Payment, "Payment Failed.", Toast.LENGTH_SHORT).show()


                }
            }
        }
        // Redirect user to the Paymob payment page
        val checkoutUrl = "https://accept.paymobsolutions.com/api/acceptance/iframes/875187?payment_token=$paymentKey"
        webView.loadUrl(checkoutUrl)

    }

    private fun launchPaymentInBrowser(paymentKey: String) {
        val checkoutUrl = "https://accept.paymob.com/api/acceptance/iframes/875188?payment_token={payment_key_obtained_previously}"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(checkoutUrl))
        startActivity(browserIntent)
    }
}