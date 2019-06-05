package io.codelabs.zenitech.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.toast
import io.codelabs.widget.BaselineGridTextView
import io.codelabs.widget.FourThreeImageView
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.databinding.ActivitySplashBinding
import io.codelabs.zenitech.databinding.ActivityWelcomeBinding
import org.jetbrains.anko.layoutInflater

class WelcomeActivity : BaseActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        val adapter = OnBoardingAdapter(this)
        binding.pager.adapter = adapter
        binding.pager.pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
        binding.indicator.setViewPager(binding.pager)

        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                TransitionManager.beginDelayedTransition(binding.container, AutoTransition())
                binding.skipButton.text = if (position == 2) getString(R.string.get_started) else getString(R.string.skip)
            }
        })

        debugLog("Show onboarding: ${prefs.isShowOnBoarding}")
    }

    fun navNext(view: View) =
        intentTo(if (prefs.isLoggedIn) HomeActivity::class.java else AuthActivity::class.java, true)

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

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
            container.removeView(`object` as? View)

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val v = getPage(container, position)
            container.addView(v)
            return v
        }

        private fun getPage(container: ViewGroup, position: Int): View {
            return when (position) {
                0 -> {
                    if (pageOne == null) {
                        pageOne = inflater.inflate(R.layout.onboarding_pager, container, false)

                        val root = pageOne?.findViewById<ViewGroup>(R.id.pager_container)
                        val title = pageOne?.findViewById<BaselineGridTextView>(R.id.title_pager)
                        val desc = pageOne?.findViewById<BaselineGridTextView>(R.id.desc_pager)
                        val image = pageOne?.findViewById<FourThreeImageView>(R.id.main_pager_image)


                    }
                    pageOne!!
                }

                1 -> {
                    if (pageTwo == null) {
                        pageTwo = inflater.inflate(R.layout.onboarding_pager, container, false)

                        val root = pageTwo?.findViewById<ViewGroup>(R.id.pager_container)
                        val title = pageTwo?.findViewById<BaselineGridTextView>(R.id.title_pager)
                        val desc = pageTwo?.findViewById<BaselineGridTextView>(R.id.desc_pager)
                        val image = pageTwo?.findViewById<FourThreeImageView>(R.id.main_pager_image)
                    }
                    pageTwo!!
                }

                else -> {
                    if (pageThree == null) {
                        pageThree = inflater.inflate(R.layout.onboarding_pager, container, false)

                        val root = pageThree?.findViewById<ViewGroup>(R.id.pager_container)
                        val title = pageThree?.findViewById<BaselineGridTextView>(R.id.title_pager)
                        val desc = pageThree?.findViewById<BaselineGridTextView>(R.id.desc_pager)
                        val image = pageThree?.findViewById<FourThreeImageView>(R.id.main_pager_image)
                    }
                    pageThree!!
                }
            }
        }

        companion object {
            private const val PAGES = 3
        }
    }

}