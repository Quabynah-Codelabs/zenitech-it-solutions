package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentCartBinding
import io.codelabs.zenitech.databinding.FragmentShopBinding

class CartFragment : BaseFragment() {
    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart,container, false)
        return binding.root
    }

}