package br.com.example.maratonasamsung.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.modoInterativo.RoomTypekFragment
import br.com.example.maratonasamsung.tutoriaisRegras.TutorialActivity


class MainActivity : AppCompatActivity() {

    var navController: NavController? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
//    val fragment = RoomTypekFragment()
//    lateinit var myString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("br.com.example.maratonasamsung", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme)
            editor.putBoolean("temaEscuroOn", true)
            editor.commit()
        } else {
            setTheme(R.style.AppTheme)
            editor.putBoolean("temaEscuroOn", false)
            editor.commit()
        }

        setContentView(R.layout.main_activity)
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent(this, TutorialActivity::class.java)
//        intent.putExtra("param1", "qualquerCoisaServe")

//        val sessionId = getIntent().getStringExtra("param2")

        if (sharedPreferences.getBoolean("firstRun", true)) {
        //if(!::myString.isInitialized){
            startActivity(intent)
            //editor = sharedPreferences.edit()
            editor.putBoolean("firstRun", false).commit()

//            myString = "firstRunTrue"
//            Log.d("euaqui1",myString)
        }
/*        else if(sessionId != null){
                myString = "firstRunTrue"
            Log.d("euaqui2",myString)
        }
        else {
            myString = "qualquerCoisaServe"
            Log.d("euaqui3",myString)
        }
    }

    fun getMyData(): String {
        return myString
    }

    fun editData(string: String) {
        myString = string
*/
    }
}