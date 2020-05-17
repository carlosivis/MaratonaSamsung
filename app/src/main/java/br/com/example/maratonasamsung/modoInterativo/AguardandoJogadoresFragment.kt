package br.com.example.maratonasamsung.modoInterativo

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.StatusBoolean
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import kotlinx.android.synthetic.main.fragment_aguardando_jogadores.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class AguardandoJogadoresFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    val timerJogadores = Timer()
    var clicavel = true
    var quantidadeJogadores = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aguardando_jogadores, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jogador = requireArguments().getString("jogador_nome").toString()
        val id_sessao = requireArguments().getInt("id_sessao")

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        timerJogadores.cancel()
                        timerJogadores.purge()
                        jogadorEncerrar(id_sessao, jogador)
                        navController!!.navigate(R.id.action_aguardandoJogadoresFragment_to_mainFragment)
                    }
                    .setNegativeButton(R.string.cancelar) { dialog, which -> }
                    .show()
            }
        }
        callback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnAguardandoJogadores).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)

        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.aguardando_jogadores_title)
                .setMessage(R.string.aguardando_jogadores_message)
                .setPositiveButton(R.string.ok) { dialog, which ->  }
                .show()
        }

        val sala_nome = requireArguments().getString("sala_nome").toString()
        val sala_senha = requireArguments().getString("sala_senha").toString()
        val id_sessao = requireArguments().getInt("id_sessao")

        aguardandoJogadoresProgressBar.visibility = View.INVISIBLE

        aguardandoJogadoresTxtNome.text = "Nome: $sala_nome"
        aguardandoJogadoresTxtSenha.text = "Senha: $sala_senha"

        jogadores(id_sessao)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_back -> activity?.onBackPressed()
            R.id.btnAguardandoJogadores -> {

                if(clicavel) {
                    val id_sessao = requireArguments().getInt("id_sessao")

                    if(quantidadeJogadores > 1) {
                        btnAguardandoJogadores.setText("")
                        aguardandoJogadoresProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        comecarPartida(id_sessao)
                    }
                    else {
                        val texto = "É necessário pelo menos duas pessoas para começar a jogar!"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                }
            }
        }
    }

    private fun configureRecyclerViewRanking(list: RankingResponse) {
        val rankingAdapter = RankingAdapter(list)
        recyclerRanking.apply {
            layoutManager = LinearLayoutManager(context)
            isComputingLayout
            adapter= rankingAdapter
            onPause()
            onCancelPendingInputEvents()
        }
    }

    fun jogadores(id_sessao: Int){
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
                        val texto = "Erro ao atualizar ranking"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        configureRecyclerViewRanking(jogadores)

                        val quantidade: ArrayList<String> = arrayListOf("")
                        jogadores.jogadores.forEach { quantidade.add((it.nome)) }

                        quantidade.removeAt(0)
                        quantidadeJogadores = quantidade.size
                    }
                }
                else {
                    Log.d("Erro banco: jogadores", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerJogadores.schedule(5000){
            jogadores(id_sessao)
        }
    }

    fun comecarPartida(id_sessao: Int) {
        Service.retrofit.comecarPartida(
            id_sessao = id_sessao
        ).enqueue(object : Callback<StatusBoolean> {
            override fun onFailure(call: Call<StatusBoolean>, t: Throwable) {
                Log.d("Ruim: Começar Partida", t.toString())
            }
            override fun onResponse(call: Call<StatusBoolean>, response: Response<StatusBoolean>) {
                Log.d("Bom: Começar Partida", response.body().toString())

                if (response.isSuccessful) {
                    timerJogadores.cancel()
                    timerJogadores.purge()

                    val jogador = requireArguments().getString("jogador_nome").toString()
                    val doencas = requireArguments().getStringArrayList("doencas")

                    val parametros = Bundle()
                    parametros.putInt("id_sessao", id_sessao)
                    parametros.putString("jogador_nome", jogador)
                    parametros.putStringArrayList("doencas", doencas)

                    navController!!.navigate(R.id.action_aguardandoJogadoresFragment_to_roomDiqueiroDoencaFragment, parametros)
                }
                else {
                    Log.d("Erro banco: ComeçarPart", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    aguardandoJogadoresProgressBar.visibility = View.INVISIBLE;
                    btnAguardandoJogadores.setText(R.string.btn_comecar)
                }
            }
        })
    }

    fun jogadorEncerrar(id_sessao: Int, jogador: String) {
        Service.retrofit.jogadorEncerrar(
            jogador = JogadorRequest(
                id_sessao = id_sessao,
                nome = jogador
            )
        ).enqueue(object : Callback<StatusBoolean> {
            override fun onFailure(call: Call<StatusBoolean>, t: Throwable) {
                Log.d("Ruim: Jogador Encerrar", t.toString())
            }

            override fun onResponse(call: Call<StatusBoolean>, response: Response<StatusBoolean>) {
                Log.d("Bom: Jogador Encerrar", response.body().toString())

                if (response.code() == 500) {
                    Log.d("Erro banco: JogUpdate", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }
}
