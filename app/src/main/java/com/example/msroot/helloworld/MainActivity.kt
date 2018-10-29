package com.example.msroot.helloworld

import android.app.Activity
import android.app.ActivityManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.support.v4.content.ContextCompat.startActivity



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun intentFunc(a:Activity)
    {
        val intent = Intent(this,a::class.java)
        startActivity(intent)
    }

    fun click_LittlePrince(view:View)
    {
        intentFunc(LittlePrince())
    }

    fun click_Lista(view:View)
    {
        startService(Intent(this,MyService::class.java))
        intentFunc(Lista())
    }

    fun click_Stop(view:View)
    {
        stopService(Intent(this,MyService::class.java))
    }
}
