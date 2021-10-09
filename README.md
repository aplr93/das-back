# das-back
Esse repositório é o backend do trabalho de desenvolvimento web

----
- Este branch (*webService-example*) contém um exemplo de web service REST baseado em Spring.   
- Ele foi originalmente desenvolvido para funcionar em conjunto com a aplicação web **crud-pessoa** desenvolvida em Angular
(disponível em https://github.com/chsev/crud-pessoa)
- Assume um ambiente Java 8 + Spring + Maven

### Como rodar a aplicação
 A partir de "/das-back":
```
cd usuario-rest-service
./mvnw spring-boot:run
```
- teste com http://localhost:8080/usuarios

### ou pelo .jar (build + run)
```
cd usuario-rest-service
./mvnw clean package
java -jar target/usuario-rest-service-0.0.1-SNAPSHOT.jar
```

