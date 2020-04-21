package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.modoInterativo.RoomAdivinhadorFragment
import br.com.example.maratonasamsung.tutoriaisRegras.TutorialActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var navController: NavController? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("faz diferenca", MODE_PRIVATE)

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        toolbar.title = ""
//        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getBoolean("firstRun", true)) {
            startActivity(Intent(this, TutorialActivity::class.java))
            editor = sharedPreferences.edit()
            editor.putBoolean("firstRun", false).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuTutorial -> {
                startActivity(Intent(this, TutorialActivity::class.java))
                return true
            }
            R.id.menuConfiguracao -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val gameFragment = supportFragmentManager.findFragmentById(R.id.roomAdivinhadorFragment)
        if(gameFragment is RoomAdivinhadorFragment) {
            AlertDialog.Builder(this)
                .setTitle("Quer mesmo sair?")
                .setMessage("Ao aceitar você sairá da sala\n Tem certeza?")
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    Navigation.createNavigateOnClickListener(R.id.action_roomAdivinhadorFragment_to_mainFragment)
                    super.onBackPressed()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, which -> }
                .show()
        }
        else{
            super.onBackPressed()
        }
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.txtDoencaNomeLista -> navController!!.navigate(R.id.action_chooseFragment_to_itemChooseFragment)
        }
    }

}
