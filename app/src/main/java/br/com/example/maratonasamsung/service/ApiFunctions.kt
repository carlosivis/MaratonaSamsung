package br.com.example.maratonasamsung.service

import android.util.Log
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.Sintoma
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun doencas() {
    Service.retrofit.jogador().enqueue(object : Callback<JogadorResponse> {
        override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
            Log.d("Deu ruim!!!", t.toString())
        }

        override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
            Log.d("Sucesso", response.body().toString())

        }
    })
}

fun sintomas() {
    Service.retrofit.sintomas().enqueue(object : Callback<Sintoma> {
        override fun onFailure(call: Call<Sintoma>, t: Throwable) {
            Log.d("Deu ruim!!!", t.toString())
        }

        override fun onResponse(call: Call<Sintoma>, response: Response<Sintoma>) {
            TODO("Not yet implemented")
        }
    })
}