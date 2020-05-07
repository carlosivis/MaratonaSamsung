package br.com.example.maratonasamsung.ui.main

import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timer
import kotlin.coroutines.EmptyCoroutineContext

class MainFragment : Fragment(), View.OnClickListener,Chronometer.OnChronometerTickListener {

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnModoEstudo).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnModoInterativo).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnMais).setOnClickListener(this)
        teste()

        }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnModoInterativo -> navController!!.navigate(R.id.action_mainFragment_to_roomTypekFragment)
            R.id.btnModoEstudo -> navController!!.navigate(R.id.action_mainFragment_to_agentsFragment)
            R.id.btnMais -> {
                navController!!.navigate(R.id.action_mainFragment_to_moreFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairApp)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        activity?.finishAffinity()
                    }
                    .setNegativeButton(R.string.cancelar) { dialog, which -> }
                    .show()
            }
        }
        callback
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun teste(){
        testetempo.isCountDown= true
        testetempo.base = SystemClock.uptimeMillis()+10000
        testetempo.setOnChronometerTickListener(this)
        testetempo.start()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onChronometerTick(chronometer: Chronometer?) {
        Timer().schedule(10000){
            chronometer?.stop()
        }
    }
}

