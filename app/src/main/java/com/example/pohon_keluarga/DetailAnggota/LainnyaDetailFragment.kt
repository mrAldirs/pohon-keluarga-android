package com.example.pohon_keluarga.DetailAnggota

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.FragmentDetailLainnyaBinding
import org.json.JSONObject
import java.util.HashMap

class LainnyaDetailFragment : Fragment() {
    lateinit var b : FragmentDetailLainnyaBinding
    lateinit var v : View
    lateinit var thisParent: DetailAnggotaActivity
    lateinit var urlClass: UrlClass

    var nm_mc = ""
    var kdA = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DetailAnggotaActivity
        b = FragmentDetailLainnyaBinding.inflate(layoutInflater)
        v = b.root

        urlClass = UrlClass()
        b.btnEdit.setOnClickListener {
            val intentDetail = Intent(it.context, LainnyaEditActivity::class.java)
            intentDetail.putExtra("kode", kdA)
            it.context.startActivity(intentDetail)
        }

        return v
    }

    override fun onStart() {
        super.onStart()
        detailProfil("detail_lainnya")
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

                kdA = st6
                nm_mc = st9
                if (nm_mc == thisParent.nm_pengguna) {
                    b.btnEdit.visibility = View.VISIBLE
                } else {
                    b.btnEdit.visibility = View.GONE
                }

                if (!st1.equals("null")) {
                    b.lainnyaPendidikan.setText(st1)
                } else {
                    b.lainnyaPendidikan.setText("<default>")
                }

                if (!st2.equals("null")) {
                    b.lainnyaPekerjaan.setText(st2)
                } else {
                    b.lainnyaPekerjaan.setText("<default>")
                }

                if (!st3.equals("null")) {
                    b.lainnyaKawin.setText(st3)
                } else {
                    b.lainnyaKawin.setText("<default>")
                }

                if (!st4.equals("null")) {
                    b.lainnyaGoldar.setText(st4)
                } else {
                    b.lainnyaGoldar.setText("<default>")
                }
            },
            Response.ErrorListener { error ->
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "detail_lainnya" -> {
                        hm.put("mode","detail_lainnya")
                        hm.put("kd_anggota", thisParent.kdA)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(thisParent)
        queue.add(request)
    }
}