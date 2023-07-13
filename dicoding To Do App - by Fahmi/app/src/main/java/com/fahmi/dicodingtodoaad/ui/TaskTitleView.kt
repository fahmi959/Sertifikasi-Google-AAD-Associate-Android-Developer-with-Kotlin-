package com.fahmi.dicodingtodoaad.ui

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.fahmi.dicodingtodoaad.R

class TaskTitleView : AppCompatTextView {
    private var mState = 0
    private var isOverdue = false

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    var state: Int
        get() = mState
        set(state) {
            when (state) {
                DONE -> {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                NORMAL -> {
                    paintFlags = 0
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                OVERDUE -> {
                    paintFlags = 0
                    setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                }
                else -> return
            }
            mState = state
        }

    fun setOverdue(isOverdue: Boolean) {
        this.isOverdue = isOverdue
        updateTextAppearance()
    }

    private fun updateTextAppearance() {
        if (isOverdue) {
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
        }
    }


    fun setStatus(status: Int) {
        mState = status
        when (status) {
            NORMAL -> {
                paintFlags = 0
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            DONE -> {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            OVERDUE -> {
                paintFlags = 0
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            }
            else -> return
        }
    }

    companion object {
        const val NORMAL = 0
        const val DONE = 1
        const val OVERDUE = 2
    }
}