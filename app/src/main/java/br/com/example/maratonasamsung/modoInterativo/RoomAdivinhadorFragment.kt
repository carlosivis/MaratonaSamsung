package br.com.example.maratonasamsung.modoInterativo

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*


class RoomAdivinhadorFragment :  Fragment() {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_adivinhador, container, false)
//        preencheSpinner()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val array = arrayOf("A", "B", "C")
        context?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, array)
        }

        spinnerResposta.adapter = spinnerAdapter
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_0) {
////             this tells the framework to start tracking for
////             a long press and eventual key up.  it will only
////             do so if this is the first down (not a repeat).
//            event.startTracking()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

//    fun preencheSpinner(){
//
//           val builder: AlertDialog.Builder = Builder(context)
//            setContentView(R.layout.spinner_dialog);
//            var spinner = view?.findViewById<Spinner>(R.id.spinnerResposta)
//            val vetor = arrayOf("A", "B", "C")
//            val adapterOpcoes = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, vetor)
//            spinner!!.setAdapter(adapterOpcoes);
//
//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                //Toast.makeText(this@RoomFragment,"Selecione uma doença",Toast.LENGTH_LONG).show()
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                resposta = parent!!.getItemAtPosition(position).toString()
//            }
//        }
//        val spinner: Spinner = findViewById(R.id.spinner)
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//            context!!,
//            R.array.planets_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
//    }
}