package com.example.pohon_keluarga.HakAkses

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.FragmentAksesBagikanBinding
import org.json.JSONObject

class AksesBagikanFragment : DialogFragment() {
    private lateinit var binding: FragmentAksesBagikanBinding
    lateinit var v: View

    lateinit var preferences: SharedPreferences

    val PREF_NAME = "akun"
    val PENGGUNA = "kd_pengguna"
    val DEF_PENGGUNA = ""

    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAksesBagikanBinding.inflate(layoutInflater)
        v = binding.root

        urlClass = UrlClass()
        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val kode = arguments?.get("kode").toString()
        val keluarga = arguments?.get("keluarga").toString()

        binding.insNamaKeluarga.setText(keluarga)

        binding.btnSubmit.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Hak Akses")
                .setIcon(R.drawable.warning)
                .setMessage("Memberikan hak akses silsilah keluarga Anda berarti pengguna lain dapat mengubah data keluarga Anda. Apakah Anda ingin memberikan hak Akses?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insertAkses(kode)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    dismiss()
                })
                .show()
        }

        return v
    }

    fun dialog(text: String) {
        AlertDialog.Builder(v.context)
            .setTitle("Peringatan!")
            .setIcon(R.drawable.warning)
            .setMessage(text)
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            .show()
    }

    private fun insertAkses(kode: String) {
        val request = object : StringRequest(
            Method.POST, urlClass.urlAkses,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    val teks = "Berhasil menambahkan hak akses ke pengguna lain!"
                    dialog(teks)
                    dismiss()
                } else if (respon.equals("2")) {
                    val teks = "Pengguna telah diberikan hak akses untuk silsilah ${binding.insNamaKeluarga.text.toString()}!"
                    dialog(teks)
                } else if (respon.equals("3")) {
                    val teks = "Tidak dapat memberikan hak akses status silsilah Anda dalam mode privat. Ubah ke mode umum terlebih dahulu untuk memberikan hak akses."
                    dialog(teks)
                } else {
                    val teks = "Pengguna tidak ditemukan. Pastikan Anda memasukkan username dan nomor handphone pengguna lain dengan benar!"
                    dialog(teks)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()

                hm.put("mode","insert_akses")
                hm.put("kd_pengguna", preferences.getString(PENGGUNA, DEF_PENGGUNA).toString())
                hm.put("kd_keluarga", kode)
                hm.put("username", binding.insUsername.text.toString())
                hm.put("nohp_mc", binding.insNoHp.text.toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }
}