# Enunciado completo de la primera entrega del tp.

# Condiciones Técnicas para todas las entregas #

Sistema Operativo: Linux.
> Lenguajes de desarrollo: C, C++ o Java (el mismo para todo el trabajo práctico)
> Documentación Técnica: Para resolver el trabajo práctico deberán tomar los conocimientos teóricos de la materia y formular una solución, la misma será reflejada en la documentación técnica del grupo, dividida por entrega.
> Documentación de Usuario: Se describirá en un pequeño manual la forma de interactuar con el sistema, qué se espera de su funcionalidad, cómo utilizarlo y cómo interpretar los resultados.
> Pruebas de muestra: Son un conjunto de archivos de datos para utilizar en pruebas que son propuestas por el grupo. Pueden ser archivos de datos a utilizar con sus instrucciones o archivos scripts que realizan automáticamente todas las operaciones.


# Recomendaciones para todas las entregas: #

  * Realizar un diseño de arquitectura modular en el que las responsabilidades estén bien definidas y acotadas. Es decir crear librerías o clases que realicen tareas puntuales para luego poder extenderlas con la funcionalidad necesaria de las siguientes etapas del trabajo práctico.
  * Realizar un diagrama de las arquitectura completa de la solución, para que todo el grupo conozca cómo utilizar el código ajeno y para permitir identificar con facilidad la ubicación de los errores de código.
  * Comentar cada librería/clase y cada método/función con su objetivo.


# Primera entrega #

Fecha de entrega: Martes 31/3 hasta las 23:59.

## Requerimientos de usuario: ##


  * Carga de documentos:

> El usuario ingresa un documento de texto, indicándole al programa su ruta en el disco.
> El programa lee el documento, y muestra al usuario aquellos términos que no conoce (no se han ingresado nunca). Por cada término que el programa informa, el usuario ingresa ‘i’, luego Enter, y dice en el micrófono la palabra que lee, hecho esto ingresa ‘f’ y nuevamente Enter. El programa reproduce la palabra recién dicha y le pregunta al usuario si fue registrada correctamente. Si el usuario ingresa ‘n’ le vuelve a solicitar que diga la palabra; si ingresa ‘s’ la almacena.
> El programa almacena todos los términos en texto y en audio, y asocia cada texto con su respectivo audio.


  * Reproducción de palabras:

> También el usuario puede ingresar un texto y que el programa reproduzca el audio correspondiente a cada palabra conocida de ese texto.


## Requerimientos técnicos: ##

  * Archivo de texto:

> Se debe almacenar un archivo con registros de longitud variable, sin bloques, para el texto. La estructura lógica es (palabra, offset\_datos\_audio).


  * Archivo de audio:

> El archivo con el audio tiene registros de longitud variable sin bloques y su estructura lógica es (datos\_audio).