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
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentCartBinding
import io.codelabs.zenitech.ui.recyclerview.CartAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CartFragment : BaseFragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartAdapter
    private val repository: ProductRepository by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(requireContext(), repository)

        binding.grid.adapter = adapter
        binding.grid.layoutManager = LinearLayoutManager(requireContext()) as RecyclerView.LayoutManager?
        binding.grid.setHasFixedSize(true)
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
        uiScope.launch {
            repository.getAllProducts().observeForever {
                if (it != null) {
                    adapter.addDataSource(it)
                    var price = 0.00
                    adapter.dataSource.forEach {
                        price += it.price
                    }

                    binding.checkout.visibility = if (price != 0.00) View.VISIBLE else View.GONE
                    binding.checkout.text = String.format("Checkout GHC%.2f", price)

                    binding.checkout.setOnClickListener {
                        //todo: implement payment logic
                    }
                }
            }
        }
    }

}