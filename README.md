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
Patrones y Principios de Diseño Utilizados
La aplicación sigue el principio de responsabilidad única y utiliza el patrón de diseño de controlador. A través de la separación de responsabilidades, se logra un código más claro y mantenible. El código está estructurado en tres paquetes principales:


#### Principio de diseño
dacd.gonzalez.control:  En este paquete recoge las clases del control.
dacd.gonzalez.model: Contiene las clases del model que recoge  información meteorológica(velocidad del viento, humedad...) y la ubicación(Latitud y longitud).


#### Diagrama del Proyecto en StarUml
![image](https://github.com/javierglezbenitez/javierglezbenitez/assets/145259489/0648d2e7-3c9d-4458-a252-16d51f57e309)
