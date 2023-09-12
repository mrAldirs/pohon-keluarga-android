package com.example.pohon_keluarga.Reuni

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityReuniInsertBinding
import org.json.JSONObject
import java.util.Calendar

class ReuniInsertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReuniInsertBinding
    private lateinit var kdPenggunaList: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>
    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReuniInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Buat Undangan Reuni")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        kdPenggunaList = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, kdPenggunaList)
        binding.listView.adapter = adapter

        binding.insJam.setOnClickListener {
            showTimePickerDialog()
        }

        binding.insTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnPilih.setOnClickListener {
            cekAkun()
        }

        binding.btnSubmit.setOnClickListener {
            kirimUndangan()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                binding.insJam.setText(time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                binding.insTanggal.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun cekAkun() {
        val request = object : StringRequest(
            Method.POST, urlClass.urlReuni,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                val username = jsonObject.getString("username")

                if (respon.equals("1")) {
                    dialog(username)
                    binding.insPengguna.setText("")
                } else if (respon.equals("2")) {
                    Toast.makeText(this, "Akun Tidak terdaftar pada Aplikasi!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()

                hm.put("mode","cek_akun")
                hm.put("username", binding.insPengguna.text.toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun dialog(username: String) {
        AlertDialog.Builder(this)
            .setTitle("Info!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin mengirim undangan ke $username?")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                kdPenggunaList.add(username)
                adapter.notifyDataSetChanged()
            })
            .setNegativeButton("Batal") { dialog, which ->
                dialog.cancel()
            }
        .show()
    }

    private fun kirimUndangan() {
        val request = object : StringRequest(
            Method.POST, urlClass.insertReuni,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil mengirim undangan reuni!", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {

            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                val kdPenggunaArray = kdPenggunaList.joinToString(",")

                hm.put("pengguna", kdPenggunaArray)
                hm.put("pesan", binding.insPesan.text.toString())
                hm.put("jam_reuni", binding.insJam.text.toString())
                hm.put("tgl_reuni", binding.insTanggal.text.toString())
                hm.put("tempat", binding.insTempat.text.toString())
                hm.put("kd_pengirim", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}