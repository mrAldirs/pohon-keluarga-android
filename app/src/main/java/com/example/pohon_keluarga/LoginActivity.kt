package com.example.pohon_keluarga

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding

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
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()

        urlClass = UrlClass()

        b.btnMasuk.setOnClickListener {
            b.progressBar.visibility = View.VISIBLE
            if (!b.loginUsername.text.toString().equals("") && !b.loginPassword.text.toString().equals("")){
                validationAccount("login")
            }else{
                Toast.makeText(this,"Username atau Password tidak boleh kosong!", Toast.LENGTH_LONG).show()
                b.progressBar.visibility = View.GONE
            }
        }

        b.btnDaftarkan.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }
    }

    private fun validationAccount(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val level = jsonObject.getString("level")
                val kd = jsonObject.getString("kd_pengguna")
                val user = jsonObject.getString("username")
                val pass = jsonObject.getString("password")
                val nm = jsonObject.getString("nama_mc")
                if (level.equals("User")) {
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val prefEditor = preferences.edit()
                    prefEditor.putString(PENGGUNA, kd)
                    prefEditor.putString(USERNAME, user)
                    prefEditor.putString(PASSWORD, pass)
                    prefEditor.putString(NAMA_MC, nm)
                    prefEditor.commit()

                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.warning)
                        .setTitle("Peringatan!")
                        .setMessage("Username dan Password yang Anda masukkan salah!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            b.progressBar.visibility = View.GONE
                        })
                        .show()
                    true
                }
            },
            Response.ErrorListener { error ->
                AlertDialog.Builder(this)
                    .setTitle("Peringatan!")
                    .setMessage("Tidak dapat terhubung ke server")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        b.progressBar.visibility = View.GONE
                    })
                    .show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "login" -> {
                        hm.put("mode", "login")
                        hm.put("username", b.loginUsername.text.toString())
                        hm.put("password", b.loginPassword.text.toString())
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}