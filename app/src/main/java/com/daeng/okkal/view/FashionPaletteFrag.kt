package com.daeng.okkal.view

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daeng.okkal.view.adapter.ColorListAdapter
import com.daeng.okkal.databinding.FashionPaletteFragBinding
import com.daeng.okkal.global.Define
import com.daeng.okkal.util.RecyclerViewDecoration
import com.daeng.okkal.view.dialog.ColorRecommendedDialog
import com.daeng.okkal.view.dialog.PhotoDialog
import com.daeng.okkal.view.dialog.ViewCloseListener
import com.daeng.okkal.viewmodel.FashionPaletteVM
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class FashionPaletteFrag : BaseFragment<FashionPaletteFragBinding>(FashionPaletteFragBinding::inflate) {

    private val viewModel: FashionPaletteVM by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var colorListAdapter: ColorListAdapter? = null

    private var mCurrentPhotoPath: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i("onViewCreate()")

        setUpObserver()                  // 라이브데이터 옵저버 셋업
        initColorList()                  // 색상 리스트 리사이클러뷰 초기화
        setCameraIntent()                // 카메라 액티비티 콜백함수 초기화

        binding.btnRecommended.setOnClickListener {
            ColorRecommendedDialog(viewModel.getOtherPartColor()!!, ViewCloseListener {
                if (it != null) setSelPartColor(it as Int)
            }).show(parentFragmentManager, "colorRecommended")
        }

        binding.btnShirts.setOnClickListener {
            viewModel.updateSelPart(Define.SEL_SHIRTS)         // 상의 선택
        }

        binding.btnPants.setOnClickListener {
            viewModel.updateSelPart(Define.SEL_PANTS)          // 하의 선택
        }

        binding.btnTakePicture.setOnClickListener {
            takePicture()                // 카메라 액티비티 호출
        }
    }


    /*
    * 색상 리스트 리사이클러뷰 초기화
    * */
    private fun initColorList() {
        binding.listColor.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(RecyclerViewDecoration(40,0))
            var colorList: HashSet<Int> = HashSet()
            colorList.add(Color.BLACK)
            colorList.add(Color.DKGRAY)
            colorList.add(Color.WHITE)
            colorListAdapter = ColorListAdapter(::setSelPartColor)
            colorListAdapter!!.setItem(colorList)
            adapter = colorListAdapter
        }
    }


    private fun setUpObserver() {
        // 상의 or 하의 선택값
        viewModel.selPart.observe(viewLifecycleOwner, Observer {
            if (it==Define.SEL_SHIRTS) {
                binding.btnShirts.isSelected = true
                binding.btnPants.isSelected = false
            } else {
                binding.btnShirts.isSelected = false
                binding.btnPants.isSelected = true
            }
        })

        // 상의 색상
        viewModel.shirtsColor.observe(viewLifecycleOwner, Observer {
            changeShirtsColor(it)
        })

        // 하의 색상
        viewModel.pantsColor.observe(viewLifecycleOwner, Observer {
            changePantsColor(it)
        })
    }


    /*
    * 카메라 액티비티 콜백함수 초기화
    * */
    private fun setCameraIntent() {
        var photoDialog: PhotoDialog? = null

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode == Activity.RESULT_OK) {
                CoroutineScope(Dispatchers.Main).launch {
                    showLoading()
                    CoroutineScope(Dispatchers.Default).async {
                        var bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                        val angle = getImageRotate(mCurrentPhotoPath)

                        photoDialog = PhotoDialog(rotateBitmap(bitmap, angle))
                        photoDialog!!.setViewCloseListener(ViewCloseListener { rgb ->
                            if (rgb != -1) setSelPartColor(rgb as Int)        // 옷 색상 업데이트
                            // 비트맵 메모리 해제
                            bitmap.recycle()
                            bitmap = null
                        })
                    }.await()
                    dismissLoading()
                    photoDialog!!.show(parentFragmentManager, "photoDialog")
                }
            }
        })
    }

    /*
    * 카메라로 찍은 이미지의 회전각 정보를 가져옴
    * */
    private fun getImageRotate(filePath: String?): Float {
        val ei = ExifInterface(filePath!!)
        val angle = when(ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        return angle
    }

    /*
    * 회전되어 있는 사진을 똑바로 회전시킴
    * */
    private fun rotateBitmap(src: Bitmap, angle: Float): Bitmap {
        val width = src.width
        val height = src.height

        // 원하는 사이즈로 조절(필요할 경우 사용)
//        var newWidth = 0f
//        var newHeight = 0f
//        if(width > height) {
//            newWidth = size
//            newHeight = height.toFloat() * (newWidth / width.toFloat())
//        } else {
//            newHeight = size
//            newWidth = width.toFloat() * (newHeight / height.toFloat())
//        }
//        val scaleWidth = newWidth.toFloat() / width
//        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postRotate(angle);
//        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true)
    }

    /*
    * 카메라 액티비티 호출
    * */
    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        /*
        * 카메라 호출전 인텐트에 사진 파일명 추가(고화질 사진으로 불러오기 위한 작업)
        * */
        if (intent.resolveActivity(activity.packageManager) != null) {
            var photoFile: File? = null
            var tempDir = activity.cacheDir

            val imageFileName = "Clothes_Photo"

            try {
                val tempImage = File.createTempFile(imageFileName,".jpg", tempDir)
                mCurrentPhotoPath = tempImage.absolutePath
                photoFile = tempImage
            } catch (e: IOException) {

                e.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity, activity.packageName + ".fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activityResultLauncher.launch(intent)
            }
        }
    }


    /*
    * 상의 색상 변경
    * */
    private fun changeShirtsColor(rgb: Int) {
        binding.imgShirts.setColorFilter(rgb)
    }


    /*
    * 하의 색상 변경
    * */
    private fun changePantsColor(rgb: Int) {
        binding.imgPants.setColorFilter(rgb)
    }


    /*
    * 현재 선택된 부위에 색상 업데이트
    * */
    private fun setSelPartColor(color: Int) {
        when(viewModel.selPart.value) {
            Define.SEL_SHIRTS -> viewModel.updateShirtsColor(color)
            Define.SEL_PANTS -> viewModel.updatePantsColor(color)
        }
    }
}
