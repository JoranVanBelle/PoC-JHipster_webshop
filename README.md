# Proof-of-Concept SOA webshop

Deze repository bevat alle code die is gebruikt tijdens het onderzoek naar het verschil tussen service-oriented architecture en [event-driven architecture](https://github.com/JoranVanBelle/EDA_Webshop/blob/master/README.md). In een verder stadium is er nood gebleken aan een zelf geprogrammeerde [service-oriented architecture](https://github.com/JoranVanBelle/SOA_webshop) die ook gevonden kan worden op Github.

## Inhoud

Deze repository bevat volgende bestanden:
  1. De source code om de architectuur op te bouwen
  2. Een docker-compose file en Dockerfile om de architectuur in docker te runnen
  3. De scenario's die met behulp van Gatling uitgevoerd kunnen worden

## Starten

Om de architectuur te kunnen runnen in docker dienen volgende stappen uitgevoerd te worden in een terminal die zich bevindt in de root van het project:
  1. Het maven-project builden
    Aangezien dit project is opgebouwd uit meerdere services, moeten deze commando's in alle services (webshop, transport, pricing, order, inventory) uitgevoerd worden
    1.1 Windows: ``./mvnw package -Pprod verify jib:dockerBuild``
    1.2 Linux/MacOS: ``mvn package -Pprod verify jib:dockerBuild``
  
  2. De containers aanmaken en runnen
    Hiervoor moet er naar de map docker-compose gegaan worden
    2.1 ``docker-compose up``

## Errors

Bij het in productie draaien van dit project zijn er enkele errors mogelijk

  1. I/O error for image:
    Deze error kan tevoorschijn komen bij het runnen van commando 1 in bovenstaand hoofdstuk.
    Deze error kan verholpen worden door het commando opnieuw te runnen.
  
  2. Java 19:
    JHipster ondersteunt op dit moment enkel java 11 tot en met java 18. Om deze error op te lossen moet de systeemvariabele ``JAVA_HOME`` geüpdated worden naar ``path/to/jdk-18.0.2.1``. 
    Meer details zijn te vinden op internet.

## Opmerkingen

Bij het runnen van de gatling scenario's is het belangrijk op te merken dat er een Bearer-token nodig is. Deze kan gevonden worden als volgt:
  1. Druk op ``F12``
  2. Ga naar ``Network``
  3. Login
  4. De Bearer-token kan gevonden worden in de response-header

Ook hier moet de databank gepopulate worden. Dit kan gedaan worden via de eenvoudige UI die mee is gegenereerd door JHipster.