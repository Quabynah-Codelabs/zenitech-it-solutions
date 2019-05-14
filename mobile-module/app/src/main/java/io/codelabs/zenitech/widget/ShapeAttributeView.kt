package io.codelabs.zenitech.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import io.codelabs.zenitech.R

/**
 * A composite view to display a text label and a shape preview view.
 *
 * The shape preview view is used to visualize the properties of shapeAppearance theme attributes.
 * The shape preview view is a TextView with it's background set to a MaterialShapeDrawable,
 * inflated according to this view's app:shapeAppearanceAttr property.
 */
class ShapeAttributeView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val shapeAttributeTextView: AppCompatTextView
    private val shapePreviewView: AppCompatTextView

    var shapeAttrText: String = "?shapeAppearanceSmallComponent"
        set(value) {
            shapeAttributeTextView.text = value
            field = value
        }

    private var shape = MaterialShapeDrawable().apply {
        strokeWidth = DEFAULT_SHAPE_STROKE_WIDTH
    }

    var shapeAppearanceRes: Int = R.attr.shapeAppearanceSmallComponent
        set(value) {
            shape.shapeAppearanceModel = ShapeAppearanceModel(
                context,
                attrs,
                defStyleAttr,
                getShapeAppearanceDefaultRes(value)
            )

            ViewCompat.setBackground(shapePreviewView, shape)

            field = value
        }

    var shapeFillColor: Int = Color.LTGRAY
        set(value) {
            shape.fillColor = ColorStateList.valueOf(value)
            field = value
        }

    var shapeStrokeColor: Int = Color.DKGRAY
        set(value) {
            shape.strokeColor = ColorStateList.valueOf(value)
            field = value
        }

    var shapeLetter: String = context.getString(R.string.shape_appearance_small_label)
        set(value) {
            shapePreviewView.text = value
            field = value
        }

    init {
        val view = View.inflate(context, R.layout.shape_attribute_view_layout, this)
        shapeAttributeTextView = view.findViewById(R.id.shape_attribute)
        shapePreviewView = view.findViewById(R.id.shape_preview)

        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ShapeAttributeView,
            defStyleAttr,
            defStyleRes
        )
        shapeAttrText = a.getString(
            R.styleable.ShapeAttributeView_android_text
        ) ?: shapeAttrText

        shapeFillColor = a.getColor(
            R.styleable.ShapeAttributeView_shapeFillColor,
            Color.LTGRAY
        )
        shapeStrokeColor = a.getColor(
            R.styleable.ShapeAttributeView_shapeStrokeColor,
            Color.DKGRAY
        )
        shapeAppearanceRes = a.getInt(
            R.styleable.ShapeAttributeView_shapeAppearanceAttr,
            R.attr.shapeAppearanceSmallComponent
        )

        shapeLetter = a.getString(
            R.styleable.ShapeAttributeView_shapeSizeLetter
        ) ?: shapeLetter

        a.recycle()
    }

    private fun getShapeAppearanceDefaultRes(shapeAppearanceRes: Int) = when (shapeAppearanceRes) {
        R.attr.shapeAppearanceMediumComponent -> R.style.Widget_MaterialComponents_CardView
        R.attr.shapeAppearanceLargeComponent -> R.style.Widget_MaterialComponents_NavigationView
        else -> R.style.Widget_MaterialComponents_Button
    }

    companion object {
        private const val DEFAULT_SHAPE_STROKE_WIDTH = 2F
    }
}
