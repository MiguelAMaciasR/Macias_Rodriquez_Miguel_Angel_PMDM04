package dam.pmdm.spyrothedragon

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class GuiaInicioFragment : Fragment(R.layout.fragment_guia_inicio){

    private lateinit var sonido_inicio: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar sonido Inicio
        sonido_inicio = MediaPlayer.create(requireContext(), R.raw.sonido_inicio)

        // Bloqueamos el botón atrás
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No hacer nada (bloquea botón atrás)
                }
            }
        )


        // Botón Saltar cierra guía
        view.findViewById<Button>(R.id.btnSaltar).setOnClickListener {

            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)

            prefs.edit().putBoolean("guia_mostrada", true).apply()
            // Eliminamos el fragment
            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }



        // Botón COMENZAR va a GuiaPersonajesFragment
        val btnComenzar = view.findViewById<FrameLayout>(R.id.btnComenzar)
        btnComenzar.setOnClickListener {

            sonido_inicio.start()

            // Marcamos guía vista
            val prefs = requireActivity()
                .getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)
            prefs.edit().putBoolean("guia_mostrada", true).apply()

            // Navegamos a Personajes
            parentFragmentManager.beginTransaction()
                .replace(R.id.overlayContainer, GuiaPersonajesFragment())
                .commit()  // <-- reemplaza, no addToBackStack
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        sonido_inicio.release()
    }
}