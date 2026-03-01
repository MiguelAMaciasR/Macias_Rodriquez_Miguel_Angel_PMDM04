package dam.pmdm.spyrothedragon

import android.R.attr.height
import android.R.attr.width
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

class MagicView(context: Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sparklePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Imagen del diamante
    private val bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.gems)

    // Escala controlada (no ocupar toda la pantalla)
    private var escala = 0.4f

    // Animaciones
    private var alpha = 120
    private var hue = 0f
    private var waveRadius = 0f

    init {
        wavePaint.style = Paint.Style.STROKE
        wavePaint.strokeWidth = 6f

        sparklePaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        // 1 0Cambio de color mágico (ciclo)
        hue += 2f
        if (hue > 360) hue = 0f
        val hsv = floatArrayOf(hue, 1f, 1f)
        val color = Color.HSVToColor(alpha, hsv)

        // 2 Brillo progresivo
        alpha += 4
        if (alpha > 255) alpha = 120

        // 3️ Escala controlada (pequeña animación de pulso)
        escala += 0.003f
        if (escala > 0.5f) escala = 0.4f

        val ancho = bitmap.width * escala
        val alto = bitmap.height * escala

        val left = centerX - ancho / 2
        val top = centerY - alto / 2
        val dest = RectF(left, top, left + ancho, top + alto)

        paint.alpha = alpha
        canvas.drawBitmap(bitmap, null, dest, paint)

        // 4 Ondas de energía
        waveRadius += 6f
        if (waveRadius > 300f) waveRadius = 0f

        wavePaint.color = color
        wavePaint.alpha = alpha - 80

        canvas.drawCircle(centerX, centerY, waveRadius, wavePaint)
        canvas.drawCircle(centerX, centerY, waveRadius + 60, wavePaint)

        // 5 Destellos mágicos
        sparklePaint.color = Color.WHITE
        sparklePaint.alpha = alpha

        for (i in 0..6) {
            val angle = Math.toRadians((i * 60 + hue).toDouble())
            val r = waveRadius / 2 + 40
            val x = centerX + (Math.cos(angle) * r).toFloat()
            val y = centerY + (Math.sin(angle) * r).toFloat()
            canvas.drawCircle(x, y, 6f, sparklePaint)
        }

        // Repetir animación
        postInvalidateOnAnimation()
    }
}