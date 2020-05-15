package br.com.example.maratonasamsung.modoInterativo

import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.StatusBoolean
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class ExpulsoSalaFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    var clicavel = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expulso_sala, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        jogadorEncerrar(id_sessao, jogador)
                    }
                    .setNegativeButton(R.string.cancelar) { dialog, which -> }
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.expulsoBtn).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.expulsoBtn -> {

                if(clicavel) {
                    val id_sessao = requireArguments().getInt("id_sessao")
                    val jogador = requireArguments().getString("jogador_nome").toString()

                    clicavel = false

                    jogadorEncerrar(id_sessao, jogador)
                }
            }
        }
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

                if (response.isSuccessful)
                    navController!!.navigate(R.id.action_expulsoSalaFragment_to_mainFragment)
                else {
                    Log.d("Erro banco: JogUpdate", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                }
            }
        })
    }
}
