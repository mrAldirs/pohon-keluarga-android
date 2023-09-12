package com.example.pohon_keluarga.DetailAnggota

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityEditLainnyaBinding
import org.json.JSONObject
import java.util.HashMap

class LainnyaEditActivity : AppCompatActivity() {
    private lateinit var b: ActivityEditLainnyaBinding
    lateinit var urlClass: UrlClass

    val pdSpinner = arrayOf("-- Pendidikan --", "Paud/TK", "SD/MI", "SMP/MTS", "SMA/SMK/MA", "D1/D2/D3", "S1", "S2", "S3")
    lateinit var adapterPd : ArrayAdapter<String>

    val kwnSpinner = arrayOf("-- Status Perkawinan --","Sudah Kawin","Belum Kawin")
    lateinit var adapterKwn : ArrayAdapter<String>

    val golSpinner = arrayOf("-- Golongan Darah --", "A", "B", "AB", "0")
    lateinit var adapterGol : ArrayAdapter<String>

    var kdA = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEditLainnyaBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Edit Profil Lainnya")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        kdA = paket?.getString("kode").toString()

        adapterPd = ArrayAdapter(this, android.R.layout.simple_list_item_1, pdSpinner)
        b.spPendidikan.adapter = adapterPd

        adapterKwn = ArrayAdapter(this, android.R.layout.simple_list_item_1, kwnSpinner)
        b.spStatusKawin.adapter = adapterKwn

        adapterGol = ArrayAdapter(this, android.R.layout.simple_list_item_1, golSpinner)
        b.spGolonganDarah.adapter = adapterGol

        b.btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Edit Profil Lainnya!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda yakin ingin mengedit profil ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editLainnya("edit_lainnya")
                    Toast.makeText(this, "Berhasil mengedit profil!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DetailAnggotaActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_lainnya")
        b.linearKontak.visibility = View.GONE
    }

    private fun detailProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("pendidikan")
                val st2 = jsonObject.getString("pekerjaan")
                val st3 = jsonObject.getString("status_kawin")
                val st4 = jsonObject.getString("golongan_darah")
                val st6 = jsonObject.getString("kd_anggota")
                val st9 = jsonObject.getString("nama_mc")

                if (!st2.equals("null")) {
                    b.edtPekerjaan.setText(st1)
                } else {
                    b.edtPekerjaan.setText("")
                }

                val spPositionPd = adapterPd.getPosition(st1)
                b.spPendidikan.setSelection(spPositionPd)

                val spPositionSa = adapterKwn.getPosition(st3)
                b.spStatusKawin.setSelection(spPositionSa)

                val spPositionGol = adapterGol.getPosition(st4)
                b.spGolonganDarah.setSelection(spPositionGol)
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail_lainnya" -> {
                        hm.put("mode","detail_lainnya")
                        hm.put("kd_anggota", kdA)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun editLainnya(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlProfil,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "edit_lainnya"->{
                        hm.put("mode","edit_lainnya")
                        hm.put("kd_anggota", kdA)
                        hm.put("pendidikan", b.spPendidikan.selectedItem.toString())
                        hm.put("pekerjaan", b.edtPekerjaan.text.toString())
                        hm.put("status_kawin", b.spStatusKawin.selectedItem.toString())
                        hm.put("golongan_darah", b.spGolonganDarah.selectedItem.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}