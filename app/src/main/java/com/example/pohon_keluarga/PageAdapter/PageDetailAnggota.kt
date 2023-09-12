package com.example.pohon_keluarga.PageAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pohon_keluarga.DetailAnggota.KontakDetailFragment
import com.example.pohon_keluarga.DetailAnggota.LainnyaDetailFragment
import com.example.pohon_keluarga.DetailAnggota.ProfilDetailFragment

class PageDetailAnggota (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return ProfilDetailFragment()
            }
            1 -> {
                return KontakDetailFragment()
            }
            2 -> {
                return LainnyaDetailFragment()
            }
            else -> {
                return ProfilDetailFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Profil"
            }
            1 -> {
                return "Kontak"
            }
            2 -> {
                return "Lainnya"
            }
        }
        return super.getPageTitle(position)
    }

}