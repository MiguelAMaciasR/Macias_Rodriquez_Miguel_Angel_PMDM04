package dam.pmdm.spyrothedragon

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlin.jvm.java

/**
 * Fragmento de la guía – Pantalla 3
 * Explica la pestaña Mundos y permite:
 * - Cambiar a Coleccionables
 * - Saltar la guía
 * - Activar el Easter Egg (triple click en Mundo 1)
 */
class GuiaMundosFragment : Fragment(R.layout.guia_mundos) {

    // Sonido al pulsar botones
    private lateinit var button_click: MediaPlayer


    // Sonido  al aparecer el bocadillo
    private lateinit var sonidoIntro: MediaPlayer

    // Variables para detectar triple pulsación en el mismo mundo
    private var contadorClicks = 0
    private var ultimoMundoPulsado: Int = -1

    /**
     * Se ejecuta cuando la vista del fragmento ya está creada.
     * Aquí configuramos:
     * - Sonidos
     * - Animaciones
     * - Navegación de la guía
     * - Easter Egg
     * - Bloqueo del botón atrás
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar sonido del botón
        button_click = MediaPlayer.create(requireContext(), R.raw.button_click)
        // Inicializar sonido bocadillo
        sonidoIntro = MediaPlayer.create(requireContext(), R.raw.intromenu)
        // -------------------------
        // Animación del bocadillo (entrada suave)
        // -------------------------
        val bocadillo = view.findViewById<View>(R.id.bocadillo)

        bocadillo.alpha = 0f
        bocadillo.translationY = 100f
        bocadillo.scaleX = 0.8f
        bocadillo.scaleY = 0.8f

        bocadillo.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(600)
            .withEndAction {
                sonidoIntro.seekTo(0)
                sonidoIntro.start()
            }
            .start()

        // -------------------------
        // Bloquear botón atrás durante la guía
        // -------------------------
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No hacer nada → evita salir de la guía
                }
            }
        )

        // -------------------------
        // Botón Siguiente Coleccionables
        // -------------------------
        view.findViewById<View>(R.id.btnSiguiente).setOnClickListener {

            button_click.start()

            // Cambiar pestaña real de la app
            requireActivity()
                .findNavController(R.id.navHostFragment)
                .navigate(R.id.navigation_collectibles)

            // Mostrar siguiente pantalla de la guía (overlay)
            parentFragmentManager.beginTransaction()
                .replace(R.id.overlayContainer, GuiaColeccionablesFragment())
                .commit()
        }

        // -------------------------
        // Botón Saltar cerrar guía completamente
        // -------------------------
        view.findViewById<Button>(R.id.btnSaltar).setOnClickListener {

            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

            prefs.edit().putBoolean("guia_mostrada", true).apply()

            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

        // -------------------------
        // Easter Egg: Triple pulsación en Mundo 1
        // -------------------------
        val mundo1 = view.findViewById<ImageView>(R.id.mundo1)

        mundo1.setOnClickListener {

            // Si pulsa el mismo mundo aumentar contador
            if (ultimoMundoPulsado == R.id.mundo1) {
                contadorClicks++
            } else {
                // Si cambia de mundo reiniciar contador
                contadorClicks = 1
                ultimoMundoPulsado = R.id.mundo1
            }

            // Si llega a 3 pulsaciones activar Easter Egg
            if (contadorClicks == 3) {
                contadorClicks = 0
                abrirVideoEasterEgg()
            }
        }
    }

    /**
     * Abre el vídeo del Easter Egg.
     * - Marca la guía como vista
     * - Elimina el overlay
     * - Lanza la actividad de vídeo en pantalla completa
     */
    private fun abrirVideoEasterEgg() {

        val prefs = requireActivity()
            .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

        prefs.edit().putBoolean("guia_mostrada", true).apply()

        // Quitar la guía (deja visible la app debajo)
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()

        // Abrir actividad de vídeo
        val intent = Intent(requireContext(), VideoEasterEggActivity::class.java)
        startActivity(intent)
    }

    /**
     * Liberar recursos de audio para evitar fugas de memoria
     */
    override fun onDestroyView() {
        super.onDestroyView()
        button_click.release()
        sonidoIntro.release()
    }
}




