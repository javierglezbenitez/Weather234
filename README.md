# _Práctica1_
Desarrollo de Aplicaciones para Ciencia de datos(DACD)  
Curso 2023-2024  
Grado en Ciencia e Ingeniería de Datos  
Universidad Las Palmas de Gran Canaria(ULPGC)  


## _Funcionalidad_
 El programa se encarga de obtener y almacenar datos meteorológicos de diversas ubicaciones e intervalos regulares de tiempo utilizando un proveedor de clima(una Appi) y almacenando los resultados en una base de datos SQLite. Esto se logra mediante el uso de temporizadores para ejecutar estas operaciones de manera automática y periódica, específicamente cada 6 horas.

## _Recursos utilizados_

#### Entornos de desarrollos

El proyecto se desarrolla en Intellij. El lenguaje utilizado para la implementación del código es Java.

#### Herramientas de Control de Versiones

Utilizo la herramienta Git para el control de versiones. Gracias a git se pueden realizar cambios en el desarrollo del codigo sin que exista la posibilidad de que se puedan borrar

#### Herramientas de Documentación

Utilizo MarkDown que proporciona una visión rápida y concisa del proyecto.




## Diseño

#### Patrones de diseños
La aplicación sigue el principio de Responsabilidad Única (SRP), garantizando que cada componente tenga una única responsabilidad y mejorando la claridad y mantenibilidad del código. A su vez, utiliza el patrón de diseño Controlador, parte del Modelo-Vista-Controlador (MVC), para gestionar eficientemente la interacción entre el modelo y la vista, logrando una clara separación de responsabilidades.


#### Principio de diseño
Estructura del diseño viene dada en dos grandes paquetes:
Por un lado tenemos, "dacd.gonzalez.control".  En este paquete recoge las clases del control y permite hacer la funcionalidad del proyecto, recoger y guardar datos de una Appi a un SQLite.  
Por otra parte esta, "dacd.gonzalez.model". En él se encuentran las clases del model que recoge  información meteorológica(velocidad del viento, humedad...) y la ubicación(Latitud y longitud).


#### Diagrama del Proyecto en StarUml
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/09049bab-81da-459c-9ca8-a61b69c15804)
