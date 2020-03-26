package br.com.example.maratonasamsung.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.example.maratonasamsung.R

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
