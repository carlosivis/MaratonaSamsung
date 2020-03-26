package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity(), View.OnClickListener {

    var pageTitle: String = "nada"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        fragment_title.text = pageTitle

    }

    fun setTitle(title : String){
        pageTitle = title
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button2 -> startActivity(Intent(this@TutorialActivity, MainFragment::class.java))
        }
    }
}
