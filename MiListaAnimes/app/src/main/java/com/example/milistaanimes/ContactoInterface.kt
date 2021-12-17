package com.example.milistaanimes

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val base_user = "http://iesayala.ddns.net/AlejandroMoles/"

interface ContactoInterface {
    @GET("leerContactosJson.php/")
    fun contactoinformation(): Call<ContactoInfo>
}

object ContactoInstance {
    val contactoInterface: ContactoInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_user)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        contactoInterface = retrofit.create(ContactoInterface::class.java)
    }


}