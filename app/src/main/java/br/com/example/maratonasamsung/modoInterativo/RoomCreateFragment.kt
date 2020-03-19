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
import br.com.example.appacessibilidade.Service
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.SalaResquest
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import kotlinx.android.synthetic.main.fragment_room_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
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
        view.findViewById<Button>(R.id.btnCriarSala).setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnCriarSala -> navController!!.navigate(R.id.action_roomCreateFragment_to_roomFragment)
        }
    }


    fun criarSala(){
        Service.retrofit.criarSala(
            SalaResquest(
                nome = txtNomeSala.text.toString(),
              //  publica = btnPrivado.isChecked,
                senha = txtSenhaSala.text.toString(),
                publica = true
            )).enqueue(object : Callback<SalaResponse>{
            override fun onFailure(call: Call<SalaResponse>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
                Log.d("Nice", response.toString())
            }
        })
    }
}

