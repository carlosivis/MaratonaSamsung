package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.modoInterativo.RoomFragment
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_next -> startActivity(Intent(this@TutorialActivity, TutorialActivity2::class.java))
            R.id.btn_skip -> startActivity(Intent(this@TutorialActivity, MainActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
