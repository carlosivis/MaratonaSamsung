package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import br.com.example.maratonasamsung.service.Service
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
            }
        }
    }

    fun criarSala(){
        Service.retrofit.criarSala(
            sala = SalaRequest(
                nome = createEditNomeSala.text.toString(),
                senha = createEditSenha.text.toString()
            )
        ).enqueue(object : Callback<SalaResponse>{
                override fun onFailure(call: Call<SalaResponse>, t: Throwable) {
                    Log.d("Deu ruim", t.toString())
                }
                override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                    Log.d("Nice", response.body().toString())

                    val sala = response.body()

                    if(!sala!!.status) {
                        var texto = "Nome da sala já existente, digite outro"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                        createEditNomeSala.setText("")
                        createEditSenha.setText("")
                    }
                    else
                        jogadorNovo(sala!!.id)}
        })
    }

    fun jogadorNovo(id: Int){
        Service.retrofit.jogadorNovo(
            jogadorRequest = JogadorRequest(
                id_sessao = id,
                nome = createEditUsuario.text.toString()
            )
        ).enqueue(object : Callback<JogadorResponse>{
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Nice", response.toString())

                val jogador = response.body()

                if(!jogador!!.status) {
                    var texto = "Nome de usuário já existente nesta sala, digite outro"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                    createEditUsuario.setText("")
                }
                navController!!.navigate(R.id.action_roomCreateFragment_to_roomFragment)
            }
        })
    }
}

