package me.kasikaruppiah.miwok

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by ledlab on 8/13/17.
 */
class SimpleFragmentPageAdapter(val context: Context, val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0)
            return NumbersFragment()
        else if (position == 1)
            return FamilyFragment()
        else if (position == 2)
            return ColorsFragment()
        else
            return PhrasesFragment()
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (position == 0)
            return context.getString(R.string.category_numbers)
        else if (position == 1)
            return context.getString(R.string.category_family)
        else if (position == 2)
            return context.getString(R.string.category_colors)
        else
            return context.getString(R.string.category_phrases)
    }
}