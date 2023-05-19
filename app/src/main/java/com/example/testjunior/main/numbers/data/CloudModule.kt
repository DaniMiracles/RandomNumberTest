package com.example.testjunior.main.numbers.data

import com.example.testjunior.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

interface CloudModule {

    fun <T> service(clazz: Class<T>): T

    abstract class Abstract() : CloudModule {
        protected abstract val level: HttpLoggingInterceptor.Level
        protected open val baseUrl : String = "http://numbersapi.com/"

        override fun <T> service(clazz: Class<T>): T {
            val logging = HttpLoggingInterceptor().apply {
                setLevel(level)
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            return retrofit.create(clazz)
        }
    }

    class Realise() : Abstract(){
        override val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
    }

    class Debug() : Abstract(){
        override val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
    }

}