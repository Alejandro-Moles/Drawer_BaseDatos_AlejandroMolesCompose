package com.example.milistaanimes

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val base_user2 = "http://iesayala.ddns.net/AlejandroMoles/"

interface ContactoFavoritoInterface {
    @GET("leerContactosFavoritosJson.php/")
    fun contactofavinformation(): Call<ContactoInfo>
}

object ContFavInstance {
    val contactoFavInterface: ContactoFavoritoInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_user2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        contactoFavInterface = retrofit.create(ContactoFavoritoInterface::class.java)
    }


}