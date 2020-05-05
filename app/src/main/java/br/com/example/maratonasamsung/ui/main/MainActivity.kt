package br.com.example.maratonasamsung.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import br.com.example.maratonasamsung.R
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

        /*Caso precise colocar a toolbar de novo (colocar no xml):
        <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:theme="@style/ToolbarTheme"
        app:menu="@menu/menu" />
         */
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


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.txtDoencaNomeLista -> navController!!.navigate(R.id.action_chooseFragment_to_itemChooseFragment)
        }
    }

}
