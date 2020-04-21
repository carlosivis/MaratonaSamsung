package br.com.example.maratonasamsung.model.Responses

import android.os.IBinder
import android.os.Parcelable
import android.view.View
import java.io.Serializable

data class DoencasResponse(
    var agente: String,
    val id: Int,
    var nome: String,
    val prevencao: List<Prevencao>,
    val sintomas: List<Sintoma>,
    var tipo: String
):Serializable