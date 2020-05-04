package br.com.example.maratonasamsung.modoInterativo

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

interface utils {
    fun ranking(int: Int){
        ranking(int)

    }
}