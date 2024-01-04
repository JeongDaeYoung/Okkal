package com.daeng.okkal.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.daeng.okkal.BuildConfig
import com.daeng.okkal.R
import com.daeng.okkal.global.Define
import com.daeng.okkal.view.dialog.ColorPickDialog
import com.daeng.okkal.view.dialog.ColorRecommDialog
import com.daeng.okkal.view.dialog.ImagePickDialog
import com.daeng.okkal.view.dialog.LoadingDialog
import com.daeng.okkal.viewmodel.FashionFittingVM
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val FIRST_VIEW   = 0  // 0. FashionFitting
        const val SECOND_VIEW  = 1  // 1. TempRecommended
        const val THIRD_VIEW   = 2  // 2. Setting
    }

    private val fashionFittingVM: FashionFittingVM by viewModels()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>             // 사진 색상선택용 카메라 콜백



    /*
    * 뒤로가기 콜백 -> 앱 종료
    * */
    private var backKeyPressedTime: Long = 0
    private val callBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity,R.string.toast_app_finish,Toast.LENGTH_SHORT).show()
                return
            }

            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                ActivityCompat.finishAffinity(this@MainActivity)
                finish()
            }
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogger()

        Logger.i("onCreate()")

        setContent {
            MaterialTheme (
                typography = Typography(FontFamily(Font(R.font.nanumgothic_bold)))        // 기본 폰트 설정
            ){
                val pagerState = rememberPagerState()                                     // 탭 레이아웃의 페이지 상태값

                Column {
                    Toolbar()                     // 최상단 툴바
                    Tab(pagerState)               // 탭 레이아웃
                    TabsContent(pagerState)       // 페이지별로 화면전환
                }
            }
        }

        this.onBackPressedDispatcher.addCallback(this,callBack)            // 뒤로가기 콜백 추가

        activityResultLauncher = registerForActivityResult(                       // 사진 색상선택용 카메라 콜백 초기화
            ActivityResultContracts.StartActivityForResult(),
            fashionFittingVM.imagePickerCallback
        )
    }


    /*
    * 최상단 툴바
    * */
    @Composable
    private fun Toolbar() {
        TopAppBar(modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.White) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_okkal_text),
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
        }
    }


    /*
    * 툴바 아래 탭 레이아웃
    * */
    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun Tab(pagerState: PagerState) {
        val scope = rememberCoroutineScope()                   // 페이지 이동시 사용하는 Coroutine

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->                      // 선택된 탭에 아랫줄을 생기게 하는 커스텀 인디케이터
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .width(tabPositions[pagerState.currentPage].width)                  // 현재 선택된 탭의 너비로 Box너비를 설정
                            .height(1.dp)
                            .align(Alignment.BottomStart)                                       // 높이를 1.dp로 하고 하단에 정렬하여 밑줄로 보이게 함
                            .offset(x = tabPositions[pagerState.currentPage].left)              // 현재 선택된 탭의 왼쪽을 기준으로 Box를 배치
                            .background(colorResource(id = R.color.primary))                    // 아랫줄에 표시할 색상
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            backgroundColor = Color.White,
        ) {
            /*
            * 피팅 화면의 탭
            * */
            androidx.compose.material.Tab(
                text = { Text(stringResource(id = R.string.tab_item_fitting)) },
                selected = pagerState.currentPage == FIRST_VIEW,              //pagerState의 currentPage값이 변경되어 탭의 페이지수와 일치하면 true를 넣어 선택되었음을 알림
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(FIRST_VIEW)                   //클릭 되었을때 pagerState의 currentPage 값을 해당 탭의 페이지로 변경
                    }
                })

            /*
            * 기온별 옷 추천 화면의 탭
            * */
            androidx.compose.material.Tab(
                text = { Text(stringResource(id = R.string.tab_item_temp_recommend)) },
                selected = pagerState.currentPage == SECOND_VIEW,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(SECOND_VIEW)
                    }
                })

            /*
            * 세팅 화면의 탭
            * */
            androidx.compose.material.Tab(
                text = { Text(stringResource(id = R.string.tab_item_setting)) },
                selected = pagerState.currentPage == THIRD_VIEW,
                onClick = {
                    scope.launch {
                        pagerState.scrollToPage(THIRD_VIEW)
                    }
                })
        }
    }


    /*
    * 탭 레이아웃과 연결되어 페이지별로 화면전환
    * */
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun TabsContent(pagerState:PagerState) {
        HorizontalPager(count = 3, state = pagerState) {
            page ->
            when (page) {
                FIRST_VIEW -> FashionFittingView()                  // 피팅 화면으로 전환
                SECOND_VIEW -> SecondView()                         // 기온별 옷 추천 화면으로 전환
                THIRD_VIEW -> ThirdView()                           // 설정 화면으로 전환
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Preview
    @Composable
    private fun DefaultPreview() {
        MaterialTheme {
            val pagerState = rememberPagerState()
            Column {
                Tab(pagerState)               // tabRow
            }
        }
    }


    /*
    * FirstView -> 피팅 화면
    * */
    @Composable
    fun FashionFittingView() {
        val selRecommColor = remember { mutableStateOf(-1) }          // 색상 추천 다이얼로그에서 선택한 색상
        val colorPick = remember { mutableStateOf(Color.White) }            // 색상 선택표 다이얼로그에서 선택한 색상
        val imagePick = remember { mutableStateOf(Color.White) }            // 사진 색상 선택 다이얼로그에서 선택한 색상

        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(top = 20.dp)
                .fillMaxSize(),
        ) {
            Surface(                                                            // 상의 하의 이미지를 담을 Surface
                modifier = Modifier
                    .width(280.dp)
                    .height(400.dp)
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = 5.dp,
                color = colorResource(id = R.color.light_beige)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(                                                      // 상의 이미지
                        imageVector = ImageVector.vectorResource(id = R.drawable.shirts),
                        contentDescription = "Image",
                        modifier = Modifier
                            .width(155.dp)
                            .height(155.dp),
                        contentScale = ContentScale.FillBounds,
                        colorFilter = ColorFilter.tint(Color(fashionFittingVM.shirtsColor.value))
                    )
                    Image(                                                      // 하의 이미지
                        imageVector = ImageVector.vectorResource(id = R.drawable.pants),
                        contentDescription = "Image",
                        modifier = Modifier
                            .width(155.dp)
                            .height(215.dp),
                        colorFilter = ColorFilter.tint(Color(fashionFittingVM.pantsColor.value))
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 5.dp)
            ) {
                /*
                * 상의 선택 버튼
                * */
                SideButton(ImageVector.vectorResource(id = R.drawable.ic_btn_shirts),
                    backColor = if (fashionFittingVM.selPart.value == Define.SEL_SHIRTS)   // 선택여부에 따라 버튼 색상 변경
                        colorResource(id = R.color.dark_gray)
                    else
                        colorResource(id = R.color.transparent_gray),
                    onClicked = {
                        fashionFittingVM.selPart.value = Define.SEL_SHIRTS                 // 상의를 선택
                    })
                Spacer(modifier = Modifier.height(10.dp))
                /*
                * 하의 선택 버튼
                * */
                SideButton(ImageVector.vectorResource(id = R.drawable.ic_btn_pants),
                    backColor = if (fashionFittingVM.selPart.value == Define.SEL_PANTS)
                        colorResource(id = R.color.dark_gray)
                    else
                        colorResource(id = R.color.transparent_gray),
                    onClicked = {
                        fashionFittingVM.selPart.value = Define.SEL_PANTS                  // 하의를 선택
                    })
                Spacer(modifier = Modifier.height(10.dp))
                /*
                * 색상 추천 다이얼로그 버튼
                * */
                SideButton(ImageVector.vectorResource(id = R.drawable.ic_thumbs_up),
                    colorResource(
                        id = R.color.transparent_gray
                    ),
                    onClicked = {
                        fashionFittingVM.showColorRecommDialog.value = true                 // 색상 추천 다이얼로그 팝업
                    })
                Spacer(modifier = Modifier.height(10.dp))
                /*
                * 사진 색상 선택 다이얼로그 버튼
                * */
                SideButton(ImageVector.vectorResource(id = R.drawable.ic_camera), colorResource(
                    id = R.color.transparent_gray
                ), onClicked = {
                    fashionFittingVM.takePicture(activityResultLauncher)                    // 사진 색상 선택을 위한 카메라 호출
                })
                Spacer(modifier = Modifier.height(10.dp))
                /*
                * 색상 선택표 다이얼로그 버튼
                * */
                SideButton(ImageVector.vectorResource(id = R.drawable.ic_color_picker),
                    colorResource(
                        id = R.color.transparent_gray
                    ),
                    onClicked = {
                        fashionFittingVM.showColorPickerDialog.value = true                 // 색상표 선택 다이얼로그 팝업
                    })
            }


            /*
            * 색상추천 다이얼로그 팝업
            * */
            if (fashionFittingVM.showColorRecommDialog.value) {
                ColorRecommDialog(
                    onDismissRequest = {                                                // 다이얼로그 창 밖이 클릭
                        fashionFittingVM.showColorRecommDialog.value = false
                        fashionFittingVM.recentColorList.clear()
                        selRecommColor.value = -1
                    },
                    colors = fashionFittingVM.recentColorList,
                    selRecommColor = selRecommColor,
                    onClickedOK = {                                                     // 확인 버튼이 클릭
                        if (selRecommColor.value != -1)
                            fashionFittingVM.setNewPartColor(
                                fashionFittingVM.recentColorList.get(                   // 색이 선택된 경우에만 색상을 변경
                                    selRecommColor.value
                                ).hashCode()
                            )
                        fashionFittingVM.showColorRecommDialog.value = false
                        fashionFittingVM.recentColorList.clear()
                        selRecommColor.value = -1
                    }
                )
                fashionFittingVM.setRecommendedColor()
            }



            /*
            * 색상표 선택 다이얼로그 팝업
            * */
            if (fashionFittingVM.showColorPickerDialog.value) {
                ColorPickDialog(
                    onDismissRequest = {
                        fashionFittingVM.showColorPickerDialog.value = false
                    },
                    onClickedOK = {
                        fashionFittingVM.setNewPartColor(colorPick.value.hashCode())
                        fashionFittingVM.showColorPickerDialog.value = false
                    },
                    colorPick = colorPick
                )
            }



            /*
            * 사진 색상 선택 다이얼로그 팝업
            * */
            if (fashionFittingVM.showImagePickerDialog.value) {
                ImagePickDialog(
                    onDismissRequest = {
                        fashionFittingVM.showImagePickerDialog.value = false
                    },
                    onClickedOK = {
                        fashionFittingVM.setNewPartColor(imagePick.value.hashCode())
                        fashionFittingVM.showImagePickerDialog.value = false
                    },
                    imagePick = imagePick,
                    imageBitmap = fashionFittingVM.imagePickerBitmap                 // 촬영한 사진을 비트맵으로 전달
                )
            }



            /*
            * 로딩 다이얼로그 팝업
            * */
            if (fashionFittingVM.showLoadingDialog.value) {
                LoadingDialog()
            }
        }
    }


    /*
    * 피팅 화면 오른쪽 원형 버튼
    * */
    @Composable
    fun SideButton(icon: ImageVector, backColor: Color, onClicked:() -> Unit) {
        Image(imageVector = icon,
            contentDescription = "Image",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .background(backColor, CircleShape)
                .padding(10.dp)
                .clickable(                                                      // 클릭효과를 없애고 onClick함수를 상위뷰로 전달
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { onClicked() })
        )
    }


    /*
    * SecondView -> 기온별 옷 추천 화면
    * */
    @Composable
    fun SecondView() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("Text2",
                style = MaterialTheme.typography.h1)
        }
    }


    /*
    * ThirdView -> 세팅 화면
    * */
    @Composable
    fun ThirdView() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("Text3",
                style = MaterialTheme.typography.h1)
        }
    }

    /*
    * Logger 초기화
    * */
    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)        // 쓰레드 보여줄지 여부
            .methodCount(1)               // 몇라인까지 출력할지, 기본값 2
            .tag(Define.LOGGER_TAG)           // 글로벌 태그 적용
            .build()

        Logger.addLogAdapter(object: AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG               // DEBUG 모드에서만 로그 출력
            }
        })
    }
}