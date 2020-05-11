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
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_acess_name.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RoomAcessNameFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    val parametros = Bundle()
    var rodada: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_acess_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.acessnameBtnAcessarSala).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.acessnameBtnAcessarSala -> {
                val id_sessao = requireArguments().getInt("id_sessao")

                if(acessnameEditUsuario.text.toString() == "") {
                    val texto = "Preencha o campos obrigatório"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else
                    jogadorNovo(id_sessao)
            }
        }
    }

    fun jogadorNovo(id_sessao: Int){
        Service.retrofit.jogadorNovo(
            jogador = JogadorRequest(
                id_sessao = id_sessao,
                nome = acessnameEditUsuario.text.toString()
            )
        ).enqueue(object : Callback<JogadorResponse> {
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Ruim: Jogador novo", t.toString())
            }
            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Bom: Jogador novo", response.toString())

                if (response.isSuccessful) {
                    val jogador = response.body()

                    if (!jogador!!.status) {
                        if (jogador.message == "Esse nome já existe") {
                            val texto = "Nome de usuário já existente nesta sala"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()
                            acessnameEditUsuario.setText("")
                        } else if (jogador.message == "Sessao já foi iniciada") {
                            val texto = "Rodada já iniciada, entrada não mais permitida"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()
                            acessnameEditUsuario.setText("")
                        }
                    }
                    else {
                        parametros.putString("jogador_nome", jogador.nome)
                        jogadores(id_sessao)
                    }
                }
                else {
                    Log.d("Erro banco: JogadorNovo", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun jogadores(id_sessao: Int){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object : Callback<RankingResponse> {
            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.d("Ruim: Jogadores", t.toString())
            }

            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                Log.d("Bom: Jogadores", response.body().toString())

                if (response.isSuccessful) {
                    val jogadores = response.body()!!


                        val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                        jogadores.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        val doencas = arguments!!.getStringArrayList("doencas")

                        parametros.putInt("id_sessao", id_sessao)
                        parametros.putStringArrayList("doencas", doencas)

                        if (quantidadeJogadores.size > 2) {
                            pegarRodada(id_sessao)
                            parametros.putInt("rodada", rodada)
                            navController!!.navigate(R.id.action_roomAcessNameFragment_to_aguardandoRodadaFragment, parametros)
                        }
                        else
                            navController!!.navigate(R.id.action_roomAcessNameFragment_to_roomAdivinhadorFragment, parametros)

                }
                else {
                    Log.d("Erro banco: Jogadores", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun pegarRodada(id_sessao: Int) {
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object : Callback<SessaoResponseListing> {
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Ruim: Pegar Rodada", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Bom: Pegar Rodada", response.toString())

                if (response.isSuccessful) {
                    val resposta = response.body()!!
                    rodada = resposta.sessao.rodada
                }
                else {
                    Log.d("Erro banco: PegarRodada", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }
}
