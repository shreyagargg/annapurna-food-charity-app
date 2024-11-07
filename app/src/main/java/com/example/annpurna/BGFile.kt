package com.example.annpurna

import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class BGFile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.copper)
        style = Paint.Style.FILL
        pathEffect = CornerPathEffect(50f)
    }

    private val path = Path()

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        path.reset()

        path.moveTo(0f, height * 0f)
        path.lineTo(width*1.1f, height * 0f)
        path.lineTo(width*1.01f, height*0.25f)
        path.lineTo(width*0.95f, height*0.23f)
        path.lineTo(width*0.05f, height*0.23f)
        path.lineTo(0f, height*0.25f)

//        path.close() // Closes the shape, connecting the last point back to the first

        canvas.drawPath(path, paint)
    }
}
