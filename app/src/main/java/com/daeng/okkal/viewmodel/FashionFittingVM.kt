package com.daeng.okkal.viewmodel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.core.content.FileProvider
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daeng.okkal.OkkalApplication
import com.daeng.okkal.R
import com.daeng.okkal.data.room.RoomEntity
import com.daeng.okkal.global.Define
import com.daeng.okkal.model.FashionFittingRepository
import com.daeng.okkal.util.RecentColorLinkedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FashionFittingVM @Inject constructor(private val myRepository : FashionFittingRepository): ViewModel() {
    val selParts: MutableState<Int> = mutableIntStateOf(Define.SEL_SHIRTS)
    val shirtsColors: MutableState<Int> = mutableIntStateOf(Color.White.hashCode())
    val pantsColors: MutableState<Int> = mutableIntStateOf(Color.Black.hashCode())
    val colorLists: SnapshotStateList<Int> = mutableStateListOf()

    val imagePickerBitmap: MutableState<Bitmap> = mutableStateOf(Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888))


    val showColorRecommDialog = mutableStateOf(false)                   // 색상 추천 다이얼로그
    val showColorPickerDialog   = mutableStateOf(false)                 // 색상 선택표 다이얼로그
    val showImagePickerDialog = mutableStateOf(false)                   // 이미지 선택 색상 다이얼로그
    val showLoadingDialog = mutableStateOf(false)                       // 로딩 다이얼로그


    init {
        viewModelScope.launch {
            myRepository.initColorData()
            loadColorData()
        }
    }

    private fun loadShirtsColor() {
        viewModelScope.launch{
            shirtsColors.value = myRepository.getShirtsColor()
        }
    }

    private fun loadPantsColor() {
        viewModelScope.launch{
            pantsColors.value = myRepository.getPantsColor()
        }
    }

    private fun loadColorData() {
        viewModelScope.launch {
            val data: RoomEntity.Companion.InitApp = myRepository.getColorData()
            shirtsColors.value = data.shirtsColor
            pantsColors.value = data.pantsColor
//            _colorList.value = RecentColorLinkedList.fromList(data.colorData.colorList)
        }
    }

    fun updateSelPart(part : Int) {
        selParts.value = part
    }

    private fun updateShirtsColor(color: Int) {
        myRepository.updateShirtsColor(color)
        shirtsColors.value = color
    }

    private fun updatePantsColor(color: Int) {
        myRepository.updatePantsColor(color)
        pantsColors.value = color
    }

    private fun addColorList(color : Int) {
//        val list = _colorList.value
//        list!!.addColor(color)
//        myRepository.updateColorList(ArrayList(list))
//        _colorList.postValue(list!!)
    }


    /*
    * 현재 선택된 부위에 색상 업데이트 및 최근 색상 리스트에 추가
    * */
    fun setNewPartColor(color: Int) {
        when(selParts.value) {
            Define.SEL_SHIRTS -> updateShirtsColor(color)
            Define.SEL_PANTS -> updatePantsColor(color)
        }
        addColorList(color)
    }


    /*
    * 현재 선택된 부위에 색상 업데이트
    * */
    fun setRecentPartColor(color: Int) {
        when(selParts.value) {
            Define.SEL_SHIRTS -> updateShirtsColor(color)
            Define.SEL_PANTS -> updatePantsColor(color)
        }
    }

    /*
    * 현재 선택되지 않은 파트의 인덱스를 리턴
    * */
    fun getOtherPartColor(): Int? {
        var color: Int? = null
        when(selParts.value) {
            Define.SEL_SHIRTS -> color = pantsColors.value
            Define.SEL_PANTS -> color = shirtsColors.value
        }
        return color
    }



    /*
    * ==================================== ColorRecommDialog ===========================================
    * */


    /*
    * 추천 색상 셋
    * */
    fun setRecommendedColor() {
        val color = getOtherPartColor()

        val hsvValue = FloatArray(3)                                  // 0 : 색상, 1 : 채도, 2 : 명도
        android.graphics.Color.RGBToHSV(color!!.red, color.green, color.blue, hsvValue)

        recursiveHsv(hsvValue.copyOf(), 0.2f)                        // 명도를 0.2씩 올려 색상 찾기
        recursiveHsv(hsvValue.copyOf(), -0.2f)                       // 명도를 0.2씩 내려 색상 찾기
    }


    /*
    * 추천 색상을 구하는 재귀함수
    * */
    private fun recursiveHsv(hsvValue: FloatArray, offSet: Float) {
        hsvValue[2] += offSet

        if (hsvValue[2] > 0 && hsvValue[2] < 1) {
            // hsvValue[2]의 값(명도)이 0보다 크고 1보다 작을때 색상을 추가하고 재귀
            colorLists.add(android.graphics.Color.HSVToColor(hsvValue))
            recursiveHsv(hsvValue, offSet)
        } else {
            // hsvValue[2]의 값(명도)이 0 이하 또는 1 이상인 경우 재귀를 종료
            return
        }
    }


    /*
    * ==================================== ImagePickDialog ===========================================
    * */


    private var mCurrentPhotoPath: String? = null

    val imagePickerCallback: ActivityResultCallback<ActivityResult> = ActivityResultCallback {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            CoroutineScope(Dispatchers.Main).launch {
                showLoadingDialog.value = true
                CoroutineScope(Dispatchers.Default).async {
                    var bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
                    val angle = getImageRotate(mCurrentPhotoPath)

                    imagePickerBitmap.value = rotateBitmap(bitmap, angle)
                    showImagePickerDialog.value = true
                }.await()
                showLoadingDialog.value = false
            }
        }
    }



    /*
    * 카메라 액티비티 호출
    * */
    fun takePicture(activityResultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        /*
        * 카메라 호출전 인텐트에 사진 파일명 추가(고화질 사진으로 불러오기 위한 작업)
        * */
        if (intent.resolveActivity(OkkalApplication.appContext.packageManager) != null) {
            var photoFile: File? = null
            var tempDir = OkkalApplication.appContext.cacheDir

            val imageFileName = "Clothes_Photo"

            try {
                val tempImage = File.createTempFile(imageFileName,".jpg", tempDir)
                mCurrentPhotoPath = tempImage.absolutePath
                photoFile = tempImage
            } catch (e: IOException) {

                e.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(OkkalApplication.appContext,OkkalApplication.appContext.packageName + ".fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activityResultLauncher.launch(intent)
            }
        }
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
}