package com.example.qrcodescanner

import android.Manifest.permission.*
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.qrcodescanner.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC)
        .build()
   private val REQUEST_IMAGE_CAPTURE = 1

    private var imageBitmap: Bitmap?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        capture_image.setOnClickListener{
            takeImage()
            text_view.text = ""

        }
        detect_text_image_btn.setOnClickListener{
            detectImage()
        }
    }

    /**
     * A function to detect the image whether it is of type WIFI of URL
     */

    private fun detectImage() {
       if(imageBitmap != null){
           val image = InputImage.fromBitmap(imageBitmap!!,0)
           val scanner = BarcodeScanning.getClient(options)

           scanner.process(image).addOnSuccessListener {
               barcodes -> if(barcodes.toString() == "[]"){
                   Toast.makeText(this,"Nothing to sccan", Toast.LENGTH_LONG).show()
           }
               for (barcode in barcodes) {





                   val valueType = barcode.valueType
                   // See API reference for complete list of supported types
                   when (valueType) {


                       Barcode.TYPE_WIFI -> {
                           val ssid = barcode.wifi!!.ssid
                           val password = barcode.wifi!!.password
                           val type = barcode.wifi!!.encryptionType

                           text_view.text= ssid+"\n"+password+"\n"+type
                           val qrDetail = QrDetail(text_view.text.toString())

                           // Store the content of the variable qrDetails to the firestore
                           FirestoreClass().getQrDetails(this,qrDetail)


                       }
                       Barcode.TYPE_URL -> {
                           val title = barcode.url!!.title
                           val url = barcode.url!!.url

                           text_view.text= title+"\n"+url
                           val qrDetail = QrDetail(text_view.text.toString())

                           //Store the content of the variable qrDetails to the firestore
                           FirestoreClass().getQrDetails(this, qrDetail)

                       }
                   }

               }

           }

       }
        else{
            Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()
       }
    }

    /**
     * A function to take image by using camera of the device
     */

    private fun takeImage(){

        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)



            if(intent.resolveActivity(packageManager) != null){
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }





    }

    /**
     * on activity result funtion to set the image as Bitmap.
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val extras: Bundle? = data?.extras
                    imageBitmap= extras?.get("data") as Bitmap
                    image_view.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT)
            }
     }

    }

    fun qrScanDetails(){
        Toast.makeText(
            this,
            "you have Stored Qr details succesfully",
            Toast.LENGTH_LONG
        ).show()



    }

}

