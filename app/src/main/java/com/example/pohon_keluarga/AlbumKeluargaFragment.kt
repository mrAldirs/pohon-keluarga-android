package com.example.pohon_keluarga

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.Adapter.AdapterAlbum
import com.example.pohon_keluarga.Pohon.PohonActivity
import com.example.pohon_keluarga.databinding.FragmentAlbumKeluargaBinding
import org.json.JSONArray
import org.json.JSONObject

class AlbumKeluargaFragment : Fragment() {
    lateinit var thisParent: PohonActivity
    lateinit var b: FragmentAlbumKeluargaBinding
    lateinit var v: View

    lateinit var urlClass: UrlClass
    val dataAlb = mutableListOf<HashMap<String,String>>()
    lateinit var albAdapter : AdapterAlbum

    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val NAMA_MC = "nama_mc"
    val DEF_NAMA_MC = ""

    var nm_pengguna = ""
    var nm_mc = ""

    var kdAl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PohonActivity
        b = FragmentAlbumKeluargaBinding.inflate(layoutInflater)
        v = b.root

        val bundle = arguments
        val kode = bundle?.getString("kode")

        preferences = v.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        nm_pengguna = preferences.getString(NAMA_MC, DEF_NAMA_MC).toString()

        urlClass = UrlClass()
        albAdapter = AdapterAlbum(dataAlb, this)
        b.rvAlbum.layoutManager = GridLayoutManager(v.context, 2)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        b.rvAlbum.addItemDecoration(itemDecoration)

        b.rvAlbum.adapter = albAdapter

        b.btnAlbumBaru.setOnClickListener {
            var frag = AlbumInsertFragment()
            var paket : Bundle? = thisParent.intent.extras
            var kode = paket?.getString("kode")

            val bundle = Bundle()
            bundle.putString("kode", kode)
            frag.arguments = bundle
            childFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, frag).commit()
            b.frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
            b.frameLayout.visibility = View.VISIBLE
        }

        b.refresh.setOnRefreshListener {
            showData("show_data_album")

            // Hentikan animasi refresh setelah selesai
            b.refresh.isRefreshing = false
            refreshData()
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        showData("show_data_album")
        validasi("validasi")
    }

    fun refreshData() {
        // Simulasi penundaan sebelum menghentikan animasi refresh
        Handler().postDelayed({
            showData("show_data_album")
            // Hentikan animasi refresh setelah selesai
            b.refresh.isRefreshing = false
        }, 2000) // Atur waktu penundaan dalam milidetik
    }

    private fun validasi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlModerator,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_mc")

                nm_mc = st1
                if (nm_pengguna == nm_mc) {
                    b.btnAlbumBaru.visibility = View.VISIBLE
                } else {
                    b.btnAlbumBaru.visibility = View.GONE
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val bundle = arguments
                when(mode){
                    "validasi" -> {
                        hm.put("mode","validasi")
                        hm.put("kd_keluarga", bundle?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun delete(mode: String){
        val request = object : StringRequest(
            Method.POST,urlClass.urlAlbum,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(v.context, "Berhasil menghapus Album!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode){
                    "delete"->{
                        hm.put("mode","delete")
                        hm.put("kd_album", kdAl)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    fun showData(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAlbum,
            Response.Listener { response ->
                dataAlb.clear()
                if (response.equals(0)) {
                    Toast.makeText(v.context,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("foto_album",jsonObject.getString("foto_album"))
                        frm.put("kd_album",jsonObject.getString("kd_album"))
                        frm.put("img_album",jsonObject.getString("img_album"))
                        frm.put("status_keluarga",jsonObject.getString("status_keluarga"))

                        if (jsonObject.length() >= 0) {
                            val nm = jsonObject.getString("nama_mc")
                            nm_mc = nm
                            if (nm_pengguna == nm_mc) {
                                b.btnAlbumBaru.visibility = View.VISIBLE
                            } else {
                                b.btnAlbumBaru.visibility = View.GONE
                            }
                        }

                        dataAlb.add(frm)
                    }
                    albAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(v.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                val bundle = arguments
                when(mode){
                    "show_data_album" -> {
                        hm.put("mode","show_data_album")
                        hm.put("kd_keluarga", bundle?.getString("kode").toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(v.context)
        queue.add(request)
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Mendapatkan posisi item
            val column = position % spanCount // Mendapatkan kolom saat ini

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 2) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 2) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}