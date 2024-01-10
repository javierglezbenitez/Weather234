# _Final Project_
Desarrollo de Aplicaciones para Ciencia de datos(DACD)  
Curso 2023-2024  
Grado en Ciencia e Ingeniería de Datos  
Universidad Las Palmas de Gran Canaria(ULPGC)  

## Execution
Arguments in the 4 modules(the user must enter the necessary data):

HotelMain: arg[0] tcp://LAPTOP-93SSDTGR:61616(user have to put url of the computer to connect to the broker)

Main time: arg[0] 51cb3b621914382d96a450c0f3451582(user should put the apikey to connect to Open Weather Map), arg[1] tcp://LAPTOP-93SSDTGR:61616

DatalakeMain: arg[0] tcp://LAPTOP-93SSDTGR:61616, arg[1] directory (user introduce the directory of the datalake)

VacationMain: arg[0] tcp://LAPTOP-93SSDTGR:61616 , arg[1] jdbc:sqlite:C:/Users/cgsos/Downloads/Databases/datamart.db (user should put the path to save de database )

## _Functionality_
The project  collects data about weather and hotels, both in specific places, from two external APIs(Open Weather Map) and (Xotelo), this datas are transmited  to message broker(Active mq). The architecture consists of 4 main parts(modules): two parts(hotel-provider and prediction-provider) are to connect with data sources via API calls every six hours. Another module(datalake-builder) is to receive the data that is sent to the broker, from the two topics, and stored in a datalake, giving rise to a data history.is responsible for obtaining and sending data, while the second("event-store-builder") manages the reception and structured storage of the information. The last module (vacation-bussines-unit) is responsible for also receiving the data from the two topics, it is stored in a datamart, this means that every time it receives new data the database is updated, eliminating the data ancient. Finally, in this module the user interface (CLI) is created, a type of interface that allows communication between user and machine.
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

In addition, two fundamental design patterns are used in the Vacation-business-unit module.
On the one hand, making use of the Observer Pattern, through a subscription mechanism, when objects are sent or updated, the datamart itself is also modified, thus eliminating all old data that is outside of that update. Adversely the Command Pattern is also used to carry out operations that the user can do on the controller, in this case the user will be able to select what type of city they want to visit and how many days the stay will last.
#### Design principle
The project employs a modular architecture with distinct components utilizing various design patterns. The Weather and Hotel Providers connect to external APIs, employing the Adapter Pattern for standardized data collection and the Scheduled Task Pattern for periodic calls every six hours. The Message Broker (ActiveMQ) facilitates communication, leveraging the Publish-Subscribe and Message Queue Patterns for asynchronous, decoupled interactions.

The DataLake-Builder captures data from the broker, employing the Event Sourcing Pattern for historical records and the Observer Pattern to update the data lake based on broker events. The Event-Store-Builder manages structured storage, implementing both Event Sourcing and Command Query Responsibility Segregation (CQRS) patterns.

The Vacation Business Unit, responsible for data reception, storage in a datamart, and CLI creation, employs the Command Pattern for data updates, Data Mart Pattern for specialized storage, and Model-View-Controller (MVC) to separate the CLI from underlying logic. Overall, these patterns enhance modularity, scalability, and maintainability, facilitating specific responsibilities within the project.

Last but not least. The project uses a lamba architecture, since it makes use of the batch layer (datalake), receiving them immutably, and the speed drop (datamart), thus managing information in real time
#### prediction-Provider
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/4d403ac4-741f-496e-ac01-8b2fe4ff6f2b)


### hotel-provider
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/5722da5a-c223-4786-9a39-28ff24e9a4a6)


#### datalake-builder
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/046d57a7-36aa-4527-a7f7-5b4be0f29697)


### Vacation-bussines-unit
![image](https://github.com/javierglezbenitez/Weather234/assets/145259489/14b1e04d-26ae-4420-a87e-6b622baf8731)
