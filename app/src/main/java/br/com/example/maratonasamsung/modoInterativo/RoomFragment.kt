package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_room.*


/**
 * A simple [Fragment] subclass.
 */
class RoomFragment :  Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false)
    }

    override fun onStart() {
        super<Fragment>.onStart()
        //textOla.text = "Olá, jogador@ $nomeJogador! Tente adivinhar a doença..."
    }

    fun selecionaDoenca(){
        var spinner = view?.findViewById<Spinner>(R.id.spinnerResposta)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Toast.makeText(this@RoomFragment,"Selecione uma doença",Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                resposta = parent!!.getItemAtPosition(position).toString()
            }
        }
    }
}
