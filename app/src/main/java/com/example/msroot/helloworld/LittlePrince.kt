package com.example.msroot.helloworld

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.util.*

class LittlePrince : AppCompatActivity() {

    lateinit var text:TextView
    val frases = arrayOf("Tu te tornas eternamente responsável por aquilo que cativas",
            "A gente corre o risco de chorar um pouco quando se deixa cativar",
            "É preciso que eu suporte duas ou três larvas se quiser conhecer as borboletas",
            "É loucura odiar todas as rosas só porque uma te espetou",
            "Só se vê bem com o coração, o essencial é invisível aos olhos.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_little_prince)

        text=findViewById(R.id.txt_Texto)
    }
    var i = 0
    fun mudar(view:View)
    {

        text.setText(frases[i])
        i=if(i==4)0 else 1+i
    }
}
