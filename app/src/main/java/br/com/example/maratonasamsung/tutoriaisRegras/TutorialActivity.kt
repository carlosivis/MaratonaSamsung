package br.com.example.maratonasamsung.tutoriaisRegras

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.example.maratonasamsung.R

class TutorialActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
    }

    override fun onClick(v: View?) {

        val sessionId = intent.getStringExtra("param1")
        val intent = Intent(this@TutorialActivity, TutorialActivity2::class.java)
        intent.putExtra("param1", sessionId)

        when (v!!.id) {
            R.id.btn_next -> startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
