package com.machinarum.alneo_sdk.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.machinarum.alneo_sdk.R

class LoadingLayout : RelativeLayout {

    private val imageView: ImageView by lazy {
        findViewById(R.id.gifimageview_image)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_loading_layout, this)
        loadGif(R.drawable.loading)
    }

    private fun loadGif(gifResId: Int) {
        Glide.with(context)
            .asGif()
            .load(gifResId)
            .into(imageView)
    }

    fun show() {
        visibility = VISIBLE
        hideKeyboard()
    }

    fun hide() {
        visibility = GONE
    }

    private fun hideKeyboard() {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(rootView.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
