package kayvankaseb.android.MLkit.facedetection

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.otaliastudios.cameraview.Facing
import com.otaliastudios.cameraview.Frame
import kayvankaseb.android.MLkit.coredetection.DrawFace
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var faceDetector: FaceDetector? = null
    private var detectionViewer: DrawFace? = null
    private var cameraWidth: Int = 0
    private var cameraHeight: Int = 0
    private var isLoadingDetection = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        checkPermission()

        with(cameraPreview) {
            facing = Facing.FRONT
            scaleX = -1f
            addFrameProcessor { if (!isLoadingDetection) detect(it) }
        }
    }

    private fun initUI() {
        findViewById<TextView>(R.id.textViewMood)
    }

    private fun detect(frame: Frame) {
        if (cameraWidth > 0 && cameraHeight > 0) {
            faceDetector?.detectFromByteArray(frame.data)
            isLoadingDetection = true
        } else {
            cameraWidth = frame.size.width
            cameraHeight = frame.size.height
            setupFaceDetector()
        }
    }

    private fun setupFaceDetector() {
        faceDetector = FaceDetector(
            cameraWidth = cameraWidth,
            cameraHeight = cameraHeight,
            successListener = OnSuccessListener (){
                val bmp = detectionViewer?.showVisionDetection(it)
                imageViewOverlay.setImageBitmap(bmp)
                isLoadingDetection = false
                processHappiness(it)
                processFace(it)
            },
            failureListener = OnFailureListener {
                Toast.makeText(this@MainActivity, getString(R.string.detection_error), Toast.LENGTH_SHORT).show()
            })

        detectionViewer = DrawFace(cameraWidth, cameraHeight)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                REQUEST_CAMERA_PERMISSION)
        } else {
            cameraPreview.start()
        }
    }

    private fun getEmojiUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED
                && grantResults[1] == PERMISSION_GRANTED) {
                cameraPreview.start()
            }
        }
    }

    companion object { private const val REQUEST_CAMERA_PERMISSION = 123 }

    private fun processHappiness(faces: List<FirebaseVisionFace>) {

        for (face in faces) {
            if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val smileProb = face.smilingProbability
                if (smileProb > 0) {
                    Toast.makeText(this, "The degree of your happiness is:$smileProb", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun processFace(faces: List<FirebaseVisionFace>) {

        for (face in faces) {

            textViewMood.text = getEmojiUnicode(0x1F60A) + getEmojiUnicode(0x1F60A) + getEmojiUnicode(0x1F60A)

            if (face.leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val leftEyeOpenProb = face.leftEyeOpenProbability
                val rightEyeOpenProb = face.rightEyeOpenProbability
                if (leftEyeOpenProb < 0.2 || rightEyeOpenProb < 0.2) {
                    textViewMood.text = getEmojiUnicode(0X1F609) + getEmojiUnicode(0X1F609) + getEmojiUnicode(0X1F609)
                }
            }
                if (face.smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                    val smileProb = face.smilingProbability
                    if (smileProb > 0.4) {
                        textViewMood.text = getEmojiUnicode(0x1F601) + getEmojiUnicode(0x1F601) + getEmojiUnicode(0x1F601)
                }
            }
        }
    }
}

