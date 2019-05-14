package io.codelabs.zenitech.data

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.widget.ForegroundImageView
import io.codelabs.zenitech.R
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    var avatar: String? = null,
    var type: String = BaseDataModel.ModelType.GUEST,
    var createdAt: Long = System.currentTimeMillis()
) : BaseDataModel {

    @Ignore
    constructor() : this("")

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadAvatar(
            imageView: ForegroundImageView,
            imageUrl: String?
        ) {
            GlideApp.with(imageView.context)
                .asBitmap()
                .load(imageUrl)
                .circleCrop()
                .transition(withCrossFade())
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(imageView)
        }
    }
}