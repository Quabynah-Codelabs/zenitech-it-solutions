package io.codelabs.zenitech.ui.recyclerview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.repository.ProductRepository
import io.codelabs.zenitech.core.theme.BaseActivity
import io.codelabs.zenitech.data.Cart
import io.codelabs.zenitech.ui.ProductDetailsActivity
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_empty_cart.view.*

class CartAdapter constructor(
    private val context: BaseActivity,
    private val repository: ProductRepository,
    private val listener: ButtonStateListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val EMPTY = R.layout.item_empty_cart
        private const val PRODUCT = R.layout.item_cart
    }

    var price: Double = 0.00
    val dataSource = mutableListOf<Cart>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var parent: ViewGroup? = null

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
        holder.v.cart_shimmer_container.startShimmer()
    }

    private fun bindProducts(holder: ProductViewHolder, position: Int) {
        val cart = dataSource[position]

        repository.getProductById(cart.product.toString()).observe(context, Observer { product ->
            holder.v.cart_product_name.text = product.name
            holder.v.cart_product_desc.text = product.desc
            holder.v.cart_product_price.text = String.format("$ %.2f", product.price)

            GlideApp.with(context)
                .asBitmap()
                .load(product.image)
                .placeholder(R.drawable.sample_image)
                .fallback(R.drawable.sample_image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.IMMEDIATE)
                .into(holder.v.cart_product_image)

            price += product.price
            listener.updateButtonState(dataSource.isNotEmpty(), price)

            holder.v.product_remove_cart.setOnClickListener {
                this.dataSource.remove(cart)
                notifyDataSetChanged()
                price -= product.price
                listener.updateButtonState(dataSource.isNotEmpty(), price)
            }

            holder.v.setOnClickListener {
                context.startActivity(Intent(context, ProductDetailsActivity::class.java).apply {
                    putExtra(ProductDetailsActivity.EXTRA_PRODUCT, product)
                    putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, product.key)
                    putExtra(ProductDetailsActivity.EXTRA_PRODUCT_IN_CART, true)
                })
            }
        })


    }

    fun addDataSource(products: MutableList<Cart>) {
        this.dataSource.clear()
        this.dataSource.addAll(products)
        notifyDataSetChanged()
    }

    interface ButtonStateListener {
        fun updateButtonState(state: Boolean = false, price: Double = 0.00)
    }
}