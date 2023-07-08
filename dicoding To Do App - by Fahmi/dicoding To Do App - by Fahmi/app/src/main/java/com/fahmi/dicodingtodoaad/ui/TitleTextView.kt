package com.fahmi.dicodingtodoaad.ui

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.fahmi.dicodingtodoaad.R

class TitleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var isCompleted = false
    private var isOverdue = false

    fun setStatus(isCompleted: Boolean, isOverdue: Boolean) {
        this.isCompleted = isCompleted
        this.isOverdue = isOverdue

        updateTextAppearance()
    }

    private fun updateTextAppearance() {
        val textColorRes = when {
            isCompleted -> R.color.completed_task
            isOverdue -> R.color.overdue_task
            else -> R.color.normal_task
        }
        setTextColor(ContextCompat.getColor(context, textColorRes))

        val flags = if (isCompleted) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        setPaintFlags(flags)
    }
}
