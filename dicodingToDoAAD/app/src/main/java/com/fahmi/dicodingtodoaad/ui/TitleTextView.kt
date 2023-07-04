package com.fahmi.dicodingtodoaad.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.fahmi.dicodingtodoaad.R

class TitleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setStatus(isCompleted: Boolean, isOverdue: Boolean) {
        val textColorRes = if (isCompleted) {
            R.color.completed_task
        } else if (isOverdue) {
            R.color.overdue_task
        } else {
            R.color.normal_task
        }
        setTextColor(ContextCompat.getColor(context, textColorRes))
    }
}