package com.example.formularioscompose.remote

import com.example.formularioscompose.remote.api.ProductoApi
import com.example.formularioscompose.remote.api.UsuarioApi
import com.example.formularioscompose.remote.api.FraseApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiClient {

    private const val BASE_URL_PRODUCTOS = "http://18.233.244.164:8081/"
    private const val BASE_URL_USUARIOS = "http://54.197.247.124:8082/"
    private const val BASE_URL_FRASES = "https://api.quotable.io/"

    // Cliente normal (validación SSL correcta) para tus microservicios
    private fun buildClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ========= Cliente inseguro SOLO para la API externa =========

    private fun buildUnsafeClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) { }

                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) { }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )

        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }

        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    // ========= APIs =========

    // Productos
    val productoApi: ProductoApi by lazy {
        buildRetrofit(BASE_URL_PRODUCTOS).create(ProductoApi::class.java)
    }

    // Usuarios
    val usuarioApi: UsuarioApi by lazy {
        buildRetrofit(BASE_URL_USUARIOS).create(UsuarioApi::class.java)
    }

    // Frases (API externa) usando cliente inseguro
    val fraseApi: FraseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_FRASES)
            .client(buildUnsafeClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FraseApi::class.java)
    }
}
