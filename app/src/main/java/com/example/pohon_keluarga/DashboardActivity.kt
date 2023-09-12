package com.example.pohon_keluarga

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.HakAkses.AksesSilsilahActivity
import com.example.pohon_keluarga.PageAdapter.PageSilsilahAdapter
import com.example.pohon_keluarga.Reuni.ReuniActivity
import com.example.pohon_keluarga.databinding.ActivityDashboardBinding
import com.example.pohon_keluarga.databinding.NavHeaderBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var b: ActivityDashboardBinding
    private lateinit var hb: NavHeaderBinding

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""
    val USERNAME = "username"
    val DEF_USERNAME = ""
    val PASSWORD = "password"
    val DEF_PASSWORD = ""
    val NAMA_MC = "nama_mc"
    val DEF_NAMA_MC = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDashboardBinding.inflate(layoutInflater)
        hb = NavHeaderBinding.bind(b.navView.getHeaderView(0))
        setContentView(b.root)
        supportActionBar?.setTitle("Dashboard")

        b.viewPager.adapter = PageSilsilahAdapter(supportFragmentManager)
        b.tabLayout.setupWithViewPager(b.viewPager)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        toggle = ActionBarDrawerToggle(this, b.drawerLayout, R.string.open, R.string.close)
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.btnSilsilahBaru.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, SilsilahInsertFragment()).commit()
            b.frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
            b.frameLayout.visibility = View.VISIBLE
        }

        b.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
                    b.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_undangan -> {
                    startActivity(Intent(this@DashboardActivity, ReuniActivity::class.java))
                }
                R.id.nav_bagikan -> {
                    startActivity(Intent(this@DashboardActivity, AksesSilsilahActivity::class.java))
                }
                R.id.nav_logout -> {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Logout")
                        .setMessage("Apakah Anda ingin keluar aplikasi?")
                        .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                            val prefEditor = preferences.edit()
                            prefEditor.putString(PENGGUNA,null)
                            prefEditor.putString(USERNAME,null)
                            prefEditor.putString(PASSWORD,null)
                            prefEditor.commit()

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        })
                        .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                        })
                        .show()
                    true
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (b.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            b.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
        jml("jumlah_keluarga")
    }

    private fun jml(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlKeluarga,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val jml = jsonObject.getString("jumlah")

                b.jumlahKeluarga.setText(jml+" Keluarga")
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "jumlah_keluarga" -> {
                        hm.put("mode","jumlah_keluarga")
                        hm.put("nama_mc", preferences.getString(NAMA_MC, DEF_NAMA_MC).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlModerator,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val nama = jsonObject.getString("nama_mc")
                val nohp = jsonObject.getString("nohp_mc")
                val foto = jsonObject.getString("foto")

                hb.usernameHeader.setText(nama)
                hb.nohpHeader.setText(nohp)
                Picasso.get().load(foto).into(hb.profilHeader)

                hb.profilHeader.setOnClickListener {
                    val intent = Intent(this, ImageDetailActivity::class.java)
                    intent.putExtra("img", foto)
                    startActivity(intent)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_profil" -> {
                        hm.put("mode","show_profil")
                        hm.put("kd_pengguna",preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}