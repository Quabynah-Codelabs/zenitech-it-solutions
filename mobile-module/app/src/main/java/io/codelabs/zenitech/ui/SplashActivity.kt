package io.codelabs.zenitech.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import io.codelabs.sdk.util.intentTo
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.databinding.ActivitySplashBinding
import org.jetbrains.anko.layoutInflater

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val adapter = OnBoardingAdapter(this)
        binding.pager.adapter = adapter
        binding.pager.pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
        binding.indicator.setViewPager(binding.pager)


    }

    fun navNext(view: View) =
        intentTo(if (prefs.isLoggedIn) HomeActivity::class.java else MainActivity::class.java, true)


    class OnBoardingAdapter(private val ctx: Context) : PagerAdapter() {
        private val inflater = ctx.layoutInflater

        @Nullable
        private var pageOne: View? = null
        @Nullable
        private var pageTwo: View? = null
        @Nullable
        private var pageThree: View? = null


        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = PAGES

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as? View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            return when (PAGES) {
                0 -> {
                    pageOne = inflater.inflate(R.layout.onboarding_pager, container, false)
                    pageOne!!
                }

                1 -> {
                    pageTwo = inflater.inflate(R.layout.onboarding_pager, container, false)
                    pageTwo!!
                }

                else -> {
                    pageThree = inflater.inflate(R.layout.onboarding_pager, container, false)
                    pageThree!!
                }
            }
        }

        companion object {
            private const val PAGES = 3
        }
    }

}