package dam.pmdm.spyrothedragon

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class GuiaPersonajesFragment : Fragment(R.layout.guia_personajes) {

    // Reproductor de sonido para los botones de la guía
    private lateinit var button_click: MediaPlayer

    /**
     * Se ejecuta cuando la vista del fragmento ya está creada.
     * Aquí se configuran:
     * - Animaciones del bocadillo
     * - Navegación entre pantallas de la guía
     * - Bloqueo del botón atrás
     * - Easter Egg del personaje Ripto
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos el sonido del botón
        button_click = MediaPlayer.create(requireContext(), R.raw.button_click)

        // -------------------------
        // Animación del bocadillo (entrada)
        // -------------------------
        val bocadillo = view.findViewById<View>(R.id.bocadillo)

        // Estado inicial (fuera de pantalla y pequeño)
        bocadillo.alpha = 0f
        bocadillo.translationY = 120f
        bocadillo.scaleX = 0.7f
        bocadillo.scaleY = 0.7f

        // Animación combinada: aparece + sube + crece + efecto rebote
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
                    // No hacer nada → evita salir de la guía accidentalmente
                }
            }
        )

        // -------------------------
        // Botón Siguiente Ir a Mundos
        // -------------------------
        view.findViewById<Button>(R.id.btnSiguiente).setOnClickListener {

            button_click.start()

            // Cambiar a la pestaña Mundos
            requireActivity()
                .findNavController(R.id.navHostFragment)
                .navigate(R.id.navigation_worlds)

            // Mostrar la siguiente pantalla de la guía
            parentFragmentManager.beginTransaction()
                .replace(R.id.overlayContainer, GuiaMundosFragment())
                .commit()
        }

        // -------------------------
        // Botón Saltar Cerrar guía
        // -------------------------
        view.findViewById<Button>(R.id.btnSaltar).setOnClickListener {

            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

            // Guardamos que la guía ya se mostró
            prefs.edit().putBoolean("guia_mostrada", true).apply()

            // Eliminamos el fragmento overlay
            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

        // -------------------------
        // Easter Egg: pulsación larga sobre Ripto
        // -------------------------
        val ripto = view.findViewById<ImageView>(R.id.ripto)

        ripto.setOnLongClickListener {
            mostrarAnimacionMagica()
            true
        }
    }

    /**
     * Muestra la animación mágica del cetro de Ripto.
     * Añade temporalmente una vista personalizada (MagicView)
     * sobre toda la pantalla durante 4 segundos.
     */
    private fun mostrarAnimacionMagica() {

        val magicView = MagicView(requireContext())

        val layout = requireActivity()
            .findViewById<ViewGroup>(android.R.id.content)

        layout.addView(
            magicView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        // Eliminar la animación después de 4 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            layout.removeView(magicView)
        }, 4000)
    }

    /**
     * Liberamos el MediaPlayer para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        button_click.release()
    }
}