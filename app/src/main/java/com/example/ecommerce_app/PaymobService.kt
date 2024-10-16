package com.example.ecommerce_app

import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

//handle network calls and necessary methods to interact with Paymob’s API
object PaymobService {

    fun String.toMediaTypeOrNull(): MediaType? = this.toMediaType()


    // Function to get authentication token
    fun getAuthToken(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {

        val client = OkHttpClient()
        val jsonObject = JSONObject()
        jsonObject.put("api_key", "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmpiR0Z6Y3lJNklrMWxjbU5vWVc1MElpd2ljSEp2Wm1sc1pWOXdheUk2TVRBd01UQTROaXdpYm1GdFpTSTZJakUzTWpreE1URXpNVEl1TkRFd016YzJJbjAucXVzdVJmb0NTcDBjZTIwR0ZFX0U4R3VSMUFrTFgza01NQlhtZ1dqSHRMY0EwRXJNc0FPQXlGSy1ZbjVCS3loRF9nMWZiZDNoUnhRMy1ZWFQ3VkE5a1E=")

        // Function to get authentication token
        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, jsonObject.toString())

        val request = Request.Builder()
            .url("https://accept.paymobsolutions.com/api/auth/tokens")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                val jsonResponse = JSONObject(responseString)
                val token = jsonResponse.getString("token")
                onSuccess(token)
            }
        })
    }

    val billingData = JSONObject().apply {
        put("name", "John Doe")
        put("email", "john.doe@example.com")
        put("phone_number", "+201234567890")
        put("address", "123 Main St")
        put("city", "Cairo")
        put("state", "Cairo")
        put("country", "Egypt")
        put("postal_code", "12345")
    }

    // Function to create an order
    fun createOrder(authToken: String, amount: Double,billingData: JSONObject, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit) {
        val client = OkHttpClient()

        val jsonObject = JSONObject()
        jsonObject.put("auth_token", authToken)
        jsonObject.put("amount_cents", (amount * 100).toInt().toString())
        jsonObject.put("currency", "EGP")
        jsonObject.put("delivery_needed", false)
        jsonObject.put("items", JSONArray())

        // Adding billing data to the order request
        jsonObject.put("billing_data", billingData)

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, jsonObject.toString())

        val request = Request.Builder()
            .url("https://accept.paymobsolutions.com/api/ecommerce/orders")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                val jsonResponse = JSONObject(responseString)
                val orderId = jsonResponse.getInt("id")
                onSuccess(orderId)
            }
        })
    }

    // Function to create a payment key
    fun createPaymentKey(authToken: String, orderId: Int, amount: Double, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val client = OkHttpClient()
        val jsonObject = JSONObject()
        jsonObject.put("auth_token", authToken)
        jsonObject.put("amount_cents", (amount * 100).toInt().toString())
        jsonObject.put("order_id", orderId.toString())
        jsonObject.put("currency", "EGP")
        jsonObject.put("expiration", 3600)
        jsonObject.put("integration_id", "4856613")

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, jsonObject.toString())

        val request = Request.Builder()
            .url("https://accept.paymobsolutions.com/api/acceptance/payment_keys")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                val jsonResponse = JSONObject(responseString)
                val paymentKey = jsonResponse.getString("token")
                onSuccess(paymentKey)
            }
        })
    }
}