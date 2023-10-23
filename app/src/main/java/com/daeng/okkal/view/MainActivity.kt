package com.daeng.okkal.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.daeng.okkal.BuildConfig
import com.daeng.okkal.R
import com.daeng.okkal.databinding.MainActivityBinding
import com.daeng.okkal.global.Define
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_FIRST = 0  // 0. FirstFragment
        const val FRAGMENT_SECOND = 1 // 1. SecondFragment
        const val FRAGMENT_THIRD = 2  // 2. ThirdFragment
    }

    lateinit var binding: MainActivityBinding

    private var firstFragment: FashionFittingFrag?           = null    // 1. FirstFragment
    private var secondFragment: SecondFrag?                  = null    // 2. SecondFragment
    private var thirdFragment: ThirdFrag?                    = null    // 3. ThirdFragment



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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogger()

        Logger.i("onCreate()")

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.onBackPressedDispatcher.addCallback(this,callBack)            // 뒤로가기 콜백 추가

        initBottomNavi()                       // 바텀 네비게이션 초기화
        moveFragment(FRAGMENT_FIRST)           // 홈화면으로 초기화
    }


    /*
    * 프래그먼트 초기화
    * */
    private fun initFragment(fragment: Int) {
        when(fragment) {
            FRAGMENT_FIRST -> if (firstFragment==null) firstFragment = FashionFittingFrag()
            FRAGMENT_SECOND -> if (secondFragment==null) secondFragment = SecondFrag()
            FRAGMENT_THIRD -> if (thirdFragment==null) thirdFragment = ThirdFrag()
        }
    }


    /*
    * 프래그먼트 이동
    * */
    private fun moveFragment(fragment: Int) {
        initFragment(fragment)

        val transaction = supportFragmentManager.beginTransaction()
        when(fragment){
            FRAGMENT_FIRST -> transaction.replace(binding.container.id, firstFragment!!)
            FRAGMENT_SECOND -> transaction.replace(binding.container.id, secondFragment!!)
            FRAGMENT_THIRD -> transaction.replace(binding.container.id, thirdFragment!!)
        }
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }


    /*
    * 바텀 네비게이션 초기화
    * */
    private fun initBottomNavi() {
        binding.bottomNavi.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> moveFragment(FRAGMENT_FIRST)           // FirstFragment
                R.id.setting -> moveFragment(FRAGMENT_SECOND)       // SecondFragment
                R.id.food -> moveFragment(FRAGMENT_THIRD)           // ThirdFragment
            }
            true
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