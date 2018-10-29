package com.example.msroot.helloworld

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import android.support.v4.os.HandlerCompat.postDelayed
import android.util.Log
import java.util.*

class MyService : Service() {

    var context: Context = this
    var handler: Handler? = null
    var runnable: Runnable? = null


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    //Processos em execução
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

        handler = Handler()
        runnable = Runnable {

                for (x in Lista.Adapter.lista_programas as MutableList)
                {
                    if(execucao()==x)
                    {
                        val startMain = Intent(Intent.ACTION_MAIN)
                        startMain.addCategory(Intent.CATEGORY_HOME)
                        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        this.startActivity(startMain)
                    }
                }

            handler!!.postDelayed(runnable, 100)
        }
        handler!!.postDelayed(runnable,100);
        super.onCreate()
    }

    override fun onDestroy() {
        Toast.makeText(this, "destroy", Toast.LENGTH_LONG).show();

        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}
