# Engineering Trust – Vertrauen bauen.


**GitHub/BitBucket Accounts**

matzebond @ Github - maschm @ GitLab
AnJ95 @ Github - Simplicitis @ GitLab
Flunzmas @ Github - boltres @ GitLab

**An welchen Open-Source-Projekten hast Du/habt Ihr bisher gearbeitet?** *Max. 3 Projektbeispiele angeben (mit Link)*

https://github.com/rekit-group/rekit-game - 2D-Spieleengine für Bildungszwecke im Bereich Software Design

https://github.com/schulzchristian/multilevelSVM - wisschenschafliches Forschungsprojekt im Bereich Support Vector Machines

https://globalgamejam.org/users/maschm - gemeinsam programmierte frei verfügbare Spiele im Rahmen des GGJ


**Projekttitel** *Max. 70 Zeichen*

FoodLens

**Beschreibe Dein Projekt kurz.** *Max. 700 Zeichen*

Im heutigen Supermarkt wählen wir aus einer Fülle von Produkten aus. Dabei können wir nicht für jedes Produkt eine faktenbasierte Kaufentscheidung treffen. Stattdessen fundiert unser Vertrauen in das Produkt zu einem Großteil auf dessen Marketing. Für unser Projekt entwickeln wir eine App die dem Kunden Licht in den Produktdschungel bringt. Mittels Bilderkennung kann der Benutzer bequem mehrere Produkte, deren Inhaltsstoffe und Nährwerte direkt im Supermarktregal vergleichen. Interessante Metriken wie der Nutri-Score sowie eine Filterung nach Allergien, Intoleranzen oder Fleischanteil werden dem Nutzer in einer Augmented Reality (AR) Experience über das Produkt gelegt.


**Welches gesellschaftliche Problem willst Du mit Deinem Projekt lösen?** *Max. 700 Zeichen*

Der Bericht des IPCCs hängt wie ein Schatten über den Köpfen der Menschen und das Bewusstsein für den Klimawandel ist auf einem nie dagewesenen Hoch. Der Deutsche geht 209 mal pro Jahr in den Supermarkt und steht dabei vor klima-, gesundheits- sowie sozioökologisch relevanten Entscheidungen. Die Marketingindustrie hat viele Wege gefunden dem Nutzer diese Entscheidung abzunehmen und auf emotionale Buzzwords und Äußerlichkeiten zu reduzieren. Wir behaupten im Informationszeitalter zu leben, doch hat selbst der investierte Nutzer weder die nötigen Informationen noch die nötige Zeit, seinem Gewissen gerecht zu entscheiden. Unsere App gibt dem Nutzer die Informationen die er braucht und verdient.


**Wie willst Du Dein Projekt technisch umsetzen?** *Max. 1300 Zeichen*  

Zentrales Element unseres Projekts ist eine Mobile-App, die der Nutzer während des Einkaufvorgangs im Supermarkt als Assistenten verwendet und die die Kamera des Geräts zur Erfassung von Produkten nutzt. Erkannte Produkte werden in einer AR Experience präsentiert, was eine einheitliche Darstellung der Produkdaten vereinfacht sowie Vergleiche zwischen Produkten ohne Ablenkung durch deren Design ermöglicht.
Bezüglich der Bilderkennung möchten wir unseren wissenschaftlichen Hintergrund nutzen, um eine für den Anwendungsfall spezialisierte Produkt- und Texterkennung zu entwickeln, die auf dem neuesten Stand der Technik arbeitet. Hierbei werden in Echtzeit die Texte der Verpackungen - unter anderem Produkt- und Markenname - erkannt.
Technisch kann hier mit bewährten Lösungen wie z.B. Googles Mobile Vision oder einem eigens entworfenen maschinellem Lernalgorithmus mit Tensorflow Lite gearbeitet werden.
Die Produktinformationen beziehen wir aus der quelloffenen Datenbank OpenFoodFacts; sie bietet über 34.000 Supermarkt-Produkte alleine im deutschprachigen Raum. Der Datensatz enthält pro Produkt die Inhaltsstoffe und Nährwertangaben sowie eine Fülle an zusätzlichen Informationen und mehrere Fotos der Verpackung.

**Welche ähnlichen Lösungen gibt es schon, und was wird Dein Projekt anders bzw. besser machen?** *Max. 400 Zeichen*

