package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.example.maratonasamsung.R

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var activity: Activity
    val firstRun = "Não entendo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("com.MaratonaSamsung", MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getBoolean(firstRun, true)) {
            editor = sharedPreferences.edit()
            editor.putBoolean(firstRun, false).commit()
            startActivity(Intent(this, TutorialActivity::class.java))
        }
    }
}
