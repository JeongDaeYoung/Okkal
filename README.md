## 소개
- Okkal은 옷의 상의 혹은 하의중 한쪽 색상만 정하고 다른쪽 색상은 정하지 못했을때 상의, 하의 이미지에 색을 입혀 조합을 해보거나 어울리는 색상을 추천받을수 있는 안드로이드 앱 입니다.

## 개발기간
- 2023-09 ~ ing

## 개발환경
- Android Studio Giraffe | 2022.3.1 Patch 2
- Java 11

## Sdk Version
- minSdkVersion : 24
- targetSdkVersion : 34

## 기술스택
- 언어
  - Kotlin, Coroutine
- UI
  - Compose
- 아키텍처
  - MVVM
- 의존성 주입
  - Hilt
- 안드로이드 아키텍처 컴포넌트(AAC) 및 주요 기타 라이브러리
  - ViewModel, Room, Lottie, Firebase, RecyclerView, Logger

## Project 구조
- app/......../okkal/api       : 외부 API와 관련된 코드
- app/......../okkal/data      : RoomDB 코드 및 데이터 클래스 패키지
- app/......../okkal/global    : 전역 상수 및 설정 클래스 패키지
- app/......../okkal/model     : 데이터 모델 및 Repository 패키지
- app/......../okkal/util      : 유틸리티 클래스 및 도우미 함수 패키지
- app/......../okkal/view      : 액티비티, 프래그먼트, 다이얼로그와 같은 사용자 인터페이스를 표시하는 클래스 패키지
- app/......../okkal/viewmodel : 뷰모델 클래스 패키지
