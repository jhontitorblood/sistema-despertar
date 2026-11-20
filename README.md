# SISTEMA — Despertar V1.2

Renovación visual RPG, misiones, atributos, logros con cinemática de despertar, ejército/colección, rutina A/B, sonidos, recordatorios en primer plano, respaldo de datos, PWA y proyecto Android WebView.

## Publicar en GitHub Pages
1. Haz un respaldo desde Ajustes en tu versión actual antes de actualizar.
2. Extrae este ZIP y sube **el contenido de la carpeta**, incluidos `assets/`, `android/` y `.github/`, a la raíz de tu repositorio (reemplazando los archivos web existentes). Si Windows no muestra `.github`, activa Explorador → Ver → Mostrar → Elementos ocultos. También se incluye `BUILD_APK_WORKFLOW.txt` por si necesitas crear ese workflow desde GitHub manualmente.
3. Espera a que Pages publique, luego abre el sitio y actualiza forzadamente/limpia datos del sitio si ves la versión anterior.

## Instalar desde el navegador
El sitio usa HTTPS, manifest con iconos y un Service Worker. En Android abre la URL en Chrome/Brave y usa el menú ⋮ → **Instalar aplicación** o **Añadir a pantalla principal**. El evento de instalación automática depende del navegador y sus criterios; el botón de la app da el prompt cuando está disponible y muestra instrucciones alternativas.

## Obtener APK Android
Este ZIP incluye proyecto Android y GitHub Actions. Al subirlo a GitHub:
- Ve a **Actions → Build Android APK → Run workflow** (o espera a que corra con un commit).
- Abre la ejecución verde y descarga el artifact **SISTEMA-Despertar-Android-APK**.
- Extrae el APK, envíalo al Android, ábrelo y autoriza instalar desde esa fuente si Android lo pide.

El APK de Actions es una build debug, no una publicación Play Store ni una APK firmada para distribución permanente. El flujo compila; debe verificarse en Actions y en el teléfono real. La APK usa WebView local y puede abrir la app sin conexión. Incluye recordatorios nativos de Android (misiones en días seleccionados, pausas periódicas), permiso de notificaciones, opción de posponer 10 minutos y restauración de alarmas tras reiniciar el teléfono. Android puede retrasar alarmas inexactas por ahorro de batería, por lo que no se consideran alarmas críticas.

## Datos
Los datos se conservan en `localStorage` bajo la clave `sistema_despertar_v1`, con migración de campos opcionales. Exporta un JSON antes de actualizar. No uses Restablecer salvo que quieras borrar el progreso.

## Limitaciones
En la versión web, los recordatorios dependen del navegador y pueden dejar de ejecutarse en segundo plano. En la APK se usan alarmas nativas de Android (inexactas, susceptibles a retrasos por ahorro de batería), con permiso de notificaciones y restauración tras reinicio.
