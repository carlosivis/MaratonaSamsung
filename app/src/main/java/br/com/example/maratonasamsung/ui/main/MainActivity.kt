package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var activity: Activity
    var name = "whatever"
    val firstRun = "whoknows"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("com.MaratonaSamsung", MODE_PRIVATE)
        var i:Intent = getIntent()
        //name = i.getStringExtra("name")
        beenHere(name)
    }

    override fun onResume() {
        super.onResume();
        val intent = Intent(this, TutorialActivity::class.java)
        startActivityForResult(intent, TUTORIAL_ACTIVITY_REQUEST_CODE)
        if (sharedPreferences.getBoolean(firstRun, true)) {
            startActivity(Intent(this, TutorialActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == request_code) {
            if (resultCode == Activity.RESULT_OK) {
                val value = data.getStringExtra("value")
                val cost = data.getStringExtra("cost")
                //handle value and cost.
            }
        }
    }

    fun beenHere(name: String){
        Log.d("euaqui","seupaunocu")
        //if (name.equals("kenny")) {
         //   Log.d("euaqui","seupaunocu2")
         //   editor = sharedPreferences.edit()
         //   editor.putBoolean(firstRun, false).commit()
        //}
    }
}

/*    lateinit var activity: Activity

    lateinit var preference: SharedPreferences
    val pref_show_intro = "llll"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //val activity1 = TutorialActivity()
        //val activity2 = TutorialActivity()

        //activity1.setTitle("tela 1")
        //activity2.setTitle("tela 2")

        activity = this
        preference = getSharedPreferences("TutorialFragment", Context.MODE_PRIVATE)
        if (preference.getBoolean(pref_show_intro, true)) {
            Log.d("euaqui","no primeiro")
            beenHere()
            startActivity(Intent(activity, TutorialActivity::class.java))
            finish()
        }else{
            Log.d("euaqui","no segundo")
            beenHere()
            startActivity(Intent(activity, TutorialActivity2::class.java))
            finish()
        }
    }

    fun beenHere(){
        Log.d("euaquiantes",pref_show_intro.toBoolean().toString())
        val editor = preference.edit()
        editor.putBoolean(pref_show_intro, true)
        editor.apply()
        //editor.apply()
        Log.d("euaqui",pref_show_intro.toBoolean().toString())
    }
}
*/

/*package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.example.maratonasamsung.R

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



    }

}
*/