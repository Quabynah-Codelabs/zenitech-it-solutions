package io.codelabs.zenitech.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.button.MaterialButton
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.sdk.util.intentTo
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)


        val adapter = AuthAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    class AuthAdapter(private val ctx: BaseActivity) : PagerAdapter() {
        private val inflater = ctx.layoutInflater

        @Nullable
        private var pageOne: View? = null
        @Nullable
        private var pageTwo: View? = null

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = PAGES

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
            container.removeView(`object` as? View)

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val v = getPage(container, position)
            container.addView(v)
            return v
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) ctx.getString(R.string.customer) else ctx.getString(R.string.store)
        }

        private fun getPage(container: ViewGroup, position: Int): View {
            return when (position) {
                0 -> {
                    if (pageOne == null) {
                        pageOne = inflater.inflate(R.layout.pager_auth, container, false)

                        val image = pageOne?.findViewById<ImageView>(R.id.image_container)
                        val btn = pageOne?.findViewById<MaterialButton>(R.id.auth_button)

                        GlideApp.with(ctx)
                            .asDrawable()
                            .load(R.drawable.customer)
                            .transition(withCrossFade())
                            .into(image!!)

                        btn?.text = ctx.getString(R.string.customer_auth_prompt)
                        btn?.setOnClickListener { ctx.intentTo(MainActivity::class.java, true) }
                    }
                    pageOne!!
                }

                else -> {
                    if (pageTwo == null) {
                        pageTwo = inflater.inflate(R.layout.pager_auth, container, false)

                        val image = pageTwo?.findViewById<ImageView>(R.id.image_container)
                        val btn = pageTwo?.findViewById<MaterialButton>(R.id.auth_button)

                        GlideApp.with(ctx)
                            .asDrawable()
                            .load(R.drawable.customer)
                            .transition(withCrossFade())
                            .into(image!!)

                        btn?.icon = ctx.resources.getDrawable(R.drawable.twotone_business_24px, ctx.theme)

                        btn?.text = ctx.getString(R.string.store_auth_prompt)
                        btn?.setOnClickListener { ctx.intentTo(MainActivity::class.java, true) }
                    }
                    pageTwo!!
                }

            }
        }

        companion object {
            private const val PAGES = 2
        }
    }
}