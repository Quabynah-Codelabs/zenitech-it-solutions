package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.databinding.FragmentShopBinding
import io.codelabs.zenitech.ui.recyclerview.ProductViewHolder
import kotlinx.android.synthetic.main.item_product.*

class ShopFragment : BaseFragment() {
    private lateinit var binding: FragmentShopBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = dataSourceOf()
        binding.productsGrid.setup {
            withDataSource(dataSource)
            withLayoutManager(LinearLayoutManager(requireContext()))
            withItem<Product>(R.layout.item_product) {
                onBind(::ProductViewHolder) { index, item ->
                    GlideApp.with(product_image.context)
                        .load(item.image)
                        .placeholder(R.drawable.sample_image)
                        .error(R.drawable.sample_image)
                        .into(product_image)
                }
            }
        }
    }

}