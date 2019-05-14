package io.codelabs.zenitech.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.codelabs.zenitech.R

class MainPagerAdapter(
    private val context: Context,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    enum class MainFragments(val titleRes: Int) {
        SHOP(R.string.tab_title_shop),
        HISTORY(R.string.tab_title_history),
        CART(R.string.tab_title_cart)
    }

    override fun getItem(position: Int): Fragment {
        return when (getItemType(position)) {
            MainFragments.SHOP -> ShopFragment()
            MainFragments.HISTORY -> HistoryFragment()
            MainFragments.CART -> CartFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(getItemType(position).titleRes)
    }

    private fun getItemType(position: Int): MainFragments {
        return MainFragments.values()[position]
    }

    override fun getCount(): Int = MainFragments.values().size
}