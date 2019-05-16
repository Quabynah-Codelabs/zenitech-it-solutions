package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paytm.pgsdk.PaytmClientCertificate
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
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

        adapter = CartAdapter(requireContext(), repository)

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
        productViewModel.liveProducts?.observeForever { products ->
            if (products != null) {
                debugLog("Cart: $products")
                adapter.addDataSource(products)
                var price = 0.00
                adapter.dataSource.forEach {
                    price += it.price
                }

                binding.checkout.visibility = if (products.isNotEmpty()) View.VISIBLE else View.GONE
                binding.checkout.text = String.format("Checkout GHC%.2f", price)

                binding.checkout.setOnClickListener {
                    //todo: implement payment logic
                    val pgService = PaytmPGService.getStagingService()
                    pgService.enableLog(requireContext())

                    pgService.initialize(
                        PaytmOrder(
                            hashMapOf<String, String>(
                                "MID" to "PAYTM_MERCHANT_ID",
                                "ORDER_ID" to "ORDER0000000001",
                                "CUST_ID" to System.currentTimeMillis().toString(),
                                "INDUSTRY_TYPE_ID" to "",
                                "CHANNEL_ID" to "WAP",
                                "TXN_AMOUNT" to "1",
                                "WEBSITE" to "PAYTM_WEBSITE",
                                "CALLBACK_URL" to "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=ORDER0000000001",
                                "EMAIL" to getString(R.string.dummy_email),
                                "MOBILE_NO" to getString(R.string.dummy_phone),
                                "CHECKSUMHASH" to "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4="
                            )
                        ),
                        PaytmClientCertificate("", "")
                    )
                    PaytmPGService.getService().startPaymentTransaction(
                        requireContext(),
                        true,
                        false,
                        object : PaytmPaymentTransactionCallback {
                            override fun onTransactionResponse(inResponse: Bundle?) {
                                debugLog("onTransactionResponse: $inResponse")
                            }

                            override fun clientAuthenticationFailed(inErrorMessage: String?) {
                                debugLog("clientAuthenticationFailed: $inErrorMessage")
                            }

                            override fun someUIErrorOccurred(inErrorMessage: String?) {
                                debugLog("someUIErrorOccurred: $inErrorMessage")
                            }

                            override fun onTransactionCancel(inErrorMessage: String?, inResponse: Bundle?) {
                                debugLog("onTransactionCancel: $inErrorMessage | $inResponse")
                            }

                            override fun networkNotAvailable() {
                                debugLog("networkNotAvailable")
                            }

                            override fun onErrorLoadingWebPage(
                                iniErrorCode: Int,
                                inErrorMessage: String?,
                                inFailingUrl: String?
                            ) {
                                debugLog("onErrorLoadingWebPage: $iniErrorCode | $inErrorMessage |$inFailingUrl")
                            }

                            override fun onBackPressedCancelTransaction() {
                                debugLog("onBackPressedCancelTransaction")
                            }
                        })
                }
            }
        }
    }

}