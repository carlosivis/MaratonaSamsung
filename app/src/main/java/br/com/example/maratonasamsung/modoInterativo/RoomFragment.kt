package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_room.*

/**
 * A simple [Fragment] subclass.
 */
class RoomFragment : Fragment() {

    var opcoes: ArrayList<Int> = arrayListOf(1, 2, 3, 4, 5, 6, 7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false)
        //var intent = intent
        //val nomeJogador = intent.getStringExtra("Nome")

        //textOla.text = "Olá, $nomeJogador, tente adivinhar a doença..."
    }

    override fun onStart() {
        super<Fragment>.onStart()

//        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item, opcoes)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super<Fragment>.onOptionsItemSelected(item)
    }
}
