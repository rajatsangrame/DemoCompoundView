package com.example.myapplication

import android.R.attr
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

/**
 * Ref: https://medium.com/android-news/perfmatters-introduction-to-custom-viewgroups-to-improve-performance-part-2-f14fbcd47c
 */
class RPKSelectButton : FrameLayout {

  private var font = ResourcesCompat.getFont(context, R.font.gilroy_medium)
  private var textSize = 16f
  private val textPadding: Int = 12.toPx
  private lateinit var textView: TextView
  private lateinit var imageView: ImageView
  private var labelText: String? = null
  private val TAG = "RPKSelectButton"

  constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
    attr?.let {
      obtainAttrValues(attr = it)
    }
    setupBackground()
    setupUi()
  }

  private fun obtainAttrValues(attr: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attr, R.styleable.RPKSelectButton)
    labelText = typedArray.getString(R.styleable.RPKSelectButton_labelText)
    typedArray.recycle()
  }

  private fun setupUi() {
    setupImageView()
    setupTextView()
    unselect()
  }

  private fun setupTextView() {
    textView = TextView(context)
    val tvParams = LayoutParams(
      ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT,
      Gravity.CENTER
    )
    textView.layoutParams = tvParams
    textView.typeface = font
    textView.textSize = textSize
    textView.text = labelText
    textView.setPadding(textPadding, textPadding, textPadding, textPadding)
    addView(textView)
  }

  private fun setupImageView() {
    imageView = ImageView(context)
    val layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
    imageView.layoutParams = layoutParams
    addView(imageView)
  }

  private fun setupBackground() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(attr.selectableItemBackground, outValue, true)
    setBackgroundResource(outValue.resourceId)
    isClickable = true
    isFocusable = true
  }

  fun select() {
    val d = ContextCompat.getDrawable(context, R.drawable.ic_rpkselect)
    imageView.setImageDrawable(d)
    val c = ContextCompat.getColor(context, R.color.white)
    textView.setTextColor(c)
    isSelected = true
  }

  fun unselect() {
    val d = ContextCompat.getDrawable(context, R.drawable.ic_rpkunselect)
    imageView.setImageDrawable(d)
    val c = ContextCompat.getColor(context, R.color.black)
    textView.setTextColor(c)
    isSelected = false
  }

  override fun performClick(): Boolean {
    Log.d(TAG, "performClick: ")
    if (isSelected) {
      unselect()
    } else {
      select()
    }
    return super.performClick()
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

    //Log.d(TAG, "onMeasure: left $left top $top right $right bottom $bottom")

    val centerX = abs(right - left) / 2
    val centerY = abs(top - bottom) / 2

    val tvCenterY = textView.measuredHeight / 2
    val t = centerY - tvCenterY
    val b = t + textView.measuredHeight

    val tvCenterX = textView.measuredWidth / 2
    val l = centerX - tvCenterX
    val r = l + textView.measuredWidth

    textView.layout(l, t, r, b)
    imageView.layout(0, 0, abs(right - left), abs(top - bottom))

  }

  private val Int.toPx: Int
    get() = ComponentUtils.toPx(context, this)

  private val Int.toDp: Int
    get() = ComponentUtils.pxToDp(context, this)

}
