# Engineering Trust – Vertrauen bauen.


**GitHub/BitBucket Accounts**  

**An welchen Open-Source-Projekten hast Du/habt Ihr bisher gearbeitet?** *Max. 3 Projektbeispiele angeben (mit Link)*  

**Projekttitel** *Max. 70 Zeichen*  

**Beschreibe Dein Projekt kurz.** *Max. 700 Zeichen*  
Im heutigen Supermarkt wählen wir aus einer Fülle von Produkten aus. Dabei können wir nicht für jedes Produkt eine faktenbasierte Entscheidung treffen. Stattdessen fundiert unser Vertrauen in das Produkt zu einem Großteil auf dessen Marketing. Für unser Projekt entwickeln wir eine App die dem Kunden Licht in den Produktdschungel bringt. Mittels Bilderkennung kann der Benutzer mehrere Produkte, deren Inhaltsstoffe und Nährwerte direkt im Supermarktregal vergleichen. Interessante Metriken wie der Nutri-Score sowie eine Filterung nach Allergien, Intoleranzen oder Fleischanteil werden dem Nutzer in einer Augmented Reality Experience über das Produkt gelegt.

Dass zum Bewältigen der Klimakrise jeder Einzelne seinen Beitrag leisten muss ist in den letzten Jahren in der Bevölkerung angekommen.


Das Projekt hat zwei zentrale Aufgaben: Mittels der Kamera und Bilderkennung sollen bequem mehrere Produkte im Supermarktregal gleichzeitig erfasst und mittels einer Datenbank aus Produktbildern identifiziert werden. Daraufhin sollen dem Nutzer zusätzliche Informationen über die erkannten Produkte in einer Augmented Reality Ansicht zugänglich gemacht werden
* Infos und Vergleich Inhaltsstoffe
* Allergien & Unverträglichkeiten
* Produktvergleich (insb. durch Multi-Erkennung und weil ähnliche Produkte nebeneinander stehen)


**Welches gesellschaftliche Problem willst Du mit Deinem Projekt lösen?** *Max. 700 Zeichen*  
Der Gang zum Supermarkt ist eine Notwendigkeit beinahe jedes Menschen nicht nur in Industrienationen. Gleichzeitig hängt der Bericht des IPCCs wie ein Schatten über den Köpfen der Menschen und die Awareness über den Klimawandel ist auf einem nie dagewesenen Hoch.
Umso seltsamer ist es, dass selbst umweltbewusste Menschen sich bei der Produktwahl im Supermarkt lediglich auf Bio- und FairTrade-Siegel verlassen können.
Wir behaupten im Informationszeitalter zu leben und dennoch beschränken sich unsere genormten Produktinformationshinweise auf Inhaltsstoffe und eine Hand voll Siegeln.

**Wie willst Du Dein Projekt technisch umsetzen?** *Max. 1300 Zeichen*  

Die OpenSource Datenbank OpenFoodFacts bietet über 34.000 Supermarkt-Produkte alleine des deutschprachigen Raumes. Die Datensätze enthalten nebst mehreren Fotos der Verpackungen die digitalisierten Inhaltsstoffe und Nährwertangaben aber auch zusätzliche Informationen wie Mengenangaben, die Art der Verpackung, die Marke, eine Kategorisierung sowie eine Liste von Zertifizierungen.

Zentrales Element unseres Projekts wird eine mobile App mit Kamera sein, welche der Nutzer während des Einkaufvorganges im Supermarkt als Assistenten verwendet.
Mittels Googles Mobile Visions text recognition werden in Echtzeit die auf Produkten befindlichen Texte und deren relative Positionen erkannt.
Diese werden unseren Server übermittelt (vlt kriegen wir das ja auch noch lokal hin?).
Der Server hat eine Datenbank aller Produkttexte, welche einmalig aus den OpenFoodFacts-Fotos erkannt werden müssen.
Durch pattern matching und unter Berücksichtigung geometrischer Transformationen wird das Produkt erkannt.
Dem Nutzer wird in der App das Produkt in einer Augmented Reality Experience präsentiert.

Die Texterkennung ist wesentlich robuster bezüglich schlechten Bildverhältnissen und durch die Allgemeinheit des Problems sind vortrainierte neuronale Netze sehr zuverlässig. Hinzu kommt, dass ein image recognition Ansatz durch die Fülle an Produkten kaum bewältigbar wäre.


**Welche ähnlichen Lösungen gibt es schon, und was wird Dein Projekt anders bzw. besser machen?** *Max. 400 Zeichen*  
Die hauseigene App von OpenFoodFacts verwendet für die Erkennung des Produkts den Barcode. Das macht die Verwendung im Supermarkt umständlich: Das Produkt muss aus dem Regal genommen werden und beide Hände sind während des Scans belegt. Ein Vergleich von zwei oder gleich mehrerern Produkten ist kaum zumutbar. Erst eine gut funktionierende Bilderkennung der Produktverpackungen macht eine solche App salonfähig.


**Wer ist die Zielgruppe und wie soll Dein Tool sie erreichen?** *Max. 700 Zeichen*  


**Hast Du schon an der Idee gearbeitet? Wenn ja, beschreibe kurz den aktuellen Stand und erkläre die Neuerung.** *Max. 700 Zeichen*  


**Wie viele Stunden willst Du (bzw. will das Team) in den 6 Monaten Förderzeitraum an der Umsetzung arbeiten?** *Bitte eine Zahl eintragen.*  

**Skizziere kurz die wichtigsten Meilensteine, die Du (bzw. das Team) im Förderzeitraum umsetzen willst.** *Max. 700 Zeichen*  
