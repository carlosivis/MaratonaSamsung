package br.com.example.maratonasamsung.model.Responses

import android.os.IBinder
import android.os.Parcelable
import java.io.Serializable

data class DoencasResponse(
    val agente: String,
    val id: Int,
    var nome: String,
    val prevencao: List<Prevencao>,
    val sintomas: List<Sintoma>,
    var tipo: String
) : Serializable