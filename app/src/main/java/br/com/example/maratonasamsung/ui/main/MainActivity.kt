package br.com.example.maratonasamsung.ui.main

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import br.com.example.maratonasamsung.R


class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var activity: Activity
    val firstRun = "whoknows"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        sharedPreferences = getSharedPreferences("com.MaratonaSamsung", MODE_PRIVATE)

    }

    override fun onResume() {
        super.onResume();

        activity = this
        if (sharedPreferences.getBoolean(firstRun, true)) {
            startActivity(Intent(activity, TutorialActivity::class.java))

            editor = sharedPreferences.edit()
            editor.putBoolean(firstRun, false).commit()
        }else{
            //startActivity(Intent(activity, MainFragment::class.java))
        }
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