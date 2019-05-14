package io.codelabs.zenitech.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.databinding.ActivityProductBinding

class ProductDetailsActivity : BaseActivity() {
    companion object {
        const val EXTRA_PRODUCT = "extra_product"
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        if (intent.hasExtra(EXTRA_PRODUCT)) {
            binding.product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
        } else if (intent.hasExtra(EXTRA_PRODUCT_ID)) {

        }
    }


}