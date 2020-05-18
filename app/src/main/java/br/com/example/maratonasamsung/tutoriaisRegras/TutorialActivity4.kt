package br.com.example.maratonasamsung.tutoriaisRegras

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.ui.main.MainActivity

class TutorialActivity4 : AppCompatActivity(), View.OnClickListener {

    val name:String = "whatever"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial4)
    }

    override fun onClick(v: View?) {

//        val sessionId = intent.getStringExtra("param1")
        val intent = Intent(this@TutorialActivity4, MainActivity::class.java)
//        if(sessionId != null)
//            intent.putExtra("param2", "qualquerCoisaServe")

        when (v!!.id) {
            R.id.btn_end -> startActivity(intent)
            R.id.btn_back -> startActivity(Intent(this@TutorialActivity4, TutorialActivity3::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
