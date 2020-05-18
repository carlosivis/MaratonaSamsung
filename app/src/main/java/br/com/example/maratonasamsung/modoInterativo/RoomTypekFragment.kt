package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.ui.main.MainActivity


class RoomTypekFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var firstRun: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_typek, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.typeBtnCriarSala).setOnClickListener(this)
        view.findViewById<Button>(R.id.typeBtnAcessarSala).setOnClickListener(this)
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)

        val mainActivity: MainActivity = activity as MainActivity
        firstRun = mainActivity.getMyData()

        if(firstRun.equals("firstRunTrue")){

            (activity as MainActivity).editData("qualquerCoisaServe")

            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(getString(R.string.interativo_title))
                    .setMessage(getString(R.string.interativo_msg))
                    .setPositiveButton(R.string.ok) { dialog, which ->  }
                    .show()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_back -> activity?.onBackPressed()
            R.id.typeBtnCriarSala -> navController!!.navigate(R.id.action_roomTypekFragment_to_roomCreateFragment)
            R.id.typeBtnAcessarSala -> navController!!.navigate(R.id.action_roomTypekFragment_to_roomAcessFragment)
        }
    }
}