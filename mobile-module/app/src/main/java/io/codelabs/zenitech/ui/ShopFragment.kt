package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.recyclerview.GridItemDividerDecoration
import io.codelabs.recyclerview.SlideInItemAnimator
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.FakeDataSource
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentShopBinding
import io.codelabs.zenitech.ui.recyclerview.ProductAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


        adapter = ProductAdapter(requireContext(), repository)
        binding.productsGrid.layoutManager = LinearLayoutManager(requireContext()) as RecyclerView.LayoutManager?
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
        // Kick off initial load process
        GlobalScope.launch {
            loadDataSource()
        }
    }

    private suspend fun loadDataSource() {
        delay(4000)
        uiScope.launch {
            adapter.addDataSource(FakeDataSource.loadProducts())
        }
    }

}