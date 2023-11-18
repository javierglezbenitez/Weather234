# _Practice1_
Desarrollo de Aplicaciones para Ciencia de datos(DACD)  
Curso 2023-2024  
Grado en Ciencia e Ingeniería de Datos  
Universidad Las Palmas de Gran Canaria(ULPGC)  


## _Functionality_
The programme attempts to obtain and store weather data from various locations and regular time intervals using a weather provider (an Appi) and storing the results in a SQLite database. This is achieved by using timers to run these operations automatically and periodically, specifically every 6 hours.

## _Resources Used_

#### Development Enviroment

The project is developed in Intellij. The language used for the implementation of the code is Java.

#### Version Control Tools

The Git tool is used for version control. Thanks to git it is possible to make changes in the development of the code without the possibility of deleting them.

#### Documentation Tools

I use MarkDown which provides a quick and concise overview of the project.

## _Design_

#### Patterns of designs
The application follows the Single Responsibility Principle (SRP), ensuring that each component has a single responsibility and improving code clarity and maintainability. In turn, it uses the Controller design pattern, part of the Model-View-Controller (MVC), to efficiently manage the interaction between the model and the view, achieving a clear separation of responsibilities.

#### Design principle
The structure of the design is given in two large packages:
On one side we have, "dacd.gonzalez.control".  This package collects the classes of the control and allows the functionality of the project, collecting and saving data from an Appi to a SQLite.  
On the other hand, there is "dacd.gonzalez.model". In it are the classes of the model that collects meteorological information (wind speed, humidity...) and the location (latitude and longitude).


#### StarUml  Diagram
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/09049bab-81da-459c-9ca8-a61b69c15804)
