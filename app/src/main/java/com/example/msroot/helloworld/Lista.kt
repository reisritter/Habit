package com.example.msroot.helloworld

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.app.ActivityManager.AppTask
import android.content.pm.PackageInfo
import android.R.attr.label
import android.view.LayoutInflater
import android.widget.*
import kotlinx.android.synthetic.main.item.view.*

class Lista : AppCompatActivity() {

    public lateinit var a:ArrayList<model>
    var lista_prog:MutableList<String>?=mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val listView = findViewById<ListView>(R.id.lista)
        var pm:PackageManager = this.getPackageManager()
        var pacotes: List<ApplicationInfo> = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        a = ArrayList()

        for(l:ApplicationInfo in pacotes)
        {
            var modelo:model=model()
            modelo.setName1(l.processName)
            modelo.i = l.loadIcon(packageManager)
            a.add(modelo)
        }

        var custom:Adapter? = Adapter(this,a)
        listView.adapter = custom
    }

    class Adapter(context:Context,val model:ArrayList<model>) : BaseAdapter()
    {

        private val vContext:Context
        //var lista_programas:MutableList<String>?= mutableListOf()
        companion object {lateinit var lista_programas:MutableList<String>}

        init
        {
            vContext=context
            lista_programas = mutableListOf("0")
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
