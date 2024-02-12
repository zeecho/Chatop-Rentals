# ChâTop Rentals

This project was created using Spring Boot 3.2.2 (Java 17)

## How to run the project
### Download it

Clone the repository:

> git clone https://github.com/zeecho/Chatop-Rentals.git

Go inside the folder:

> cd Chatop-Rentals

### Setup the database

You need to create a new database and optionnally a new user (the project was coded using mariadb).

Create the database using your root account (or any account with the correct rights):

> mariadb -u root -p[your_password] -e "CREATE DATABASE rentals;"

Connect to mariadb using your root account

> mariadb -u root -p[your_password]

In mariadb's console, create the new user (optionnal) (replace [any_password_you_want] with the password you want ;) ) :

> CREATE USER 'rentals_user'@'localhost' IDENTIFIED BY '[any_password_you_want]';

Grant privileges:

> GRANT ALL PRIVILEGES ON rentals.* TO 'rentals_user'@'localhost';

Flush privileges (apply them):

> FLUSH PRIVILEGES;

Exit with Ctrl+D

Import the database structure (with root or your newly created user):

> mariadb -u root -p[your_password] < ressources/script.sql

### Create the application.properties file

Put something like this in src/main/resources/application.properties (or in a file outside the project if possible), replacing your database's credentials with the correct ones:

```
spring.application.name=chatop_rentals
server.port=3001

logging.level.root=error
logging.level.com.openclassrooms=info
logging.level.org.springframework.boot.web.embedded.tomcat=INFO

spring.jpa.hibernate.ddl-auto=none
spring.datasource.url=jdbc:mariadb://localhost:3306/rentals
spring.datasource.username=rentals_user
spring.datasource.password=my_password
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

springdoc.version=@springdoc.version@
springdoc.swagger-ui.use-root-path=true
server.forward-headers-strategy=framework
```

### Start the project

Build the application using maven (make sure Java 17 is installed):

> ./mvnw clean package

Execute the jar:

> cd chatop_build

> java -jar BankApplicationBackend-0.0.1-SNAPSHOT.jar
