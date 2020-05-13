package com.example.seccion6dexter

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
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

        //Evento del boton para permisos de la camara llamando a su funcion.
        buttonCamera.setOnClickListener{checkCameraPermission()}
        //Evento del boton para permisos de los contactos llamando a su funcion.
        buttonContacts.setOnClickListener{checkContactsPermission()}
        //Evento del boton para permisos del audio llamando a su funcion.
        buttonAudio.setOnClickListener{checkAudioPermission()}
    }

    //Funcion para checar permisos de la camara.
    private fun checkCameraPermission(){
        val context = this
        //Sintaxis que implementa Dexter.
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)//llamando a los permisos de la camara desde el Manifest.
            .withListener(object : PermissionListener{//Se implementan los tres metodos necesarios.
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    //Se cambia el texto del permiso junto con su color.
                    textViewCamera.text = getString(R.string.permission_status_granted)
                    textViewCamera.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusGranted))
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Para este caso se verifican dos casos si es o no permanente.
                    if (response.isPermanentlyDenied){//En caso de solo denegarlo una vez sin chequear el checkBox
                        textViewCamera.text = getString(R.string.permission_status_camera_permanently)
                        textViewCamera.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusPermanentlyDenied))
                    }else{//En caso de aceptar el checkBox para no volver a mostrarlo
                        textViewCamera.text = getString(R.string.permission_status_denied)
                        textViewCamera.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusDenied))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    //Esto es para que la app vuelva a preguntar en caso de denegar el permiso.
                    token.continuePermissionRequest()
                }
            }).check()
    }
    //Funcion para checar permisos de los contactos.
    private fun checkContactsPermission(){
        val context = this
        //Sintaxis que implementa Dexter.
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_CONTACTS)//llamando a los permisos de la camara desde el Manifest.
            .withListener(object : PermissionListener{//Se implementan los tres metodos necesarios.
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                //Se cambia el texto del permiso junto con su color.
                textViewContacts.text = getString(R.string.permission_status_granted)
                textViewContacts.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusGranted))
            }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Para este caso se verifican dos casos si es o no permanente.
                    if (response.isPermanentlyDenied){//En caso de solo denegarlo una vez sin chequear el checkBox
                        textViewContacts.text = getString(R.string.permission_status_camera_permanently)
                        textViewContacts.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusPermanentlyDenied))
                    }else{//En caso de aceptar el checkBox para no volver a mostrarlo
                        textViewContacts.text = getString(R.string.permission_status_denied)
                        textViewContacts.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusDenied))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    //Esto es para que la app vuelva a preguntar en caso de denegar el permiso.
                    token.continuePermissionRequest()
                }
            }).check()
    }
    //Funcion para checar permisos del audio.
    private fun checkAudioPermission(){
        val context = this
        //Sintaxis que implementa Dexter.
        Dexter.withContext(this)
            .withPermission(Manifest.permission.RECORD_AUDIO)//llamando a los permisos de la camara desde el Manifest.
            .withListener(object : PermissionListener{//Se implementan los tres metodos necesarios.
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                //Se cambia el texto del permiso junto con su color.
                textViewAudio.text = getString(R.string.permission_status_granted)
                textViewAudio.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusGranted))
            }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    //Para este caso se verifican dos casos si es o no permanente.
                    if (response.isPermanentlyDenied){//En caso de solo denegarlo una vez sin chequear el checkBox
                        textViewAudio.text = getString(R.string.permission_status_camera_permanently)
                        textViewAudio.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusPermanentlyDenied))
                    }else{//En caso de aceptar el checkBox para no volver a mostrarlo
                        textViewAudio.text = getString(R.string.permission_status_denied)
                        textViewAudio.setTextColor(ContextCompat.getColor(context, R.color.ColorPermissionStatusDenied))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    //Esto es para que la app vuelva a preguntar en caso de denegar el permiso.
                    token.continuePermissionRequest()
                }
            }).check()
    }
}
