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
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import kotlinx.android.synthetic.main.fragment_room_acess_name.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RoomAcessNameFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    val parametros = Bundle()
    var rodada: Int = 0
    var clicavel = true

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
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)

        acessNameProgressBar.visibility = View.INVISIBLE;
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_back -> activity?.onBackPressed()
            R.id.acessnameBtnAcessarSala -> {

                if(clicavel) {
                    val id_sessao = requireArguments().getInt("id_sessao")

                    if(acessnameEditUsuario.text.toString() == "") {
                        val texto = "Preencha o campos obrigatório"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        acessnameBtnAcessarSala.setText("")
                        acessNameProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        jogadorNovo(id_sessao)
                    }
                }
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

                            clicavel = true
                            acessNameProgressBar.visibility = View.INVISIBLE;
                            acessnameBtnAcessarSala.setText(R.string.btn_acessar)
                        }
                        else if (jogador.message == "Sessao já foi iniciada") {
                            val texto = "Jogo já começou, a entrada não é mais permitida"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()
                            acessnameEditUsuario.setText("")

                            clicavel = true
                            acessNameProgressBar.visibility = View.INVISIBLE;
                            acessnameBtnAcessarSala.setText(R.string.btn_acessar)
                        }
                    }
                    else {
                        val doencas = requireArguments().getStringArrayList("doencas")

                        val parametros = Bundle()
                        parametros.putInt("id_sessao", id_sessao)
                        parametros.putStringArrayList("doencas", doencas)
                        parametros.putString("jogador_nome", acessnameEditUsuario.text.toString())

                        navController!!.navigate(R.id.action_roomAcessNameFragment_to_aguardandoComecarFragment, parametros)
                    }
                }
                else {
                    Log.d("Erro banco: JogadorNovo", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    acessNameProgressBar.visibility = View.INVISIBLE;
                    acessnameBtnAcessarSala.setText(R.string.btn_acessar)
                }
            }
        })
    }
}
