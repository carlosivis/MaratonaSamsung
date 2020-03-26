package br.com.example.maratonasamsung.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialFragment : Fragment() {

    var pageTitle : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
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