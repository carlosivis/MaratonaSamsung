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
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import kotlinx.android.synthetic.main.fragment_room_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RoomCreateFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    val parametros = Bundle()
    var clicavel = true

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

        createProgressBar.visibility = View.INVISIBLE;
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.createBtnCriarSala -> {

                if(clicavel) {
                    if(createEditUsuario.text.toString() == "" || createEditNomeSala.text.toString() == "" || createEditSenha.text.toString() == "") {
                        val texto = "Preencha todos os campos obrigatórios"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else if(createEditUsuario.text.toString() != "" && createEditNomeSala.text.toString() != "" && createEditSenha.text.toString() != "") {
                        createBtnCriarSala.setText("")
                        createProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        criarSala()
                    }
                }
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
                Log.d("Ruim: Criar sala", t.toString())
            }
            override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                Log.d("Bom: Criar sala", response.body().toString())

                if (response.isSuccessful) {
                    val sala = response.body()!!

                    if (!sala.status) {
                        val texto = "Nome da sala já existente"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                        createEditNomeSala.setText("")
                        createEditSenha.setText("")

                        clicavel = true
                        createProgressBar.visibility = View.INVISIBLE;
                        createBtnCriarSala.setText(R.string.btn_criar)
                    }
                    else
                        cadastrarSessao(sala.nome, sala.senha)
                }
                else {
                    Log.d("Erro banco: Criar sala", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    createProgressBar.visibility = View.INVISIBLE;
                    createBtnCriarSala.setText(R.string.btn_criar)
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
                Log.d("Ruim: Cadastrar sessão", t.toString())
            }
            override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                Log.d("Bom: Cadastrar sessão", response.toString())

                if (response.isSuccessful) {
                    val sessao = response.body()!!

                    val doencas: ArrayList<String> = arrayListOf("")
                    sessao.doencas.forEach { doencas.add((it.nome)) }

                    parametros.putInt("id_sessao", sessao.id_sessao)
                    parametros.putString("jogador_nome", createEditUsuario.text.toString())
                    parametros.putStringArrayList("doencas", doencas)
                    parametros.putString("sala_nome", nome)
                    parametros.putString("sala_senha", senha)

                    jogadorNovo(sessao.id_sessao)
                }
                else {
                    Log.d("Erro banco: CadSessão", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    createProgressBar.visibility = View.INVISIBLE;
                    createBtnCriarSala.setText(R.string.btn_criar)
                }
            }
        })
    }

    fun jogadorNovo(id_sessao: Int){
        Service.retrofit.jogadorNovo(
            jogador = JogadorRequest(
                id_sessao = id_sessao,
                nome = createEditUsuario.text.toString()
            )
        ).enqueue(object : Callback<JogadorResponse>{
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Ruim: jogadorNovo", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Bom: jogadorNovo", response.toString())

                if (response.isSuccessful)
                    navController!!.navigate(R.id.action_roomCreateFragment_to_aguardandoJogadoresFragment, parametros)
                else {
                    Log.d("Erro banco; jogadorNovo", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    createProgressBar.visibility = View.INVISIBLE;
                    createBtnCriarSala.setText(R.string.btn_criar)
                }
            }
        })
    }
}