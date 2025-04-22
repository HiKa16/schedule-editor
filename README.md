# Éditeur d'emploi du temps en JavaFX

Application JavaFX permettant de créer, organiser et visualiser un emploi du temps avec une interface graphique, avec la possibilité de l'exporter au format HTML.

## Structure du projet

### Modélisation (src/main/java/schedule)
L'emploi du temps est modélisé à l'aide des classes suivantes : 
- **Event** : représente un évènement avec ses détails (nom, description)
- **PlannedEvent** (hérite de **Event**) : représente un évènement planifié, associé à un créneau horaire
- **TimeSlot** : représente un créneau horaire (date, heure de début et de fin)
- **Schedule** : structure principale, construit et gère les évènements planifiés

### Interface graphique (src/main/java/app)
L'application principale **EditorApp** permet de : 
- afficher le planning sur une plage de jours et de créneaux horaires définis par la classe **Config** (modifiables par l'utilisateur, voir ci-dessous)
- ajouter, modifier et supprimer des évènements via une interface simple
- afficher les détails d'un évènement en cliquant dessus
- exporter le planning en HTML interactif avec la classe **ScheduleExporter** 

## Technologies utilisées
- **Java 21**
- **JavaFX**
- **Maven**
- **HTML/CSS/JavaScript**

## Lancer le projet
Les dates/heures de début et de fin affichées par l'application peuvent être modifiées dans le fichier `config.json`. 

Assurez-vous d'avoir Java et Maven installés, puis dans un terminal depuis le dossier contenant le fichier `pom.xml` :
```bash
mvn clean install
mvn javafx:run
