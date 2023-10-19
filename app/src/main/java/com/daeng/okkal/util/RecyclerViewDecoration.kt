package com.daeng.okkal.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/*
* 리사이클러뷰 아이템의 간격 조정
* */
class RecyclerViewDecoration(private val width: Int, private val height: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)             // 아이템의 위치를 가져옴

        // 좌우 간격
        if (width > 0) {
            if (position < 1) {
                outRect.left = width        // 첫번째 아이템인 경우만 왼쪽에도 간격을 줌
            }

            outRect.right = width
        }


        // 상하 간격
        if (height > 0) {
            if (position < 1) {
                outRect.top = height         // 첫번째 아이템인 경우만 위쪽에도 간격을 줌
            }

            outRect.bottom = height
        }
    }
}