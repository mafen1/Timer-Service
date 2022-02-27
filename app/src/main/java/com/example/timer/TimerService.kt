package com.example.timer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class TimerService : Service() {

    companion object {
        val extraTime = "TIME"
        val update = "UPDATE"
    }
    override fun onBind(intent: Intent): IBinder? = null

    private val timer = Timer()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val time = intent!!.getDoubleExtra(extraTime, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)

        return START_NOT_STICKY
    }
    private inner class TimeTask(private var time:Double): TimerTask(){

        override fun run() {
            val i = Intent(update)
            time++
            i.putExtra(extraTime,time)
            sendBroadcast(i)

        }

    }

}