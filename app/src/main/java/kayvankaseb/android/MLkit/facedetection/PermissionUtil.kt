package kayvankaseb.android.MLkit.facedetection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class PermissionUtil {

    companion object {
        /**存储权限*/
        const val storageRequestCode: Int = 101
        val storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        /**定位权限*/
        const val loctionRequestCode: Int = 102
        val loctionPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        /**图片获取权限*/
        const val takePhotoRequestCode: Int = 103
        val takePhotoPermission = arrayOf(Manifest.permission.CAMERA)
        /**电话状态权限*/
        const val readPhoneRequestCode:Int =104
        val readPhonePermission = arrayOf(Manifest.permission.READ_PHONE_STATE)

        @JvmStatic
        fun checkStoragePermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, storagePermission[0]) == PackageManager.PERMISSION_GRANTED&&
                    ContextCompat.checkSelfPermission(context, storagePermission[1]) == PackageManager.PERMISSION_GRANTED
        }

        @JvmStatic
        fun checkLoctionPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, loctionPermission[0]) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, loctionPermission[1]) == PackageManager.PERMISSION_GRANTED
        }

        @JvmStatic
        fun checkTakePhotoPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, takePhotoPermission[0]) == PackageManager.PERMISSION_GRANTED
//                    ContextCompat.checkSelfPermission(context, takePhotoPermission[1]) == PackageManager.PERMISSION_GRANTED
        }

        @JvmStatic
        fun checkReadPhonePermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, readPhonePermission[0]) == PackageManager.PERMISSION_GRANTED
        }

    }

}