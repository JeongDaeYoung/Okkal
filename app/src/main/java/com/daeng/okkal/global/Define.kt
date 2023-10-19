package com.daeng.okkal.global

object Define {
    // Logger Tag
    const val LOGGER_TAG = "Okkal_"

    // Option
    const val COMPILE_MODE_LIVE = 0
    const val COMPILE_MODE_DEV  = 1
    const val COMPILE_MODE_QA   = 2

    // FirstFragment
    const val SEL_SHIRTS = 0       // 상의 선택
    const val SEL_PANTS  = 1       // 하의 선택

    const val CLIENT_ID = "CLIENT" // 사용자 ID (Room 테이블 주키로 쓰기 위함)

    // RoomTable
    const val TABLE_INIT_APP = "INIT_APP_DATA_TB"               // 앱 시작시 초기화 데이터 관련 테이블명

    // RoomColumn
    const val COL_INIT_APP_SHIRTS_COLOR = "INIT_APP_SHIRTS_COLOR"         // 상의 색상 컬럼명
    const val COL_INIT_APP_PANTS_COLOR = "INIT_APP_PANTS_COLOR"           // 하의 색상 컬럼명
    const val COL_INIT_APP_COLOR_LIST = "INIT_APP_COLOR_LIST"             // 색상 리스트 컬럼명
}