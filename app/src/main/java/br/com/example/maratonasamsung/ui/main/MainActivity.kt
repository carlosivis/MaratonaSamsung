package br.com.example.maratonasamsung.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.modoInterativo.RoomAdivinhadorFragment
import br.com.example.maratonasamsung.modoInterativo.RoomDiqueiroDicasFragment
import br.com.example.maratonasamsung.modoInterativo.RoomDiqueiroDoencaFragment
import br.com.example.maratonasamsung.tutoriaisRegras.TutorialActivity

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("faz diferenca", MODE_PRIVATE)

    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getBoolean("firstRun", true)) {
            startActivity(Intent(this, TutorialActivity::class.java))
            editor = sharedPreferences.edit()
            editor.putBoolean("firstRun", false).commit()
        }
    }
}
