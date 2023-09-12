package com.example.pohon_keluarga.DetailAnggota

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.ImageDetailActivity
import com.example.pohon_keluarga.PageAdapter.PageDetailAnggota
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityAnggotaDetailBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.HashMap

class DetailAnggotaActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val NAMA_MC = "nama_mc"
    val DEF_NAMA_MC = ""

    var nm_pengguna = ""

    lateinit var urlClass: UrlClass

    lateinit var b: ActivityAnggotaDetailBinding

    var kdA = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAnggotaDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Detail Anggota Keluarga")

        b.viewPager.adapter = PageDetailAnggota(supportFragmentManager)
        b.tabLayout.setupWithViewPager(b.viewPager)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        nm_pengguna = preferences.getString(NAMA_MC, DEF_NAMA_MC).toString()

        var paket : Bundle? = intent.extras
        kdA = paket?.getString("kdA").toString()

        b.detailImage.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", paket?.getString("img").toString())
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_profil")
    }

    private fun detailProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_anggota")
                val st6 = jsonObject.getString("img")

                b.detailNama.setText(st1)
                Picasso.get().load(st6).into(b.detailImage)
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "detail_profil" -> {
                        hm.put("mode","detail_profil")
                        hm.put("kd_anggota", paket?.getString("kdA").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}