Die OpenFoodFacts App und proprietäre Anbieter wie codecheck oder barcoo verwenden für die Erkennung der Produkte den Strichcode. Unsere App bietet im Gegensatz zur Erkennung des nicht immer eindeutigen Strichcode zusätzlich eine robuste Bilderkennung. Außerdem werden die Informationen mittels AR direkt in das Kamerabild integriert und erleichtern somit den Vergleich von Produkten im Supermarktregal.


**Wer ist die Zielgruppe und wie soll Dein Tool sie erreichen?** *Max. 700 Zeichen*

Heutzutage führt praktisch jeder Supermarktkunde sein Handy mit und käme damit als potentieller Nutzer unserer App infrage. Es ist jedoch sinnvoll, die Zielgruppe auf diejenigen Kunden zu beschränken, die ihre Kaufentscheidungen faktengestützt tätigen möchten und hierfür bereit sind das Handy zu nutzen. Diesen Kunden möchten wir mit unserer App eine intuitiv benutzbare und effektive Entscheidungshilfe geben. Die Bekanntheit unserer App werden wir schrittweise aufbauen, indem wir uns u.U. zunächst geografisch beschränken oder aber gewisse Nutzer-Communities gezielt ansprechen und Bekanntheit bei denen erlangen, die unsere Vision im Folgenden zur breiten Masse hinaus tragen werden.

**Hast Du schon an der Idee gearbeitet? Wenn ja, beschreibe kurz den aktuellen Stand und erkläre die Neuerung.** *Max. 700 Zeichen*

Der Anmeldung ging eine Recherche über schon existierende Lösungen sowie mögliche Kollaborationen voraus. Hierbei haben wir mit codecheck Kontakt aufgenommen und ein "Angebot" bekommen, deren Datenbank zu mieten (1000 Anfragen pro Tag für 300€ im Monat). Danach wurden wir auf die unter der Open Database License verfügbaren Daten von OpenFoodFacts aufmerksam.
Als Proof-of-Concept für unseren Ansatz der textbasierten Bilderkennung haben wir bereits einen Prototypen entwickelt. Es handelt sich um eine Android-App welche 100 Produkte in unter einer Sekunde mit Bilderkennung erkennen und in einer simplen AR-Ansicht anzeigen kann. Hierfür haben wir bereits die OpenFoodFacts Datenbank verwendet.

**Wie viele Stunden willst Du (bzw. will das Team) in den 6 Monaten Förderzeitraum an der Umsetzung arbeiten?** *Bitte eine Zahl eintragen.*
1080

**Skizziere kurz die wichtigsten Meilensteine, die Du (bzw. das Team) im Förderzeitraum umsetzen willst.** *Max. 700 Zeichen*

1. App, die Produktname und weitere Texte extrahiert und in einer AR-Ansicht anzeigt.
2. Anbindung an die OpenFoodFacts-Datenbank und ihre Suche -> Funktionierende Produkterkennung mit primitiver Info-Anzeige.
3. Integration zentraler Informationsanzeigen und anderer App-Funktionen -> Erster voll funktionsfähiger Prototyp.
4. Veröffentlichung der App in den Apple Stores.
5. Erste 1000 Nutzer durch gezielte Präsentation in Online-Communities und Freundeskreisen.

-------------------------------- EXTRA -----------------------------------

Dass zum Bewältigen der Klimakrise jeder Einzelne seinen Beitrag leisten muss ist in den letzten Jahren in der Bevölkerung angekommen.

Das macht die Verwendung im Supermarkt umständlich: Das Produkt muss aus dem Regal genommen werden und beide Hände sind während des Scans belegt. Ein Vergleich von zwei oder gleich mehrerern Produkten ist kaum zumutbar. Erst eine gut funktionierende Bilderkennung der Produktverpackungen macht eine solche App salonfähig. Des Weiteren ist der Barcode eines Produkts nicht immer eindeutig und unterliegt Änderungen.

Das Projekt hat zwei zentrale Aufgaben: Mittels der Kamera und Bilderkennung sollen bequem mehrere Produkte im Supermarktregal gleichzeitig erfasst und mittels einer Datenbank aus Produktbildern identifiziert werden. Daraufhin sollen dem Nutzer zusätzliche Informationen über die erkannten Produkte in einer Augmented Reality Ansicht zugänglich gemacht werden
* Infos und Vergleich Inhaltsstoffe
* Allergien & Unverträglichkeiten
* Produktvergleich (insb. durch Multi-Erkennung und weil ähnliche Produkte nebeneinander stehen)
