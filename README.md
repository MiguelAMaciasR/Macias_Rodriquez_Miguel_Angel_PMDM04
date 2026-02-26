

El objetivo principal de la aplicación es permitir al usuario explorar el universo de Spyro mediante distintas secciones organizadas en pestañas. La aplicación incluye una guía interactiva inicial que explica el funcionamiento de cada apartado, así como un Easter Egg oculto que mejora la experiencia del usuario.

El proyecto integra navegación estructurada, animaciones, reproducción de sonido y persistencia de datos locales.

Características principales

Navegación por pestañas
La aplicación se organiza en tres secciones principales:

Personajes

Mundos

Coleccionables

Guía interactiva inicial

Explicación paso a paso de cada sección de la aplicación.

Bocadillos informativos animados.

Bloqueo del botón atrás durante la guía para asegurar el recorrido completo.

Transiciones entre pantallas de guía mediante fragments.

Resumen final con los pasos completados.

Reproducción de sonidos

Sonidos al mostrar determinados bocadillos informativos.

Sonido específico al finalizar la guía.

Easter Egg

Activación mediante triple pulsación sobre un mundo concreto.

Reproducción de vídeo en pantalla completa.

Retorno automático a la sección correspondiente tras finalizar el vídeo.

Persistencia de datos

Uso de SharedPreferences para almacenar si la guía ya ha sido mostrada, evitando que se repita en futuros inicios de la aplicación.

Animaciones

Animaciones de aparición con escala y transición.

Efectos de rebote en elementos informativos.

Pantalla de información

Sección accesible mediante icono en la barra superior.

Explicación integrada dentro de la guía interactiva.

Tecnologías utilizadas

Kotlin como lenguaje principal de desarrollo.

Android Studio como entorno de desarrollo.

Fragments para estructurar la interfaz en módulos independientes.

Navigation Component para la gestión de la navegación entre secciones.

MediaPlayer para la reproducción de sonidos y vídeo.

SharedPreferences para almacenamiento local de preferencias.

RecyclerView para la visualización eficiente de listas.

Material Components para la implementación de una interfaz coherente y moderna.

Instrucciones de uso

Clonamos el repositorio:

git clone https://github.com/tu-usuario/tu-repositorio.git

Abrimos el proyecto en Android Studio.

Sincronizamos las dependencias de Gradle en caso de que el entorno lo solicite.

Ejecutamos la aplicación en un emulador o dispositivo físico Android.

No se requiere configuraciones externas adicionales.

Conclusiones del desarrollador

El desarrollo de este proyecto me ha permitido aplicar de manera práctica los conceptos fundamentales de la programación en Android, especialmente en lo relativo a la navegación entre fragments, la gestión del ciclo de vida, la animación de elementos de interfaz y la persistencia de datos locales.

Uno de los principales retos fue gestionar correctamente el overlay de la guía interactiva sin interferir en la interacción con los elementos de fondo, así como sincronizar adecuadamente animaciones y sonidos.

El proyecto ha contribuido a consolidar conocimientos sobre arquitectura de aplicaciones Android y buenas prácticas en organización del código.
