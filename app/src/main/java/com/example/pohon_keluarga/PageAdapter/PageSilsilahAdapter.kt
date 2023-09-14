package com.example.pohon_keluarga.PageAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pohon_keluarga.SilsilahDibagikanFragment
import com.example.pohon_keluarga.SilsilahkuFragment

class PageSilsilahAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return SilsilahkuFragment()
            }
            1 -> {
                return SilsilahDibagikanFragment()
            }
            else -> {
                return SilsilahkuFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Silsilahku"
            }
            1 -> {
                return "Akses Dibagikan"
            }
        }
        return super.getPageTitle(position)
    }

}