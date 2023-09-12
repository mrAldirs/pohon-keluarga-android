package com.example.pohon_keluarga.Reuni

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.Adapter.AdapterReuniTerkirim
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.FragmentReuniTerkirimBinding
import org.json.JSONArray
import org.json.JSONException

class ReuniTerkirimFragment : Fragment() {
    private lateinit var binding: FragmentReuniTerkirimBinding
    lateinit var v: View

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass
    val dataList = mutableListOf<HashMap<String,String>>()
    lateinit var adapter : AdapterReuniTerkirim

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReuniTerkirimBinding.inflate(layoutInflater)
        v = binding.root

        urlClass = UrlClass()
        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        adapter = AdapterReuniTerkirim(dataList)
        binding.recyclerView.layoutManager = LinearLayoutManager(v.context)
        binding.recyclerView.adapter = adapter

        binding.btnTambah.setOnClickListener {
            startActivity(Intent(v.context, ReuniInsertActivity::class.java))
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        showUndangan()
    }

    private fun showUndangan() {
        val request = object : StringRequest(
            Method.POST,urlClass.urlReuni,
            Response.Listener { response ->
                dataList.clear()
                try {
                    val jsonArray = JSONArray(response)
                    if (jsonArray.length() == 0) {
                        binding.textView.visibility = View.VISIBLE
                    } else {
                        binding.textView.visibility = View.GONE
                        for (x in 0..(jsonArray.length()-1)){
                            val jsonObject = jsonArray.getJSONObject(x)
                            var  frm = HashMap<String,String>()
                            frm.put("jam_reuni",jsonObject.getString("jam_reuni"))
                            frm.put("tgl_reuni",jsonObject.getString("tgl_reuni"))
                            frm.put("pesan",jsonObject.getString("pesan"))
                            frm.put("tempat",jsonObject.getString("tempat"))
                            frm.put("value",jsonObject.getString("value"))

                            dataList.add(frm)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","show_data_terkirim")
                hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }
}