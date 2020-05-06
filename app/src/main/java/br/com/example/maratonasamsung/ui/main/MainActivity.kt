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

class MainActivity : AppCompatActivity(), View.OnClickListener {
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

    override fun onBackPressed() {
        val gameFragment = supportFragmentManager.findFragmentById(R.id.roomAdivinhadorFragment)
        if(gameFragment is RoomAdivinhadorFragment) {
            AlertDialog.Builder(this)
                .setTitle(R.string.sairJogo)
                .setMessage(R.string.sairJogoPont)
                .setPositiveButton(R.string.sair) { dialog, which ->
                    navController!!.navigate(R.id.mainFragment)
                }
                .setNegativeButton(R.string.cancelar) { dialog, which -> }
                .show()
        }
        else if(gameFragment is RoomDiqueiroDoencaFragment) {
            AlertDialog.Builder(this)
                .setTitle(R.string.sairJogo)
                .setPositiveButton(R.string.sair) { dialog, which ->
                    navController!!.navigate(R.id.mainFragment)
                }
                .setNegativeButton(R.string.cancelar) { dialog, which -> }
                .show()
        }
        else if(gameFragment is RoomDiqueiroDicasFragment) {
            AlertDialog.Builder(this)
                .setTitle(R.string.sairJogo)
                .setMessage(R.string.sairJogoPont)
                .setPositiveButton(R.string.sair) { dialog, which ->
                    navController!!.navigate(R.id.mainFragment)
                }
                .setNegativeButton(R.string.cancelar) { dialog, which -> }
                .show()
        }
        else {
            super.onBackPressed()
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

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.txtDoencaNomeLista -> navController!!.navigate(R.id.action_chooseFragment_to_itemChooseFragment)
        }
    }

}
