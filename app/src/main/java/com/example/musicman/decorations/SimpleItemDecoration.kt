package com.example.musicman.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleItemDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        with(outRect) {
            top = if (position == 0) margin else margin / 2
            bottom = if (position == parent.childCount - 1) margin else margin / 2
            left = margin
            right = margin
        }
    }
}