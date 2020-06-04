package br.com.example.maratonasamsung.modoInterativo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.StatusBoolean
import kotlinx.android.synthetic.main.fragment_room_acess.*
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class RoomAcessFragment : Fragment(), View.OnClickListener {

    private var bool = false
    var navController: NavController? = null
    var clicavel = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_acess, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.acessBtnContinuar).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)

        acessProgressBar.visibility = View.INVISIBLE;
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_back -> activity?.onBackPressed()
            R.id.acessBtnContinuar -> {

                if(clicavel) {
                    if(acessEditNomeSala.text.toString() == "" || acessEditSenha.text.toString() == "") {
                        val texto = "Preencha todos os campos obrigatórios"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else if(acessEditNomeSala.text.toString() != "" && acessEditSenha.text.toString() != "") {
                        acessBtnContinuar.setText("")
                        acessProgressBar.visibility = View.VISIBLE
                        clicavel = false

                        acessarSala()
                    }
                }
            }
        }
    }

    fun acessarSala(){
        Service.retrofit.acessarSala(
            nome = acessEditNomeSala.text.toString()
        ).enqueue(object : Callback<SalaResponse>{
            override fun onFailure(call: Call<SalaResponse>, t: Throwable) {
                Log.d("Ruim: Acessar Sala", t.toString())
            }
            override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                Log.d("Bom: Acessar Sala", response.toString())

                if (response.isSuccessful) {
                    val sala = response.body()!!
                    if (sala.status) {
                        if (sala.senha == acessEditSenha.text.toString())
                            cadastrarSessao(sala.nome, sala.senha)
                        else {
                            val texto = "Senha inválida"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()
                            acessEditSenha.setText("")

                            clicavel = true
                            acessProgressBar.visibility = View.INVISIBLE
                            acessBtnContinuar.setText(R.string.btn_continuar)
                        }
                    }
                    else {
                        val texto = "Sala não encontrada"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                        acessEditNomeSala.setText("")
                        acessEditSenha.setText("")

                        clicavel = true
                        acessProgressBar.visibility = View.INVISIBLE;
                        acessBtnContinuar.setText(R.string.btn_continuar)
                    }
                }
                else {
                    Log.d("Erro banco: AcessarSala", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    acessProgressBar.visibility = View.INVISIBLE;
                    acessBtnContinuar.setText(R.string.btn_continuar)
                }
            }
        })
    }

    fun cadastrarSessao(nome: String, senha: String) {
        Service.retrofit.cadastrarSessao(
            sala = SalaRequest(
                nome = nome,
                senha = senha
            )
        ).enqueue(object : Callback<SalaResponse>{
            override fun onFailure(call: Call<SalaResponse>, t: Throwable) {
                Log.d("Ruim: Cadastrar Sessao", t.toString())
            }
            override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                Log.d("Bom: Cadastrar Sessao", response.toString())

                if (response.isSuccessful) {
                    val sessao = response.body()!!

                    var doencas: ArrayList<String> = arrayListOf("")
                    sessao.doencas.forEach { doencas.add((it.nome)) }

                    val parametros = Bundle()
                    parametros.putInt("id_sessao", sessao.id_sessao)
                    parametros.putStringArrayList("doencas", doencas)
                    parametros.putString("sala_nome", nome)
                    parametros.putString("sala_senha", senha)

                    jogadores(sessao.id_sessao, parametros)
                }
                else {
                    Log.d("Erro banco: CadSessao", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    acessProgressBar.visibility = View.INVISIBLE;
                    acessBtnContinuar.setText(R.string.btn_continuar)
                }
            }
        })
    }

    fun jogadores(id_sessao: Int, parametros: Bundle){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object : Callback<RankingResponse> {
            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.d("Ruim: jogadores", t.toString())
            }

            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                Log.d("Bom: jogadores", response.body().toString())

                if (response.isSuccessful) {
                    val jogadores = response.body()!!

                    if (!jogadores.status) {
                        val texto = "Erro ao pegar ranking"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()

                        clicavel = true
                        acessProgressBar.visibility = View.INVISIBLE;
                        acessBtnContinuar.setText(R.string.btn_continuar)
                    }
                    else {
                        val quantidadeJogadores: java.util.ArrayList<String> = arrayListOf("")
                        jogadores.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        if(quantidadeJogadores.isNotEmpty()) {
                            verificarPartida(id_sessao, parametros)
                        }
                        else {
                            val texto = "Sala desabilitada, acesse outra ou tente criar uma nova"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()

                            clicavel = true
                            acessProgressBar.visibility = View.INVISIBLE;
                            acessBtnContinuar.setText(R.string.btn_continuar)
                        }
                    }
                }
                else {
                    Log.d("Erro banco: jogadores", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    acessProgressBar.visibility = View.INVISIBLE;
                    acessBtnContinuar.setText(R.string.btn_continuar)
                }
            }
        })
    }
    fun verificarPartida(id_sessao: Int, bundle: Bundle) {
        Service.retrofit.verificarPartida(
            id_sessao = id_sessao
        ).enqueue(object : Callback<StatusBoolean> {
            override fun onFailure(call: Call<StatusBoolean>, t: Throwable) {
                Log.d("Ruim: Começar Partida", t.toString())
            }
            override fun onResponse(call: Call<StatusBoolean>, response: Response<StatusBoolean>) {
                Log.d("Bom: Começar Partida", response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.status == false)
                        navController!!.navigate(
                            R.id.action_roomAcessFragment_to_roomAcessNameFragment,
                            bundle
                        )
                    else {
                        clicavel = true
                        acessProgressBar.visibility = View.INVISIBLE;
                        acessBtnContinuar.setText(R.string.btn_continuar)
                        Toast.makeText(
                            context,
                            "A partida já começou, tente outra sala",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.d("Erro banco: ComeçarPart", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

}