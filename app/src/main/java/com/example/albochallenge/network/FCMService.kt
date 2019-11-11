package com.example.albochallenge.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


data class NotificationBody(val body: String, val title: String)
data class Notification(val to: String, val notification: NotificationBody)

interface FCMEndpoints {

    @POST("fcm/send")
    fun send(@Body notification: Notification): Call<Int>

}

class FCMService(key: String) {
    private val httpClient by lazy {
        OkHttpClient.Builder().apply {
            val interceptor = Interceptor {
                val original = it.request()

                val request = original
                    .newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "key=${key}")
                    .method(original.method(), original.body())
                    .build()

                it.proceed(request)
            }

            addInterceptor(interceptor)
        }.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    private val client by lazy {
        retrofit.create(FCMEndpoints::class.java)
    }


    fun send(notification: Notification, success: (Int) -> Unit, failure: (String) -> Unit) {

        val call = client.send(notification)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.code() == 200 && response.body() != null) {
                    response.body()?.let {
                        success(it)
                    }
                } else {
                    failure("status code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                val message = t.localizedMessage ?: "Unknown error"
                failure(message)
            }
        })

    }
}

