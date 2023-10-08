package com.mahmoud.mohammed.movieapp.presentation.view

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView

class MovieView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(drawable != null && drawable is BitmapDrawable){
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val heigh = MeasureSpec.getSize(heightMeasureSpec)
            if (width <= 0 || heigh <= 0) {
                return
            }

            val bmp_height = (drawable as BitmapDrawable).bitmap.height
            val bmp_width = (drawable as BitmapDrawable).bitmap.width

            var ss_width = width
            var ss_height = bmp_height * ss_width / bmp_width
            if (ss_height >= heigh) {
                ss_height = heigh
                ss_width = bmp_width * ss_height / bmp_height
            }
            super.setMeasuredDimension(ss_width, ss_height)
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}