package com.example.pohon_keluarga.Pohon

import android.content.DialogInterface
import android.content.Intent
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
import com.example.pohon_keluarga.databinding.FragmentPohonDetailBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.HashMap

class PohonDetailFragment : DialogFragment() {
    private lateinit var binding: FragmentPohonDetailBinding
    lateinit var parent: PohonActivity
    lateinit var v: View
    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPohonDetailBinding.inflate(layoutInflater)
        v = binding.root
        parent = activity as PohonActivity

        urlClass = UrlClass()

        val kode = arguments?.get("kode").toString()
        val nodeId = arguments?.get("nodeId").toString()
        val status = arguments?.get("status").toString()
        val hakAkses = arguments?.get("akses").toString()

        if (status.equals("Wareng")) {
            binding.btnTambah.visibility = View.GONE
        } else {
            binding.btnTambah.visibility = View.VISIBLE
        }

        if (hakAkses.equals("akses")) {
            binding.btnHapus.visibility = View.GONE
        } else {
            binding.btnHapus.visibility = View.VISIBLE
        }

        binding.btnTambah.setOnClickListener {
            dismiss()
            val intent = Intent(v.context, PohonInsertActivity::class.java)
            intent.putExtra("nodeId", nodeId)
            intent.putExtra("kode", kode)
            intent.putExtra("status", status)
            v.context.startActivity(intent)
        }

        binding.btnHapus.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Hapus Anggota!")
                .setIcon(R.drawable.warning)
                .setMessage("Jika Anda menghapus anggota keluarga, maka akan menghapus anak-anaknya. Apakah Anda ingin menghapus anggota ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    delete(nodeId)
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        detail()
    }

    fun detail() {
        val request = object : StringRequest(
            Method.POST, urlClass.urlAnggota,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_anggota")
                val st2 = jsonObject.getString("img")
                val st3 = jsonObject.getString("jenkel")
                val st4 = jsonObject.getString("status_anggota")

                binding.dtNama.setText(st1)
                Picasso.get().load(st2).into(binding.dtImg)
                binding.dtJenkel.setText(st3)
                binding.dtStatusAnggota.setText(st4)
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode","detail")
                hm.put("kd_anggota", arguments?.get("nodeId").toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }

    private fun delete(kode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAnggota,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    Toast.makeText(this.context, "Berhasil menghapus anggota!", Toast.LENGTH_SHORT).show()
                    dismiss()
                    parent.recreate()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode","delete")
                hm.put("kd_anggota", kode)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}