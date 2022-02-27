package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timer.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var serviseIntent: Intent
    private lateinit var binding: ActivityMainBinding
    private var time = 0.0
    var timerStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviseIntent = Intent(applicationContext,TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.update))

        binding.btnStart.setOnClickListener { StartAndStop() }
        binding.brtnStop.setOnClickListener { StartAndStop() }
        binding.btnReset.setOnClickListener { reset() }


    }
    private fun reset(){
        stopTimer()
        time = 0.0
        binding.textView.text = getStringFromDouble(time)
    }
    private fun StartAndStop(){
        if (timerStarted){
            stopTimer()
        }else{
            startTimer()
        }
    }
    private fun startTimer() {
       serviseIntent.putExtra(TimerService.extraTime, time)
        startService(serviseIntent)
       timerStarted=true

    }

    private fun stopTimer() {
        serviseIntent.putExtra(TimerService.extraTime, time)
        stopService(serviseIntent)
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object: BroadcastReceiver(){

        override fun onReceive(context: Context?, i: Intent?) {
            time = i!!.getDoubleExtra(TimerService.extraTime, 0.0)
            binding.textView.text = getStringFromDouble(time)
        }

    }

    private fun getStringFromDouble(time: Double):String {
        val result = time.roundToInt()
        val hour = result % 86400 / 3600
        val minute = result % 86400 % 3600 / 60
        val second = result % 86400 % 3600  %60
        return makeTimeString(hour,minute,second)
    }

    private fun makeTimeString(hour: Int, minute: Int, second: Int) = String.format("%02d:%02d:%02d", hour,minute,second)
}