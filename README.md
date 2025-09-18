# Mise en place de "devcontainers"

## Objectifs et intérêt

Ce projet a pour objectif de mettre en avant la mise en place d'un environnement de développement basé sur des  "devcontainers" pour la stack technique suivante :
- une application web basée sur Spring boot (code serveur) ;
- une base de données relationnelle PostgreSQL.

L'approche en "devcontainer" a de multiples avantages :
- mutualisation des environnement de développement pour le travail en équipe :
  - les fichiers de configuration des conteneurs pourront être partagé via un dépôt Git et chaque membre d'une équipe pourra bénéficier rapidemment du même environnement
- création d'environnement isolés du système :
  - la conteneurisation permet de d'isoler les outils de développement du système hôte. Ceci a pour avantage de ne pas intéférer avec les applications du système hôte (sandboxing).
- facilitation du changement d'environnement :
  - il sera, par exemple, plus facile de changer rapidemment de version en configurant le conteneur et non pas le système hôte

> [!IMPORTANT]
> La présentation se base sur  une approche utilisant VSCode.
>
> De nombreuses ressources sont disponibles en ligne si vous utilisez d'autres IDE, le principe reste identique.

## Principe de base

Le principe de développement dans un "dev container" est de pouvoir utiliser un conteneur comme un environnement de travail pré-configuré.

Ce conteneur pourra contenir les outils de développement (par exemple le compilateur et l'outil de gestion de dépendance) et toutes applications nécessaire au lancement d'une application (par exemple un JRE).

Le **système de volumes** de la conteneurisation est utilisé pour pouvoir retrouver les fichiers constitutifs d'un projet à l'intérieur du conteneur.

Une fois ce conteneur établi, l'IDE est en capacité de s'y connecter via un module appelé "VSCode server" situé dans le conteneur de développement :

![Fonctionnement de VSCode avec un "dev container"](img/architecture-containers.png)

## Configuration de "dev containers"

La création d'un "dev container" est rendue possible grâce à l'utilisation d'un fichier de configuration `.json`.

L'approche traditionnelle consiste à positionner ce fichier dans un dossier situé à la racine du projet :
```js
projet
├───.devcontainer // répertoire contenant le fichier de configuration
│   └───devcontainer.json
├───src
└───pom.xml // exemple basé sur un projet Maven avec "pom.xml"
```

Ce fichier de configuration est propre à l'outil "dev container".

Voici un premier exemple de fichier de configuration pour un conteneur Java :
```json
// README at: https://github.com/devcontainers/templates/tree/main/src/java
{
    // Nom du "dev container", champ libre
    "name": "Java",
    // Utilisation d'une image provenant d'un dépôt Microsoft.
    // Il est aussi possible de créer une image adaptée en utilisant un fichier "Dockerfile" ou "docker-compose.yml"
    // Ici, un conteneur spécifique à Java est utilisé (il contient les outils de compilation)
    // Pour plus d'information sur ce conteneur veuillez consulter la documentation : https://hub.docker.com/r/microsoft/devcontainers-java
    "image": "mcr.microsoft.com/devcontainers/java:21-bullseye",
    // Ajout d'une liste de fonctionnalités à ajouter au conteneur
    // Pour plus de détails : https://github.com/devcontainers/features/tree/main/src/java
    "features": {
          "ghcr.io/devcontainers/features/java:1": {
                  "version": "none",
                  "installMaven": "true",
                  "mavenVersion": "3.9.10",
                  "installGradle": "false"
          }
    }
}
```

Il sera possible d'intégrer un tel fichier à un projet existant.

> [!IMPORTANT]
> Il n'est pas tout le temps évident de choisir les images de conteneur sur lesquelles se baser. L'exemple présenté utilise l'image "java:21-bullseye".
> Pour en apprendre plus sur les différentes images utilisables il vous est fortement conseillé de lire l'article disponible [ici](https://medium.com/@faruk13/alpine-slim-bullseye-bookworm-noble-differences-in-docker-images-explained-d9aa6efa23ec)
> A votre avis, quelle serait l'image la plus adaptée pour la mise en place d'un conteneur de production le plus optimisé en mémoire ?

### Utilisation du client en console et/ou l'IDE

"dev container" peut être vue comme une sur-couche de Docker permettant de manipuler facilement des conteneurs dédiés au développement.

La manipulation de ces conteneurs peut être faite via deux approches :
- utilisation de "devcontainer cli", le client en **console** ;
- utilisation de l'interface de l'IDE.

L'installation du client en console peut se faire en suivant la procédure disponible [ici](https://code.visualstudio.com/docs/devcontainers/devcontainer-cli).

Pour une utilisation directement dans VSCode il est possible d'utiliser [cette extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers). Si vous installez l'extension vous n'aurez pas besoin d'installer l'outil en ligne de commande, il est intégré.
