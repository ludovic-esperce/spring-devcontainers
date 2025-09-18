# Modèle MVC en Java

Compétences abordées :
- programmation de controllers pour répondre à des requêtes http (avec rendu de vue + Rest API)
- prise en main de la création de vues avec template Thymeleaf

## Framework Spring 

Ce découverte est basée sur le framework [Spring](https://spring.io/) et intègre les dépendances [Spring Boot](https://spring.io/projects/spring-boot).

Spring Boot est un outil qui accélère et simplifie le développement d'applications Web et de microservices avec Spring Framework grâce à trois fonctionnalités principales :
- facilité de configuration (notammment grâce aux [dépendances "starter"](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/README.adoc), consultez également ce [très bon article](https://www.baeldung.com/spring-boot-starters))
- les [mécanismes de configuration automatique](https://dev-mind.fr/blog/2022/spring_boot_starter_database_schema_initialization.html)
- l'outil [Spring Initalizr](https://start.spring.io/) pour rapidemment créer un nouveau projet
- l'outil de débogage [Actuator](https://www.jtips.info/Spring/Actuator)

En résumé, Spring avec l'outil Spring Boot permet de simplifier la mise en place et le débogage d'un projet, ce qui nous permet de nous concentrer sur le code métier.

## Démarrer le projet

Le plugin `spring-boot-maven-plugin` ajouté au fichier `pom.xml` permet de bénéficier du "goal" `run`.

Extrait de fichier de configuration `pom.xml` intégrant le plugin en question :
```xml
<plugin>
<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

Ce "goal" est très pratique puisqu'il vous permet de démarrer un serveur Tomcat et de déployer l'application en cours de développement automatiquement.

En ligne de commande vous pourrez démarrer le serveur avec l'instruction suivante :
```bash
mvn spring-boot:run
```

A partir de votre IDE il vous suffira de cliquer sur "run" à partir des ordres maven.
    
## Marche à suivre

Vous allez réaliser l'exerice en suivant une démarche TDD :
1. Complétez les TODO de façon à répondre aux attentes
2. Testez dès que vous le pouvez (lancez les tests unitaires et faites des tests fonctionnels)

N'oubliez pas de poussez régulièrement votre travail !
