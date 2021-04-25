Java SE - Programming Complete - Curso da Oracle
====

Este projeto segue o curso Java SE - Programming Complete.

### Observações

As instruções abaixo consisderam que você esteja na raiz do projeto e que você esteja utilizando um terminal linux, para
o prompt de comando substituir ` / ` por ` \ `. Também será necessário habilitar UFT-8 no prompt de comando.

O curso é dividido em 20 capítulos:

### 1. Introduction to Java

Não possui código equivalente no projeto

### 2. Primitive Types, Operators, and Flow Control Statements

Não possui código equivalente no projeto

### 3. Text, Date, Time, and Numeric Objects

Para esta parte é possível executar os arquivos individualmente

    java BigDecimalNumericFormat.java
    java DateTimeValueFormat.java
    java LocalizationStringDateNumber.java
    java StringAndStringBuilder.java

### 4. Classes and Objects

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-basic

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-basic.jar

### 5. Improved Class Design

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-improved

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-improved.jar

### 6. Inheritance

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-inheritance

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-inheritance.jar

### 7. Interfaces

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-interfaces

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-interfaces.jar

### 8. Arrays and Loops

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-arrays

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-arrays.jar

### 9. Collections

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-collections

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-collections.jar

### 10. Nested Classes and Lambda Expressions

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-nestedclasseslambda

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-nestedclasseslambda.jar

### 11. Java Streams API

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-streamapi

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-streamapi.jar

### 12. Handle Exceptions and Fix Bugs

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-handleexceptions

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-handleexceptions.jar

### 13. Java IO API

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-javaio

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-javaio.jar

### 14. Java Concurrency and Multithreading

Para gerar o executável relativo à esta parte pode-se executar o comando:

    ant clean dist-concurrencymultithreading

Para rodar o executável utilizar o comando:

    java -jar dist/artifacts/Shop-concurrencymultithreading.jar

### 15. Open Module Content

Para gerar o jlink relativo à esta lição rodar os seguintes comandos:

    ant clean compile
    jlink --output dist/artifacts/shop-app --add-modules labs.pm --module-path build --launcher shop=labs.pm/com.javase11.certification.productmanagement.modules.client.Shop

Será gerado um diretório `/dist/artifacts/shop-app/` que contém tudo o que é necessário para rodar a aplicação.
Em `bin/` existirá um arquivo `shop.bat` caso você esteja utilizando um ambiente windows ou um arquivo `shop.sh` caso
você esteja utilizando um ambiente linux, ao executar este arquivo você conseguirá rodar a aplicação.

### 16. Annotations

### 17. Java Database Connectivity

### 18. Java Security

### 19. Advanced Generics

### 20. Oracle Cloud Deployment
 
