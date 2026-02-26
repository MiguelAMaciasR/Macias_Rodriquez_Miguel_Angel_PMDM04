package dam.pmdm.spyrothedragon

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog


class GuiaInfoFragment : Fragment(R.layout.guia_info) {

    // Reproductor de sonido para botones
    private lateinit var button_click: MediaPlayer

    /**
     * Pantalla 5 de la guía.
     * Explica el icono de información de la ActionBar.
     * - Muestra bocadillo animado
     * - Bloquea botón atrás
     * - Permite avanzar o saltar la guía
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar sonido
        button_click = MediaPlayer.create(requireContext(), R.raw.button_click)

        // ===============================
        // Animación del bocadillo (entrada con rebote)
        // ===============================
        val bocadillo = view.findViewById<View>(R.id.bocadillo)

        // Estado inicial
        bocadillo.alpha = 0f
        bocadillo.translationY = 120f
        bocadillo.scaleX = 0.7f
        bocadillo.scaleY = 0.7f

        // Animación
        bocadillo.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(500)
            .withEndAction {
                bocadillo.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }
            .start()

        // ===============================
        // Bloquear botón atrás durante la guía
        // ===============================
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No permitir salir con atrás
                }
            }
        )

        // ===============================
        // Botón Siguiente → Pantalla Final
        // ===============================
        view.findViewById<View>(R.id.btnSiguiente).setOnClickListener {

            button_click.start()

            parentFragmentManager.beginTransaction()
                .replace(R.id.overlayContainer, GuiaFinalFragment())
                .commit()
        }

        // ===============================
        // Botón Saltar → cerrar guía
        // ===============================
        view.findViewById<Button>(R.id.btnSaltar).setOnClickListener {

            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

            prefs.edit().putBoolean("guia_mostrada", true).apply()

            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }
    }

    /**
     * Liberar recursos de audio
     */
    override fun onDestroyView() {
        super.onDestroyView()
        button_click.release()
    }
}