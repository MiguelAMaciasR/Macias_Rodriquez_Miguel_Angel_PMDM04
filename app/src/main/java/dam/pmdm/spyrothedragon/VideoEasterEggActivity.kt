package dam.pmdm.spyrothedragon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Activity encargada de reproducir el vídeo del Easter Egg.
 *
 * - Muestra el vídeo en pantalla completa.
 * - Oculta las barras del sistema (modo inmersivo).
 * - Ajusta el escalado para que el vídeo ocupe toda la pantalla.
 * - Al finalizar, cierra la actividad y vuelve a la pantalla anterior.
 */
class VideoEasterEggActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar el layout que contiene el VideoView
        setContentView(R.layout.activity_video)

        // Permite que el contenido ocupe toda la pantalla
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Ocultar barras del sistema (modo pantalla completa)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Referencia al VideoView del layout
        val videoView = findViewById<VideoView>(R.id.videoView)

        // Crear URI del vídeo almacenado en res/raw
        val uri = Uri.parse("android.resource://$packageName/${R.raw.video_spyro}")

        // Asignar el vídeo al VideoView
        videoView.setVideoURI(uri)

        /**
         * Se ejecuta cuando el vídeo está listo para reproducirse.
         * Aquí ajustamos el escalado para que ocupe toda la pantalla
         * manteniendo la proporción.
         */
        videoView.setOnPreparedListener { mp ->

            val videoWidth = mp.videoWidth
            val videoHeight = mp.videoHeight

            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            // Calcular escala proporcional
            val scaleX = screenWidth.toFloat() / videoWidth
            val scaleY = screenHeight.toFloat() / videoHeight

            // Elegimos la mayor escala para cubrir toda la pantalla
            val scale = maxOf(scaleX, scaleY)

            videoView.scaleX = scale
            videoView.scaleY = scale
        }

        // Iniciar reproducción automática
        videoView.start()

        /**
         * Cuando el vídeo termina:
         * - Se marca la guía como ya mostrada.
         * - Se cierra esta actividad.
         * - Se vuelve automáticamente a la pantalla anterior (Mundos).
         */
        videoView.setOnCompletionListener {

            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            prefs.edit().putBoolean("guia_mostrada", true).apply()

            // Cerrar la actividad de vídeo
            finish()
        }
    }
}