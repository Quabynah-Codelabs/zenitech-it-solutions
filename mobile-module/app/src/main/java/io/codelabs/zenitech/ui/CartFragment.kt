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
import io.codelabs.sdk.util.debugLog
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.PRODUCT_VM
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.datasource.viewmodel.ProductViewModel
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentCartBinding
import io.codelabs.zenitech.ui.recyclerview.CartAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.viewModel

class CartFragment : BaseFragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartAdapter
    private val repository: ProductRepository by inject()
    private val productViewModel: ProductViewModel by viewModel(PRODUCT_VM)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(requireContext(), repository, object : CartAdapter.ButtonStateListener {
            override fun updateButtonState(state: Boolean, price: Double) {
                binding.checkout.visibility = if (state) View.VISIBLE else View.GONE
                binding.checkout.text = String.format("Checkout GHC%.2f", price)

                binding.checkout.setOnClickListener {
                    //todo: checkout with pay pal here
                }
            }
        })

        binding.grid.adapter = adapter
        binding.grid.layoutManager = LinearLayoutManager(requireContext()) as RecyclerView.LayoutManager?
        binding.grid.addItemDecoration(
            GridItemDividerDecoration(
                requireContext(),
                R.dimen.divider_height,
                R.color.divider
            )
        )
        binding.grid.itemAnimator = SlideInItemAnimator()

        loadLiveData()
    }

    private fun loadLiveData() {
        productViewModel.liveProducts.observeForever { products ->
            if (products != null) {
                debugLog("Cart: ${products.size}")
                adapter.addDataSource(products)
            }
        }
    }

}