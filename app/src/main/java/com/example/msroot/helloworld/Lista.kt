package com.example.msroot.helloworld

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.app.ActivityManager.AppTask
import android.content.pm.PackageInfo
import android.R.attr.label
import android.R.attr.onClick
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.item.view.*
import android.app.AppOpsManager
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission

import android.support.v4.content.ContextCompat.getSystemService
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import java.util.*


class Lista : AppCompatActivity() {

    public lateinit var lista_programas_instalados:ArrayList<model>

    companion object {
        var status:Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        //COLOCO A TOOLBAR PERSONALIZADA NA ACTIVITY
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        //LISTA OS PROGRAMAS INSTALADOS
        val listView = findViewById<ListView>(R.id.lista)
        var pm:PackageManager = this.getPackageManager()
        var pacotes: List<ApplicationInfo> = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        lista_programas_instalados = ArrayList()

        //val newFragment = TimePicker()
        //newFragment.show(supportFragmentManager, "timePicker")


        for(l:ApplicationInfo in pacotes)
        {
            var modelo:model=model()
            modelo.setName1(l.processName)
            modelo.i = l.loadIcon(packageManager)
            lista_programas_instalados.add(modelo)
        }

        var custom:Adapter? = Adapter(this,lista_programas_instalados)
        listView.adapter = custom

    }



    fun p():Boolean
    {
        var granted = false
        val appOps = this.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), this.getPackageName())

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) === PackageManager.PERMISSION_GRANTED
        } else {
            granted = mode == AppOpsManager.MODE_ALLOWED
        }
        return granted
    }

    //COLOCA O MENU_LISTA NA TOOLBAR
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_lista, menu)
        return true
    }

    //AÇÃO PARA OS BOTÕES DA TOOLBAR
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_grava -> {

            if(Adapter.lista_programas.size>0)
            {
                if (!p())
                {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                 }
                else
                {
                    status = true
                    var tempo=0
                    //Escolher o tempo
                    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        val cal = Calendar.getInstance()

                        var horaAtual = if (cal.get(Calendar.HOUR_OF_DAY)==0) 24 else cal.get(Calendar.HOUR_OF_DAY)
                        var horaEscolhida = if(hour ==0)24 else hour

                        var t1=horaEscolhida*60+minute
                        var t2 = horaAtual*60+cal.get(Calendar.MINUTE)

                        if(t1 >= t2)
                            tempo=t1-t2
                        else tempo = 24*60-(t2-t1)

                        var horaAte = tempo/60
                        var minutoAte = tempo%60

                        tempo*=60

                        Toast.makeText(this,"Duração: "+horaAte.toString()+":"+minutoAte.toString(),Toast.LENGTH_SHORT).show()

                        val service : Intent = Intent(this,MyService::class.java)
                        service.putExtra("lista",Adapter.lista_programas as ArrayList<out String>)
                        startService(service)

                        object : CountDownTimer(tempo.toLong()*1000, 1000) {

                            override fun onTick(millisUntilFinished: Long) {
                                //Toast.makeText(this@Lista,"seconds remaining: " + (millisUntilFinished / 1000).toString(),Toast.LENGTH_SHORT).show()
                            }

                            override fun onFinish() {
                                stopService(service)
                            }
                        }.start()

                    }
                    TimePickerDialog(this, timeSetListener, 0, 0, true).show()
                }
            }
            else
            {
                val dlgAlert = AlertDialog.Builder(this)
                dlgAlert.setMessage("Selecione um programa")
                dlgAlert.setTitle("App Title")
                dlgAlert.setPositiveButton("OK", null)
                dlgAlert.setCancelable(true)
                dlgAlert.create().show()
                //dlgAlert.setPositiveButton("Ok", DialogInterface.OnClickListener()
            }

            true
        }

        else ->
        {
            super.onOptionsItemSelected(item)
        }
    }

    class Adapter(context:Context,val model:ArrayList<model>) : BaseAdapter()
    {

        private val vContext:Context
        companion object {
            lateinit var lista_programas:MutableList<String>
        }


        init
        {
            vContext=context
            lista_programas = mutableListOf()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder

            if (convertView == null) {
                holder = ViewHolder()
                val inflater = vContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.item, null, true)

                holder.tvname = convertView!!.findViewById(R.id.nome) as TextView
                holder.icon = convertView.findViewById(R.id.app_icon) as ImageView
                holder.status = convertView.findViewById(R.id.switch_id) as Switch

                convertView.tag = holder
            } else holder = convertView.tag as ViewHolder


            holder.tvname!!.setText(model[position].getName1())
            holder.icon!!.setImageDrawable(model[position].i);

            //QUANDO MUDA O STATUS DO SWITCH DO ITEM DA LISTA

            holder.status!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener
            (fun (buttonView: CompoundButton, isChecked: Boolean) {
                Toast.makeText(vContext,model[position].getName1(),Toast.LENGTH_SHORT).show()

                if (holder.status!!.isChecked)
                    lista_programas!!.add(model[position].getName1())
                else
                    if (lista_programas!!.contains(model[position].getName1()))
                        lista_programas!!.remove(model[position].getName1())

                }
                )
            )

            return convertView
        }

        fun getListaProg():MutableList<String>
        {
            return lista_programas!!
        }

        override fun getViewTypeCount(): Int {
            return count
        }

        override fun getItemViewType(position: Int): Int {

            return position
        }

        override fun getItem(position: Int): Any {
            return model[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return model.size
        }

    private inner class ViewHolder {

        var tvname: TextView? = null
        var tvnumber: TextView? = null
        var icon:ImageView? = null
        var status:Switch? = null

    }


    }
}
