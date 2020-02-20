package br.com.example.maratonasamsung


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 */
class RoomFragment : Fragment() {

    var opcoes: ArrayList<Int> = arrayListOf(1,2,3,4,5,6,7)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false)
    }

    override fun onStart() {
        super.onStart()

//        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item, opcoes)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
