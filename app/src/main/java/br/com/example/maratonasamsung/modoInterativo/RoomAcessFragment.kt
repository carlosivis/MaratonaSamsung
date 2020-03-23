package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R

class RoomAcessFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null

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
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.acessBtnContinuar -> {
//                listaSala()
                navController!!.navigate(R.id.action_roomAcessFragment_to_roomAcessNameFragment)
            }
        }
    }

//    fun listaSala(){
//        Service.retrofit.listaSala(
//            SalaResquest(
//
//                nome = acessEditNomeSala.text.toString(),
//                senha = acessEditSenha.text.toString()
//
//            )).enqueue(object : Callback<SalaResponse>{
//            override fun onFailure(call: Call<SalaResponse>, t: Throwable) {
//                Log.d("Deu ruim", t.toString())
//            }
//
//            override fun onResponse(call: Call<SalaResponse>, response: Response<SalaResponse>) {
//                Log.d("Nice", response.toString())
//                val sala = response.body()
//                val parametro = Bundle()
//                parametro.putInt("id", sala!!.id)
////                val intent = Intent(RoomAcessNameFragmentX)
////                intent.putExtras(parametro)
////
////                startActivity(intent)
//            }
//        })
//    }
}

