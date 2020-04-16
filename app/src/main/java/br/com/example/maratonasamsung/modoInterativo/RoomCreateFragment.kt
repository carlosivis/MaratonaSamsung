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
import br.com.example.maratonasamsung.model.Responses.Doenca
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponse
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


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
                if(createEditUsuario.text.toString() == "" || createEditNomeSala.text.toString() == "" || createEditSenha.text.toString() == "") {
                    val texto = "Preencha todos os campos obrigatórios"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else if(createEditUsuario.text.toString() != "" && createEditNomeSala.text.toString() != "" && createEditSenha.text.toString() != "")
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
                    val texto = "Nome da sala já existente"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                    createEditNomeSala.setText("")
                    createEditSenha.setText("")

                }
                else
                    cadastrarSessao(sala!!.nome, sala!!.senha)
            }
        })
    }

    fun cadastrarSessao(nome: String, senha: String) {
        Service.retrofit.cadastrarSessao(
            sala = SalaRequest(
                nome = nome,
                senha = senha

            )
        ).enqueue(object : Callback<SessaoResponse>{
            override fun onFailure(call: Call<SessaoResponse>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponse>, response: Response<SessaoResponse>) {
                Log.d("Nice", response.toString())

                val sessao = response.body()

                val doencas: ArrayList<String> = arrayListOf("")
                sessao?.doencas!!.forEach { doencas.add((it.nome)) }

                val parametros = Bundle()
                parametros.putInt("id", sessao.id_sessao)
                parametros.putStringArrayList("doencas", doencas)

                jogadorNovo(sessao.id_sessao, parametros)
            }
        })
    }

    fun jogadorNovo(id: Int, parametros: Bundle){
        Service.retrofit.jogadorNovo(
            jogador = JogadorRequest(
                id_sessao = id,
                nome = createEditUsuario.text.toString()
            )
        ).enqueue(object : Callback<JogadorResponse>{
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Nice", response.toString())

                navController!!.navigate(R.id.action_roomCreateFragment_to_roomDiqueiroDoencaFragment, parametros)
            }
        })
    }
}