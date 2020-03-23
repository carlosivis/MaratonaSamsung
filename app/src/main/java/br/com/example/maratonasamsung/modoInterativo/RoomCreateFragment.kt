package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.service.Service
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import kotlinx.android.synthetic.main.fragment_room_acess.*
import kotlinx.android.synthetic.main.fragment_room_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomCreateFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.createBtnCriarSala).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.createBtnCriarSala -> {
               criarSala()
//                jogador()
            }
        }
    }

    fun criarSala(){
        Service.retrofit.criarSala(
            sala = SalaRequest(
                "aaa",
                "sss"
                    )
        ).enqueue(object : Callback<SalaResponse>{
                override fun onFailure(call: Call<SalaResponse>, t: Throwable) {
                    Log.d("Deu ruim", t.toString())
                }

                override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                    Log.d("Nice", response.toString())
                    navController!!.navigate(R.id.action_roomCreateFragment_to_roomFragment)
                    val sala = response.body()
//                    jogadorNovo(sala!!.id)
                }
        })
    }

    fun jogadorNovo(id: Int){
        Service.retrofit.jogadorNovo(
            JogadorRequest(
                id_sessao = id,
                nome = createEditNomeSala.text.toString()
            )
        ).enqueue(object : Callback<JogadorResponse>{
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Nice", response.toString())
            }
        })
    }
}

