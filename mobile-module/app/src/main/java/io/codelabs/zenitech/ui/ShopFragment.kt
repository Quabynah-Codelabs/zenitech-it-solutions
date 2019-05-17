package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.codelabs.recyclerview.GridItemDividerDecoration
import io.codelabs.recyclerview.SlideInItemAnimator
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.network.Outcome
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentShopBinding
import io.codelabs.zenitech.ui.recyclerview.ProductAdapter
import org.koin.android.ext.android.inject

class ShopFragment : BaseFragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var adapter: ProductAdapter
    private val repository: ProductRepository by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ProductAdapter(requireContext(), repository, true)
        val lm = LinearLayoutManager(requireContext())
        binding.productsGrid.layoutManager = lm
        binding.productsGrid.setHasFixedSize(true)
        binding.productsGrid.addItemDecoration(
            GridItemDividerDecoration(
                requireContext(),
                R.dimen.divider_height,
                R.color.divider
            )
        )
        binding.productsGrid.itemAnimator = SlideInItemAnimator()
        binding.productsGrid.adapter = adapter
        /*binding.productsGrid.addOnScrollListener(object :
            InfiniteScrollListener(binding.productsGrid.layoutManager as LinearLayoutManager, adapter) {
            override fun onLoadMore() {
                debugLog("Loading more...")
                loadDataSource()
            }
        })
        */

        // Kick off initial load process
        loadDataSource()
    }

    private fun loadDataSource() {
        api.getDatabaseService().loadProducts().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Outcome.Success -> {
                    debugLog("Products loaded successfully")
                    adapter.addDataSource(it.data)
                }

                is Outcome.Progress -> {
                    debugLog("Products load in session")
                }

                is Outcome.Failure -> {
                    // todo: for testing
                    adapter.addDataSource(api.loadProducts())
                    debugLog("Failed to load products")
                }

                is Outcome.ApiError -> {
                    // todo: for testing
                    adapter.addDataSource(api.loadProducts())
                    debugLog("Error with current API")
                }
            }
        })
    }

}