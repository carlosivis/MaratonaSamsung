package br.com.example.maratonasamsung.data.service

import android.content.Context
import android.view.View
import android.widget.Toast

class ErrorCases() {
    fun error(context: Context){
        Toast.makeText(context,"Falha de conexão\nTente Novamente!", Toast.LENGTH_SHORT).show()
    }
}