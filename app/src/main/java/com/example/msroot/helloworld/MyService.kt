package com.example.msroot.helloworld

import android.app.ActivityManager
import android.app.Notification
import android.app.Notification.PRIORITY_MIN
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import android.support.v4.os.HandlerCompat.postDelayed
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class MyService : Service() {

    var context: Context = this
    var handler: Handler? = null
    var runnable: Runnable? = null

    var status:Boolean = true
    var lista_programa_bloqueio : MutableList<String> = mutableListOf()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    //Retorna o processo
     fun execucao():String
    {
        var currentApp = "NULL";
        var usm:UsageStatsManager  = this.context.getSystemService(Context.USAGE_STATS_SERVICE)as(UsageStatsManager);
        var time = System.currentTimeMillis();
        var appList : List<UsageStats>  = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size > 0) {
            var mySortedMap:SortedMap<Long, UsageStats>  = TreeMap<Long, UsageStats>();
            for (usageStats in appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey())!!.getPackageName();
            }
        }
        return currentApp
    }

    override fun onCreate() {

        startForeground()
        super.onCreate()
    }

    override fun onDestroy() {
        Toast.makeText(this, "destroy", Toast.LENGTH_LONG).show();
        status = false
        super.onDestroy()
    }

    private fun startForeground() {

        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = ""

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        startForeground(101, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var lista:ArrayList<String> = intent!!.getStringArrayListExtra("lista")
        Toast.makeText(this,lista.size.toString(),Toast.LENGTH_SHORT).show()
        handler = Handler()
        runnable = Runnable {

            if(status) {
                for (x in lista as ArrayList) {
                    if (execucao() == x) {
                        val startMain = Intent(Intent.ACTION_MAIN)
                        startMain.addCategory(Intent.CATEGORY_HOME)
                        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        this.startActivity(startMain)
                    }
                }
            }

            handler!!.postDelayed(runnable, 100)
        }
        handler!!.postDelayed(runnable,100);

        return START_REDELIVER_INTENT
    }
}
