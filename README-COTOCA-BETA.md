# HÉROE DEL ROCK — Beta jugable de Cotoca

Esta beta es una experiencia independiente dentro del repositorio de SISTEMA Despertar. No reemplaza ni modifica los archivos de la aplicación original.

## Abrir la beta

Una vez que los archivos estén publicados por GitHub Pages, abre:

`https://jhontitorblood.github.io/sistema-despertar/cotoca.html`

También puedes abrir `cotoca.html` con un servidor local sencillo (por ejemplo, VS Code Live Server). El guardado se realiza en el almacenamiento local del navegador bajo la clave `hero_rock_cotoca_beta_v1`, separada del guardado de SISTEMA Despertar.

## Contenido jugable

- Cinco sectores de Cotoca: Centro, Caminos del Curichi, Humedales del Jichi, Campamento de los Bravos y Santuario de las Aguas Profundas.
- Campaña narrativa de ocho misiones, diálogos y una misión secreta.
- Combate por turnos, ataques básicos, habilidad de energía, defensa y consumibles.
- Siete personajes especiales: cuatro capitanes Épicos, dos lugartenientes Míticos y un líder Legendario.
- Conversión espectral con tres intentos individuales, probabilidades por rareza y reaparición temporal de 12/24/36 horas.
- Legión coleccionable, formación inicial de tres seguidores y sinergias.
- Progresión de nivel, atributos, oro, inventario, equipo, refugio y jefe secreto Vharzûl.
- Guardado automático, exportación e importación de partida JSON.

## Controles de prueba

1. Completa «Territorio desconocido».
2. En «Los hombres del Curichi», vence dos encuentros con vigilantes.
3. Continúa la historia hasta el enfrentamiento de Aruma.
4. Después de derrotar a un personaje especial, abre Legión, revisa su ficha e intenta convertirlo.
5. Completa la campaña; vuelve al mapa para buscar tres sellos antiguos en tres sectores diferentes y desbloquear a Vharzûl.

## Alcance y limitaciones de esta beta

Es un prototipo de sistemas, no la versión final. Los personajes se representan temporalmente con iconografía y arte CSS; todavía no se integraron ilustraciones finales ni música de batalla. El combate permite validar el bucle básico, pero algunas habilidades individuales, IA avanzada, daño/incapacitación de seguidores, eventos postconquista y recompensas completas de sets aún requieren una siguiente iteración. Los temporizadores de reaparición usan la hora real del dispositivo y continúan aunque se cierre la página.

## Archivos

- `cotoca.html`: entrada independiente de la beta.
- `cotoca.css`: interfaz responsive y estética rock oscura.
- `cotoca-data.js`: contenido narrativo, zonas, personajes, enemigos, objetos y sinergias.
- `cotoca-engine.js`: estado, progresión, misiones, inventario y conversión.
- `cotoca-ui.js`: interfaz, navegación y combate.

El progreso de esta beta es independiente de SISTEMA Despertar.
