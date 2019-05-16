package io.codelabs.zenitech.data

import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.widget.ForegroundImageView
import io.codelabs.zenitech.R
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "products")
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    var name: String,
    var price: Double,
    var desc: String,
    var image: String?,
    var url: String? = "https://codelabs.netlify.com/products/$key",
    var uploadTime: Long = System.currentTimeMillis(),
    var quantity: Long = 0L,
    var category: String = Category.OTHER,
    override var synced: Boolean = false,
    override var isWishListItem: Boolean = false
) : SyncableDataModel {

    @Ignore
    constructor() : this("", "", 0.00, "", "")

    object Category {
        const val LAPTOP = "Laptop"
        const val OTHER = "Other"
        const val MOBILE = "Smart Phone"
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageLink")
        fun loadImage(
            imageView: ForegroundImageView,
            imageLink: String?
        ) {

            GlideApp.with(imageView.context)
                .load(imageLink)
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.sample_image)
                .error(R.drawable.sample_image)
                .transition(withCrossFade())
                .into(imageView)
        }
    }
}