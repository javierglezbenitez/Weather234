# _Practice1_
Desarrollo de Aplicaciones para Ciencia de datos(DACD)  
Curso 2023-2024  
Grado en Ciencia e Ingeniería de Datos  
Universidad Las Palmas de Gran Canaria(ULPGC)  


## _Functionality_
The project  collects data from an external API(Open Weather Map) and transmits it through a message broker(Active mq). The architecture consists of two main parts(modules): the first("prediction-provider") is responsible for obtaining and sending data, while the second("event-store-builder") manages the reception and structured storage of the information.This process is done every six hours
## _Resources Used_

#### Development Enviroment

The project is developed in Intellij. The language used for the implementation of the code is Java.

#### Version Control Tools

The Git tool is used for version control. Thanks to git it is possible to make changes in the development of the code without the possibility of deleting them.

#### Documentation Tools

I use MarkDown which provides a quick and concise overview of the project.

## _Design_

#### Patterns of designs
The application follows the Single Responsibility Principle (SRP), ensuring that each component has a single responsibility and improving code clarity and maintainability. In turn, it uses the Controller design pattern, part of the Model-View-Controller (MVC), to efficiently manage the interaction between the model and the view, achieving a clear separation of responsibilities.The observer pattern is used to asynchronously receive messages from a JMS topic. The AMQTopicSubscriber class acts as an observer that receives messages when they are available.

#### Design principle
The structure of the design is given in two  modules:
On one side we have, "prediction-provider".  This module collects the meteorlogical information(wind, speed, humidity...) and send it throught a message broker.  
On the other hand, there is "event-store-builder". In it the message sent it to  the broker is received and written in a directory.

#### Prediction-Provider
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/4d403ac4-741f-496e-ac01-8b2fe4ff6f2b)


#### Event-Store-Builder
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/7e39b2b6-e10e-4d3d-9086-8870a08dd325)

