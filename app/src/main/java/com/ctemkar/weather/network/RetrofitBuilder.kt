package com.ctemkar.weather.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    private const val BASE_URL = "https://www.metaweather.com/"
    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

/*
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                val response: Response = chain.proceed(request)

                // deal with the issues the way you need to
                if (response.code == 500) {
                    /*
                    startActivity(
                        Intent(
                            this@ErrorHandlingActivity,
                            ServerIsBrokenActivity::class.java
                        )
                    )
                     */
                    return response
                }
                return response
            }
        })
        .build()

 */
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}