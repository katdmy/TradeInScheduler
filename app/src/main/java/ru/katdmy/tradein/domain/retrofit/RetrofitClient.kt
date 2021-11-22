package ru.katdmy.tradein.domain.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val LocalServer = "http://192.168.3.247/Main/hs/tradein/"
    //private const val InternetServer = "http://mail.smkh.ru:777/Main/hs/tradein/"
    //private const val API_KEY = "37d023007af6569b99e1ba7cad35a94b"

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor { message -> Log.e("OkHttp", message) }.setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(LocalServer)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val smkhApi: SmkhApi = retrofit.create()
}