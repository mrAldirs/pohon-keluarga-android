package com.example.pohon_keluarga.Pohon

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pohon_keluarga.AlbumKeluargaFragment
import com.example.pohon_keluarga.AnggotaKeluargaFragment
import com.example.pohon_keluarga.R
import com.example.pohon_keluarga.UrlClass
import com.example.pohon_keluarga.databinding.ActivityPohonBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import de.blox.treeview.BaseTreeAdapter
import de.blox.treeview.TreeNode
import org.json.JSONArray
import org.json.JSONException

class PohonActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityPohonBinding
    private var nodeCount = 0
    private lateinit var adapter: BaseTreeAdapter<ViewHolder>
    lateinit var urlClass: UrlClass
    private lateinit var animator: ObjectAnimator
    private lateinit var handler: Handler
    private lateinit var stopAnimationRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPohonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Silsilah Keluarga")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()
        var paket : Bundle? = intent.extras
        supportActionBar?.setSubtitle(paket?.getString("nm").toString())

        val hak = paket?.getString("akses").toString()
        if (hak.equals("akses")) {
            binding.hakAkses.visibility = View.VISIBLE
        } else {
            binding.hakAkses.visibility = View.GONE
        }

        animator = ObjectAnimator.ofInt(binding.hakAkses, "textColor", 0xFF000000.toInt(), 0xFFFF0000.toInt())
        animator.duration = 5000
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE

        handler = Handler()
        stopAnimationRunnable = Runnable {
            animator.cancel()
            binding.hakAkses.visibility = View.GONE
        }

        binding.buttonTambah.setOnClickListener {
            val intent = Intent(this@PohonActivity, PohonInsertActivity::class.java)
            intent.putExtra("kode", paket?.getString("kode").toString())
            intent.putExtra("nodeId", "null")
            startActivity(intent)
        }

        adapter = object : BaseTreeAdapter<ViewHolder>(this, R.layout.node) {
            @NonNull
            override fun onCreateViewHolder(view: View): ViewHolder {
                return ViewHolder(view)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, data: Any, position: Int) {
                val node = data as Node
                viewHolder.mTextView.text = node.nama
                if (node.img.isNotEmpty()) {
                    Picasso.get().load(node.img).into(viewHolder.image)
                    viewHolder.image.visibility = View.VISIBLE
                } else {
                    viewHolder.image.visibility = View.GONE
                }
                viewHolder.setNodeId(node.id)
            }
        }
        binding.treeview.adapter = adapter

        binding.bottomNavigationViewKeluarga.setOnNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        animator.start()
        handler.postDelayed(stopAnimationRunnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        animator.cancel()
        handler.removeCallbacks(stopAnimationRunnable)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        getDataFromWebService(adapter)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menu_silsilah -> {
                supportActionBar?.setTitle("Silsilah Keluarga")
                binding.frameLayoutKeluarga.visibility = View.GONE
            }
            R.id.menu_anggota -> {
                supportActionBar?.setTitle("Anggota Keluarga")
                var frag = AnggotaKeluargaFragment()
                var paket : Bundle? = intent.extras
                var kode = paket?.getString("kode")

                val bundle = Bundle()
                bundle.putString("kode", kode)
                frag.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutKeluarga, frag).commit()
                binding.frameLayoutKeluarga.setBackgroundColor(Color.argb(255,255,255,255))
                binding.frameLayoutKeluarga.visibility = View.VISIBLE
            }
            R.id.menu_album -> {
                supportActionBar?.setTitle("Album Keluarga")
                var frag = AlbumKeluargaFragment()
                var paket : Bundle? = intent.extras
                var kode = paket?.getString("kode")

                val bundle = Bundle()
                bundle.putString("kode", kode)
                frag.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutKeluarga, frag).commit()
                binding.frameLayoutKeluarga.setBackgroundColor(Color.argb(255,255,255,255))
                binding.frameLayoutKeluarga.visibility = View.VISIBLE
            }
        }
        return true
    }

    private fun getDataFromWebService(adapter: BaseTreeAdapter<ViewHolder>) {
        val stringRequest = object : StringRequest(
            Method.POST, urlClass.urlAnggota,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    if (jsonArray.length() == 0) {
                        // Tidak ada data, tampilkan Toast dan sembunyikan pohon akar
                        binding.buttonTambah.visibility = View.VISIBLE
                        binding.treeview.visibility = View.GONE
                    } else {
                        binding.treeview.visibility = View.VISIBLE
                        binding.buttonTambah.visibility = View.GONE
                        val data = ArrayList<Node>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val id = jsonObject.getString("kd_anggota")
                            val nama = jsonObject.getString("nama_anggota")
                            val img = jsonObject.getString("img")
                            val idParent = jsonObject.getString("parent_id")

                            data.add(Node(id, nama, img, idParent))
                        }

                        // Mengatur data ke adapter dan menampilkan di treeView
                        val rootNode = createTreeView(data)
                        adapter.setRootNode(rootNode)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                var paket : Bundle? = intent.extras
                params["mode"] = "show_data_anggota"
                params["kd_keluarga"] = paket?.getString("kode").toString()

                return params
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }

    private class ViewHolder(view: View) : View.OnClickListener {
        val mTextView = view.findViewById<TextView>(R.id.textView)
        val image = view.findViewById<ImageView>(R.id.image_view)
        private var nodeId: String = ""

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            // Mengirim intent ke halaman selanjutnya dengan membawa data id
            val nodeId = nodeId
            val nama = mTextView.text.toString()
            val kode = (v.context as AppCompatActivity).intent.getStringExtra("kode")

            val frag = PohonDetailFragment()

            val bundle = Bundle()
            bundle.putString("kode", kode)
            bundle.putString("nodeId", nodeId)
            frag.arguments = bundle

            val fragmentManager = (v.context as AppCompatActivity).supportFragmentManager
            frag.show(fragmentManager, "pohon_detail")
        }

        fun setNodeId(id: String) {
            nodeId = id
        }
    }

    private fun createTreeView(data: ArrayList<Node>): TreeNode {
        val nodeMap = HashMap<String, TreeNode>()

        // Membuat semua node berdasarkan data dan memetakkannya ke dalam HashMap
        for (node in data) {
            val treeNode = TreeNode(node)
            nodeMap[node.id] = treeNode
        }

        // Membuat hubungan antara parent dan child node berdasarkan id_parent
        for (node in data) {
            val parentId = node.id_parent
            if (parentId != null && nodeMap.containsKey(parentId)) {
                val parent = nodeMap[parentId]
                val child = nodeMap[node.id]
                parent?.addChild(child)
            }
        }

        // Mencari dan mengembalikan root node (tanpa parent)
        var rootNode: TreeNode? = null
        for (node in data) {
            if (!nodeMap.containsKey(node.id_parent)) {
                rootNode = nodeMap[node.id]
                break
            }
        }

        return rootNode ?: TreeNode(Node("", "", "", ""))
    }
}
