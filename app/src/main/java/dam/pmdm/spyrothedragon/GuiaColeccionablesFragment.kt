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
import androidx.navigation.findNavController


class GuiaColeccionablesFragment : Fragment(R.layout.guia_coleccionables) {

    // Reproductor de sonido para los botones
    private lateinit var button_click: MediaPlayer

    /**
     * Se ejecuta cuando la vista del fragmento está lista.
     * Configura:
     * - Cambio automático a la pestaña Coleccionables
     * - Animación del bocadillo
     * - Navegación de la guía
     * - Bloqueo del botón atrás
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar sonido
        button_click = MediaPlayer.create(requireContext(), R.raw.button_click)

        // -------------------------
        // Cambiar automáticamente al tab Coleccionables
        // -------------------------
        requireActivity()
            .findNavController(R.id.navHostFragment)
            .navigate(R.id.navigation_collectibles)

        // -------------------------
        // Animación del bocadillo (entrada con rebote)
        // -------------------------
        val bocadillo = view.findViewById<View>(R.id.bocadillo)

        // Estado inicial
        bocadillo.alpha = 0f
        bocadillo.translationY = 120f
        bocadillo.scaleX = 0.7f
        bocadillo.scaleY = 0.7f

        // Animación de entrada
        bocadillo.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(500)
            .withEndAction {
                // Pequeño rebote final
                bocadillo.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }
            .start()

        // -------------------------
        // Bloquear botón atrás durante la guía
        // -------------------------
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No permitir salir con atrás
                }
            }
        )

        //-------------------------
        // Botón Siguiente Pantalla Info
        // -------------------------
        view.findViewById<View>(R.id.btnSiguiente).setOnClickListener {

            button_click.start()

            parentFragmentManager.beginTransaction()
                .replace(R.id.overlayContainer, GuiaInfoFragment())
                .commit()
        }

        // -------------------------
        // Botón Saltar cerrar guía
        // -------------------------
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
     * Liberar recursos de audio para evitar fugas de memoria
     */
    override fun onDestroyView() {
        super.onDestroyView()
        button_click.release()
    }
}