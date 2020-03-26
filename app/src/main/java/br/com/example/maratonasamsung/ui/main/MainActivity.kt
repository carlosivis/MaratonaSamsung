package br.com.example.maratonasamsung.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.example.maratonasamsung.R

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val firstRun = "Não entendo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("com.MaratonaSamsung", MODE_PRIVATE)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getBoolean(firstRun, true)) {
            editor = sharedPreferences.edit()
            editor.putBoolean(firstRun, false).commit()
            startActivity(Intent(this, TutorialActivity::class.java))
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
}