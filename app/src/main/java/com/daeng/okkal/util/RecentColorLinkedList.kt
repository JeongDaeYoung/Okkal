package com.daeng.okkal.util

import com.daeng.okkal.global.Define
import java.util.LinkedList

/**
 * Created by JDY on 2023-11-22
 * 데이터 중복 제한, 크기 제한을 위한 커스텀 LinkedList
 */


class RecentColorLinkedList<E> : LinkedList<E>() {
    /*
    * 최근 사용한 데이터 추기
    * */
    fun addColor(e: E) {
        remove(e)                             // 중복요소 제거
        trimToSize()                          // 리스트 크기제한
        super.addFirst(e)                     // 최근 사용한 데이터를 맨 앞에 추가
    }

    /*
    * 리스트가 최대 크기를 넘어가면 사용한지 오래된 데이터를 삭제
    * */
    private fun trimToSize() {
        while (size > Define.MAX_RECENT_COLOR) {
            removeLast()
        }
    }


    companion object {
        /*
        * List -> RecentColorLinkedList 형변환
        * */
        fun <T> fromList(list: List<T>): RecentColorLinkedList<T> {
            val linkedList = RecentColorLinkedList<T>()
            linkedList.addAll(list)
            return linkedList
        }
    }
}