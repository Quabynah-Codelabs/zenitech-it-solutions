package io.codelabs.zenitech.ui.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.codelabs.recyclerview.DataLoadingSubject
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.data.Product
import io.codelabs.zenitech.ui.ProductDetailsActivity
import kotlinx.android.synthetic.main.item_empty_product.view.*
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.item_product.view.product_desc
import kotlinx.android.synthetic.main.item_product.view.product_image
import kotlinx.android.synthetic.main.item_product.view.product_name
import kotlinx.android.synthetic.main.item_product.view.product_price

class ProductAdapter constructor(
    private val context: Context,
    private val repository: ProductRepository, override val isDataLoading: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), DataLoadingSubject {


    override fun registerCallback(callbacks: DataLoadingSubject.DataLoadingCallbacks) {
        callbacks.dataStartedLoading()
    }

    override fun unregisterCallback(callbacks: DataLoadingSubject.DataLoadingCallbacks) {
        callbacks.dataFinishedLoading()
    }

    companion object {
        private const val EMPTY = R.layout.item_empty_product
        private const val PRODUCT = R.layout.item_product
    }

    private val dataSource = mutableListOf<Product>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var parent: ViewGroup? = null
    private var tappedProduct: Product? = null

    override fun getItemViewType(position: Int): Int = if (dataSource.isEmpty()) EMPTY else PRODUCT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent

        return when (viewType) {
            EMPTY -> EmptyViewHolder(inflater.inflate(EMPTY, parent, false))
            PRODUCT -> ProductViewHolder(inflater.inflate(PRODUCT, parent, false))
            else -> throw IllegalArgumentException("No viewholder defined")
        }
    }

    override fun getItemCount(): Int = if (dataSource.isEmpty()) 1 else dataSource.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            EMPTY -> bindEmptyView(holder as EmptyViewHolder)
            PRODUCT -> bindProducts(holder as ProductViewHolder, position)
        }
    }

    private fun bindEmptyView(holder: EmptyViewHolder) {
        holder.v.shimmer_container.startShimmer()
    }

    private fun bindProducts(holder: ProductViewHolder, position: Int) {
        val product = dataSource[position]

        holder.v.product_name.text = product.name
        holder.v.product_desc.text = product.desc
        holder.v.product_price.text = String.format("$ %.2f", product.price)

        GlideApp.with(context)
            .asBitmap()
            .load(product.image)
            .placeholder(R.drawable.sample_image)
            .fallback(R.drawable.sample_image)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.IMMEDIATE)
            .into(holder.v.product_image)

        holder.v.product_add_cart.setOnClickListener {

            if (parent != null) {
                var isUndo = false
                this.tappedProduct = product
                if (holder.v.product_add_cart.drawable == context.resources.getDrawable(
                        R.drawable.twotone_add_shopping_cart_24px,
                        null
                    )) {
                    repository.removeProduct(tappedProduct ?: product)
                } else {
                    val snackbar =
                        Snackbar.make(parent!!, "${product.name} added to cart", Snackbar.LENGTH_SHORT)
                            .setAction("Undo") {
                                isUndo = true
                                tappedProduct = null
                                holder.v.product_add_cart.setImageDrawable(
                                    context.resources.getDrawable(
                                        R.drawable.twotone_add_shopping_cart_24px, null
                                    )
                                )
                            }
                    snackbar.show()

                    holder.v.product_add_cart.setImageDrawable(
                        context.resources.getDrawable(
                            R.drawable.twotone_remove_shopping_cart_24px, null
                        )
                    )

                    snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (!isUndo && tappedProduct != null) {
                                if (tappedProduct != null) repository.addProduct(tappedProduct!!)
                            } else if (tappedProduct != null) repository.removeProduct(tappedProduct!!)
                        }
                    })
                }
            }
        }

        holder.v.setOnClickListener {
            context.startActivity(Intent(context, ProductDetailsActivity::class.java).apply {
                putExtra(ProductDetailsActivity.EXTRA_PRODUCT, product)
                putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, product.key)
            })
        }
    }

    fun addDataSource(products: MutableList<Product>) {
        this.dataSource.clear()
        this.dataSource.addAll(products)
        notifyDataSetChanged()
    }
}