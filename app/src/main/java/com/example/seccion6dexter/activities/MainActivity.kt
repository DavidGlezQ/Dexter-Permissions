package com.example.seccion6dexter.activities

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.seccion6dexter.R
import com.example.seccion6dexter.enums.PermissionStatusEnum
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import kotlinx.android.synthetic.main.activity_main.*
//Para poder poner el tema en oscuro:
//1.-Lo primero es cambiar el colorAcent por #009688 em el archivo colors.
//2.-Cambiar en el archivo styles parent="android:Theme.Material" para poder hacer uso del color.
//3.-Como ultimo paso debemos de heredar de Activity ya que estamos trabajando con el minimo de sdk 21 (version 5 de android), por que es necesario cambiarlo.
//En versiones anteriores a esta no funciona.
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonClicks()
    }

    private fun setButtonClicks(){
        buttonCamera.setOnClickListener{checkCameraPermission()}
        buttonContacts.setOnClickListener{checkContactsPermission()}
        buttonAudio.setOnClickListener{checkAudioPermission()}
        buttonAll.setOnClickListener{checkAllPermission()}
    }
    //Permisos.
    //Permiso permiso con composite, cuadro de dialogo.
    //private fun checkCameraPermission() = setCameraPermissionHandlerWithDialog()
    //Permiso permiso con composite, SnackBar.
    private fun checkCameraPermission() = setCameraPermissionHandlerWithSnackBar()
    //private fun checkCameraPermission() = setPermissionHandler(Manifest.permission.CAMERA, textViewCamera)
    private fun checkContactsPermission() = setPermissionHandler(Manifest.permission.READ_CONTACTS, textViewContacts)
    private fun checkAudioPermission() = setPermissionHandler(Manifest.permission.RECORD_AUDIO, textViewAudio)
    private fun checkAllPermission(){
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.RECORD_AUDIO)
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    //En caso de ser null no hace nada de lo que esta dentro
                    report?.let {
                        for (permission in report.grantedPermissionResponses){
                            when(permission.permissionName){
                                Manifest.permission.CAMERA -> setPermissionStatus(textViewCamera, PermissionStatusEnum.GRANTED)
                                Manifest.permission.READ_CONTACTS -> setPermissionStatus(textViewAudio, PermissionStatusEnum.GRANTED)
                                Manifest.permission.RECORD_AUDIO -> setPermissionStatus(textViewAudio, PermissionStatusEnum.GRANTED)
                            }
                        }
                        for (permission in report.deniedPermissionResponses){
                            when (permission.permissionName){
                                Manifest.permission.CAMERA -> {
                                    if (permission.isPermanentlyDenied){
                                        setPermissionStatus(textViewCamera, PermissionStatusEnum.PERMANENTLY_DENIED)
                                    }else {
                                        setPermissionStatus(textViewCamera, PermissionStatusEnum.DENIED)
                                    }
                                }
                                Manifest.permission.RECORD_AUDIO -> {
                                    if (permission.isPermanentlyDenied){
                                        setPermissionStatus(textViewAudio, PermissionStatusEnum.PERMANENTLY_DENIED)
                                    }else {
                                        setPermissionStatus(textViewAudio, PermissionStatusEnum.DENIED)
                                    }
                                }
                                Manifest.permission.READ_CONTACTS -> {
                                    if (permission.isPermanentlyDenied){
                                        setPermissionStatus(textViewContacts, PermissionStatusEnum.PERMANENTLY_DENIED)
                                    }else {
                                        setPermissionStatus(textViewContacts, PermissionStatusEnum.DENIED)
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: MutableList<PermissionRequest>?, token: PermissionToken) {
                    token.continuePermissionRequest()
                }

            }).check()
    }
    //Handler para los permisos (todos los permisos).
    private fun setPermissionHandler(permission: String, textView: TextView){
        //Sintaxis que implementa Dexter.
        Dexter.withContext(this)
            //llamando a los permisos de la camara desde el Manifest.
            .withPermission(permission)
            //Se implementan los tres metodos necesarios.
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                setPermissionStatus(textView, PermissionStatusEnum.GRANTED)
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Para este caso se verifican dos casos si es o no permanente.
                    if (response.isPermanentlyDenied){
                        //En caso de solo denegarlo una vez sin chequear el checkBox.
                        setPermissionStatus(textView, PermissionStatusEnum.PERMANENTLY_DENIED)
                    }else{
                        //En caso de aceptar el checkBox para no volver a mostrarlo.
                        setPermissionStatus(textView, PermissionStatusEnum.DENIED)
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    //Esto es para que la app vuelva a preguntar en caso de denegar el permiso.
                    token.continuePermissionRequest()
                }
            }).check()
    }
    //Funcion para los textos.
    private fun setPermissionStatus(textView: TextView, status: PermissionStatusEnum){
        when (status){
            PermissionStatusEnum.GRANTED -> {
                //Se cambia el texto del permiso junto con su color.
                textView.text = getString(R.string.permission_status_granted)
                textView.setTextColor(ContextCompat.getColor(this, R.color.ColorPermissionStatusGranted))
            }
            PermissionStatusEnum.DENIED -> {
                //Se cambia el texto del permiso junto con su color.
                textView.text = getString(R.string.permission_status_denied)
                textView.setTextColor(ContextCompat.getColor(this, R.color.ColorPermissionStatusDenied))
            }
            PermissionStatusEnum.PERMANENTLY_DENIED -> {
                //Se cambia el texto del permiso junto con su color.
                textView.text = getString(R.string.permission_status_permanently)
                textView.setTextColor(ContextCompat.getColor(this, R.color.ColorPermissionStatusPermanentlyDenied))
            }
        }
    }
    //Mismos permisos pero con composite, cuadro de dialogo pero si se deniega manda una alerta al usuario.
    private fun setCameraPermissionHandlerWithDialog(){
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(this)
            .withTitle("Camera Permission")
            .withMessage("Camera permission is needed to take pictures")
            .withButtonText(android.R.string.ok)
            .withIcon(R.mipmap.ic_launcher)
            .build()
        val permission = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                setPermissionStatus(textViewCamera, PermissionStatusEnum.GRANTED)
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                if (response.isPermanentlyDenied){
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.PERMANENTLY_DENIED)
                }else{
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.DENIED)
                }
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }
        }

        val composite = CompositePermissionListener(permission, dialogPermissionListener)

        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(composite)
            .check()
    }
    //Mismos permisos pero con composite, SnackBar pero si se deniega manda una alerta al usuario.
    private fun setCameraPermissionHandlerWithSnackBar(){
        val permission = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                setPermissionStatus(textViewCamera, PermissionStatusEnum.GRANTED)
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                if (response.isPermanentlyDenied){
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.PERMANENTLY_DENIED)
                }else{
                    setPermissionStatus(textViewCamera, PermissionStatusEnum.DENIED)
                }
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                token?.continuePermissionRequest()
            }
        }

        val snackBarPermissionsListener = SnackbarOnDeniedPermissionListener.Builder
            .with(root, "Camera is needed to take pictures")
            .withOpenSettingsButton("Setting")
            .withCallback(object : Snackbar.Callback(){
                override fun onShown(sb: Snackbar?) {
                    //Even handler for when rhe given SnackBar is visible.
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    //Even handler for when the given SnackBar has been dismissed
                }
            }).build()
        val composite = CompositePermissionListener(permission, snackBarPermissionsListener)

        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(composite)
            .check()
    }
}
