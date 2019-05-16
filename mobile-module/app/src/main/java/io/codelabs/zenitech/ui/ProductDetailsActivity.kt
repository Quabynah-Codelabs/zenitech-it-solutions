package io.codelabs.zenitech.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.databinding.ActivityProductBinding

class ProductDetailsActivity : BaseActivity() {
    companion object {
        const val EXTRA_PRODUCT = "extra_product"
        const val EXTRA_PRODUCT_ID = "extra_product_id"
        const val EXTRA_PRODUCT_IN_CART = "extra_product_cart"
        const val EXTRA_PRODUCT_IN_FAV = "extra_product_fav"
    }

    private var addToCart: Boolean = false
    private var addToFav: Boolean = false
    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        if (intent.hasExtra(EXTRA_PRODUCT)) {
            addToCart = intent.getBooleanExtra(EXTRA_PRODUCT_IN_CART, false)
            addToFav = intent.getBooleanExtra(EXTRA_PRODUCT_IN_FAV, false)

            binding.product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
            /*Snackbar.make(
                binding.container,
                (binding.product as? Product)?.url ?: (binding.product as? Product)?.name!!,
                Snackbar.LENGTH_LONG
            ).show()*/
        } else if (intent.hasExtra(EXTRA_PRODUCT_ID)) {

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        val cartItem = menu?.findItem(R.id.menu_cart_add)
        val favItem = menu?.findItem(R.id.menu_fav)

        cartItem?.icon = resources.getDrawable(
            if (addToCart) R.drawable.twotone_remove_shopping_cart_24px
            else R.drawable.twotone_add_shopping_cart_24px, null
        )

        favItem?.icon = resources.getDrawable(
            if (addToFav) R.drawable.twotone_favorite_24px
            else R.drawable.twotone_favorite_border_24px, null
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_cart_add -> {
                addToCart = !addToCart
                invalidateOptionsMenu()

                if (addToCart) repository.addProduct(binding.product as Product) else repository.removeProduct(binding.product as Product)
            }
            R.id.menu_fav -> {
                addToFav = !addToFav
                invalidateOptionsMenu()

                //todo: wish list
//                if (addToFav) repository.addFavorite(binding.product as Product) else repository.removeFavorite(binding.product as Product)
            }
            R.id.menu_browse -> {
                launchUrl((binding.product as? Product)?.url)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchUrl(urlString: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlString)))
    }

}