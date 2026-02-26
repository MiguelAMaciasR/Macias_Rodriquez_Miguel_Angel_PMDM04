package dam.pmdm.spyrothedragon


import android.content.Intent
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


class GuiaFinalFragment : Fragment(R.layout.guia_final) {

    // Sonido final de cierre
    private lateinit var sonido_final: MediaPlayer
    // Sonido  al aparecer el bocadillo

    private lateinit var sonidoEnd: MediaPlayer

    /**
     * Pantalla final de la guía.
     * - Muestra resumen de pasos completados
     * - Aplica animación destacada
     * - Permite comenzar la aplicación
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sonido_final = MediaPlayer.create(requireContext(), R.raw.sonido_final)
        // Inicializar sonido bocadillo

        sonidoEnd = MediaPlayer.create(requireContext(), R.raw.end1)
        // ===============================
        // Bloquear botón atrás
        // ===============================
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No permitir salir hacia atrás
                }
            }
        )

        // ===============================
        // Animación especial de cierre
        // ===============================
        val bocadillo = view.findViewById<View>(R.id.bocadillo)

        // Estado inicial más dramático
        bocadillo.alpha = 0f
        bocadillo.scaleX = 0.5f
        bocadillo.scaleY = 0.5f
        bocadillo.rotation = -10f

        bocadillo.animate()
            .alpha(1f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .rotation(0f)
            .setDuration(600)
            .withEndAction {
                sonidoEnd.seekTo(0)
                sonidoEnd.start()

                bocadillo.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
        // ===============================
        // Botón Finalizar comenzar app
        // ===============================
        view.findViewById<Button>(R.id.btnFinalizar).setOnClickListener {

            sonido_final.start()

            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

            // Guardamos que la guía ya fue completada
            prefs.edit().putBoolean("guia_mostrada", true).apply()

            // Eliminamos el overlay y dejamos la app lista
            parentFragmentManager.beginTransaction()
                .remove(this@GuiaFinalFragment)
                .commit()
        }

        // Botón Saltar mismo comportamiento
        view.findViewById<Button>(R.id.btnSaltar).setOnClickListener {

            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

            prefs.edit().putBoolean("guia_mostrada", true).apply()

            parentFragmentManager.beginTransaction()
                .remove(this@GuiaFinalFragment)
                .commit()
        }
    }

    /**
     * Liberar recursos de audio
     */
    override fun onDestroyView() {
        super.onDestroyView()
        sonido_final.release()
        sonidoEnd.release()
    }
}