# Thema

Voor dit project heb ik gekozen om een klaslokaal-management systeem te maken. Hierbij zijn er 3 microservices waarbij een voor algemene info over de lokalen (ClassRoom), een voor de inhoud van een lokaal (Content) en een voor het reserveren van een lokaal (Reservation).

Daarnaast wil ik ook vermelden dat ik te laat was om een account aan te maken op Okteto, waardoor ik dus mijn containers lokaal moet draaien.

![Alt text](/readme_images/image.png)

# Gateway

In de gateway heb ik alle endpoints die ik heb gebruikt in mijn microservices gebruikt, daarnaast is het ook nog beveiligd met Google-cloud-Auth. Origineel was ik van plan om de GET-routes individueel te verklaren, tot ik er achter kwam dat ze allemaal bereikt mogen worden, daarom heb ik alle Get-routes bereikbaar gemaakt.

# Classroom-Service

De Classroom-Service werkt met het ClassRoom-model dat bestaat uit de variabelen: roomName en building. Dit wil zeggen dat er niet zo veel instaat maar het werkt beter in combinatie met de andere twee services. Het ClassRoom-model is voornamelijk bedoelt als algemene informatie over een klaslokaal: hoe het noemt en in welk les-gebouw het staat op de campus.

_GET-request /classrooms_

Dit endpoint haalt alle klaslokalen op met als format het originele ClassRoom-model als basis.

![Alt text](/readme_images/image-21.png)

_GET-request /classroom/{roomname}_

Dit endpoint haalt een klaslokaal op, op basis van de naam van het lokaal, hierbij wordt wel wat extra teruggegeven. Zo wordt er met behulp van de Reservation-Service gekeken of het lokaal op dit moment in gebruik is en met de Content-Service of het gebruikt zou kunnen worden voor een les waarbij de leerlingen een laptop nodig hebben en de docent een beamer.

![Alt text](/readme_images/image-1.png)

_POST-request /classroom (AUTH)_

Met dit endpoint kun je een classroom-object aanmaken.

![Alt text](/readme_images/image-2.png)

![Alt text](/readme_images/image-3.png)

_DELETE-request /classroom/{roomname} (AUTH)_

Met dit endpoint kun je een classroom-object verwijderen op basis van de naam van het lokaal.

![Alt text](/readme_images/image-4.png)

# Content-Service

Ik heb besloten om de inhoud van een lokaal te splitsen van het ClassRoom-model, voornamelijk door een gebrek aan inspiratie voor een thema. Daarnaast heb ik ook besloten om deze service te doen op basis van mongoDB. Dit is ook weer omdat ik de andere services zeker met Mysql wou doen. De Classroom-service was mijn main-container die requests moest sturen naar de andere containers. En de Reservation-Service maakt gebruik van LocalDate en LocalTime, dan heb ik uit voorzichtigheid toch maar Mysql daarvoor gekozen.

_GET-request /content/{roomname}_

Dit endpoint haalt de inhoud van een lokaal op, op basis van de naam van een lokaal.

![Alt text](/readme_images/image-5.png)

_POST-request /content (AUTH)_

Met dit endpoint kun je de inhoud van een lokaal aanmaken. Je kunt voor een lokaal niet meerdere Content-modellen aanmaken.

![Alt text](/readme_images/image-6.png)

![Alt text](/readme_images/image-7.png)

_PUT-request /content (AUTH)_

Met dit endpoint kun je de inhoud van een lokaal updaten. Dit is de enige service waar dit nodig is omdat een ClassRoom-object dit niet echt nodig heeft en een Reservation kan best opnieuw aangemaakt worden wanneer dit nodig is.

![Alt text](/readme_images/image-8.png)

![Alt text](/readme_images/image-9.png)

_DELETE-request /content/{roomname} (AUTH)_

Met dit endpoint kun je de content-object van een lokaal verwijderen op basis van de naam van het lokaal.

![Alt text](/readme_images/image-10.png)

# Reservation-Service

_GET-request: /reservations_

Dit endpoint haalt alle reservaties op.

![Alt text](/readme_images/image-11.png)

_GET-request: /reservations/{roomname}_

Dit endpoint haalt alle reservaties op voor een specifiek lokaal.

![Alt text](/readme_images/image-12.png)

_POST-request: /reservation (AUTH)_

Met dit endpoint kun je een reservation-object aanmaken. Je krijgt een fout-bericht terug als het overlapt met een gelijktijdige reservatie voor hetzelfde lokaal.

![Alt text](/readme_images/image-13.png)

![Alt text](/readme_images/image-14.png)

_DELETE-request: /reservation/{id} (AUTH)_

Met dit endpoint kun je een reservation-object verwijderen op basis van de id van deze reservatie. Ik maak hier gebruik van de id omdat dat de beste manier is om reservatie te onderscheiden van andere reservaties voor hetzelfde lokaal.

![Alt text](/readme_images/image-15.png)

![Alt text](/readme_images/image-16.png)

![Alt text](/readme_images/image-17.png)

_DELETE-request: /reservations/{roomname} (AUTH)_

Met dit endpoint kun je alle reservaties voor een bepaald lokaal verwijderen.

![Alt text](/readme_images/image-18.png)

![Alt text](/readme_images/image-19.png)

![Alt text](/readme_images/image-20.png)