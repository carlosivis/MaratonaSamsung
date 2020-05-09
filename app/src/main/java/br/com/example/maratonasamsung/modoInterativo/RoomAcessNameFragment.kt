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
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_acess_name.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomAcessNameFragment : Fragment(), View.OnClickListener {
    var navController: NavController? = null

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
                    val texto = "Preencha todos os campos obrigatórios"
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
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Nice", response.toString())
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
                    } else {
                        val doencas = arguments!!.getStringArrayList("doencas")

                        val parametros = Bundle()
                        parametros.putInt("id_sessao", id_sessao)
                        parametros.putString("jogador_nome", jogador.nome)
                        parametros.putStringArrayList("doencas", doencas)

                        navController!!.navigate(
                            R.id.action_roomAcessNameFragment_to_roomAdivinhadorFragment,
                            parametros
                        )
                    }
                }
                else
                    Log.d("Erro do banco", response.message())
            }
        })
    }
}