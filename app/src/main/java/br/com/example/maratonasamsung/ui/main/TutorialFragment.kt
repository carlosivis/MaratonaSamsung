package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController

import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialFragment : Fragment(), View.OnClickListener {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    val firstRun = "whoknows"
    var navController: NavController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        sharedPreferences = this.getActivity()!!.getSharedPreferences("com.MaratonaSamsung", Context.MODE_PRIVATE)
        val view: View = inflater!!.inflate(R.layout.fragment_tutorial, container, false)
        val btn: Button = view.findViewById(R.id.btn_next)
        btn.setOnClickListener(this)

        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getBoolean(firstRun, true)) {
            editor = sharedPreferences.edit()
            editor.putBoolean(firstRun, false).commit()
        }
        else{
            Log.d("oi","deu")
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_next -> btn_next.text="deu"//navController!!.navigate(R.id.action_tutorialFragment_to_tutorialFragment2)
        }
    }

    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //fragment_title.text = pageTitle
    //}

    //fun setTitle(title : String){
    //    pageTitle = title
    //}
}
/*package br.com.example.maratonasamsung.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation

import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorialFragment : Fragment(), View.OnClickListener{

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        //view.findViewById<Button>(R.id.).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
          //  R.id.createBtnCriarSala -> navController!!.navigate(R.id.action_roomCreateFragment_to_roomFragment)
        }
    }
}

*/