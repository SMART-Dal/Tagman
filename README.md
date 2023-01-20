# Tagman	
Tagman is a web platform to create a manually annotated dataset of smells. The principle aim of Tagman is to facilitate users to annotate randomly selected code snippets for particular smells that could exist in the code and use the platform to create a sizeable quality dataset containing manually annotated samples for subjective code smells. We have considered four code smells we believe are subjective in nature: Multifaceted Abstraction, Complex Method, Long Method and Empty Catch Block.

## Getting Started
In order to run Tagman, the following dependencies are necessary.

### Prerequisites
#### Software
* Java version 8 or above
* Apache Maven
* Mysql Server running

Tested on Ubuntu 20.04.4 LTS.

### Setup
To setup the Tagman server, follow the following steps

1) Clone the repository
``` console
git clone https://github.com/SMART-Dal/Tagman.git
```
2) Run maven build
In the project directory, run the following command:
``` console
mvn clean install
```


## Configuration

The Tagman requires a running MySql server to use as a database for the annotations.  The following properties are configured for the server:
``` console
spring.datasource.url=jdbc:mysql://localhost:3306/tagman4	
spring.datasource.username=root
spring.datasource.password=my_strong_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
spring.jpa.properties.hibernate.current_session_context_class=org.springframework.orm.hibernate5.SpringSessionContext
```

The server sends and receives requests over https. Hence, we need a valid signed ppk certificate. The config for the same is as follows:
``` console

server.ssl.key-store=classpath:latest.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=pkcs12
```
###  Running the JAR

The server contains a self contained embedded tomcat server. Hence, the jar does not need to be deployed to a server. To start Tagman, run the following command:

``` console
java -jar tagman-1.0.jar
```


### Importing the samples
Tagman needs a corpus of samples – classes and methods to be shown to the user. The corpus can be built using [Tagman scripts](https://github.com/SMART-Dal/Tagman-scripts).

Once the samples are ready, log into the tagman as an admin and import them to the MySQL server. After completing this process, users can signup and start tagging the samples. 
