package com.salugan.cobakeluar.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.LinearLayout

class RadioLinearLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), Checkable {

    private var isChecked = false
    private val checkableViews = mutableListOf<View>()

    init {
        setOnClickListener {
            toggle()
        }
    }


    override fun setChecked(checked: Boolean) {
        isChecked = checked
        for (view in checkableViews) {
            view.isSelected = isChecked

        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        isChecked = !isChecked
        setChecked(isChecked)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        if (child != null) {
            checkableViews.add(child)
        }
    }
}