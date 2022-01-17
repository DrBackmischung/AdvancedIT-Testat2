# Testat
bearbeitet von 	
Samed Demirel (Matrikelnummer 1307732) und Mathis Neunzig (Matrikelnummer 2240587)

## Aufgabe

Entwerfen Sie mit TCP einen Server, der Nachrichten speichert und zur Abfrage über das Netz bereit hält.
Zum Ablegen einer Nachricht auf dem Server sendet ein Client einen String mit dem folgenden Format an
den Server:

```
SAVE beliebig langer String mit abschließendem Zeilenende
```
	
Der Server generiert nach dem Empfang einen neuen geeigneten eindeutigen Schlüssel (als String) und
speichert die Nachricht in einer Datei, wobei der Schlüssel als Dateiname verwendet wird. Danach sendet
der Server den Schlüssel zurück an den Client:

```
KEY schluessel
```
	
Alle Dateien sollen auf dem Server auf dem Desktop im Verzeichnis ”Messages/” abgespeichert werden, das
Sie vorher schon anlegen sollten.

Zum Abrufen einer Nachricht sendet ein Client einen String:

```
GET schluessel
```

GET schluessel
an den Server, der daraufhin u¨berpru¨ft, ob eine entsprechende Datei existiert.
Falls ja, sendet er den Inhalt der Datei an den Client:

```
OK dateiinhalt
```

Anderenfalls sendet er:

```
FAILED
```

Implementieren Sie den Server auf Port 7777 sowie einen Client zum Testen.

## Beispiel 1 - Korrekte Ausführung

Für die Korrekte Ausführung wird einmal SAVE mit einer Nachricht ausgeführt. Der dann übergebene Key wird mit GET angeschickt, um die Nachricht ausgegeben zu bekommen. Die Nachricht wird im Ordner "Messages" auf dem Desktop abgespeichert. Mit "." wird die Verbindung vom Client unterbrochen.

### Ausgabe Client
Zeile 1 wird dem User beim Start des Clients angezeigt

Zeile 2, 4 und 6 sind Eingaben vom Client

Zeile 3 und 5 sind Antworten vom Server

``` java
Verbunden! 
SAVE Ich bin ein Test
KEY 7c184ab7-a8e4-41c6-a213-d3bc14295f31
GET 7c184ab7-a8e4-41c6-a213-d3bc14295f31
OK Ich bin ein Test
.
```
![image](https://user-images.githubusercontent.com/20612995/149787421-509e3c4e-e2fc-46cc-9baa-b85815fa7499.png)

### Ausgabe Server
Zeile 1 wird beim Starten des Servers ausgegeben

``` java
Nachrichtenserver gestartet! (Port 7777)
```
### Auswertung

Die korrekte Ausführung funktioniert, die Nachricht beim SAVE wird als "7c184ab7-a8e4-41c6-a213-d3bc14295f31.txt" im "Messages" Ordner abgespeichert. Beim Lesen der Datei mit GET wird die Datei gefunden und aus ihr gelesen. Der Client wird beendet, wenn "." eingegeben wird. Die Befehle SAVE und GET werden korrekt vom Server interpretiert und abgearbeitet.

## Beispiel 2 - Syntax-Fehler

Erlaubte Eingaben sind die Befehle "SAVE <Nachricht>", "GET <Nachricht>" und "." zum Beenden. Wenn der Befehl falsch benutzt wird und es wird keine Nachricht mitgegeben, dann soll "FAILED" zurückgegeben werden.

### Ausgabe Client
Zeile 1 wird dem User beim Start des Clients angezeigt

Zeile 2, 4, 6 und 8 sind Eingaben vom Client

Zeile 3, 5 und 7 sind Antworten vom Server

``` java
Verbunden!
SAVE
FAILED (Syntax-Error. Folgende Syntax ist erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)
GET
FAILED (Syntax-Error. Folgende Syntax ist erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)

FAILED (Syntax-Error. Folgende Syntax ist erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)
.

```

### Ausgabe Server
Zeile 1 wird beim Starten des Servers ausgegeben

``` java
Nachrichtenserver gestartet! (Port 7777)
```
### Auswertung

Wenn die Nachricht nicht aus zwei Teilen besteht - dem Schlüsselwort und der Nachricht, gibt es einen Fehler und FAILED wird zurückgegeben.

## Beispiel 3 - Falsche Befehle

Erlaubte Eingaben sind die Befehle "SAVE <Nachricht>", "GET <Nachricht>" und "." zum Beenden. Wenn ein falscher Befehl eingegeben wird, soll "FAILED" zurückgegeben werden.

### Ausgabe Client
Zeile 1 wird dem User beim Start des Clients angezeigt

Zeile 2, 4 und 6 sind Eingaben vom Client

Zeile 3 und 5 sind Antworten vom Server

``` java
Verbunden!
SPEICHERN Test
FAILED (Befehl nicht bekannt. Folgende Befehle sind erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)
OK Test
FAILED (Befehl nicht bekannt. Folgende Befehle sind erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)
.

```

### Ausgabe Server
Zeile 1 wird beim Starten des Servers ausgegeben

``` java
Nachrichtenserver gestartet! (Port 7777)
```
### Auswertung

Wenn der Befehl nicht existiert und nicht gültig ist, wird "FAILED" an den Client zurückgegeben.

## Beispiel 4 - Kein Zugriff auf Dateisystem

Zum Schreiben von Dateien braucht der Server Rechte, um auf das Verzeichnis, in dem die Dateien gespeichert werden sollen. Zum Testen wurde der Pfad im Code auf "C:/" geändert, da dieser auf Windows schreibgeschützt ist. Sollte der im Code angegebene Pfad zum Desktop schreibgeschützt sein, wird der selbe Fehler geworfen. Der Client soll "FAILED" erhalten.

### Ausgabe Client
Zeile 1 wird dem User beim Start des Clients angezeigt

Zeile 2 und 4 sind Eingaben vom Client

Zeile 3 ist die Antwort vom Server

``` java
Verbunden!
SAVE Ich bin ein Test
FAILED (Die Datei konnte nicht erstellt werden)
.
```

### Ausgabe Server
Zeile 1 wird beim Starten des Servers ausgegeben

Die restlichen Zeilen sind der Stack Trace der geworfenen IOException

``` java
Nachrichtenserver gestartet! (Port 7777)
java.io.IOException: Zugriff verweigert
	at java.io.WinNTFileSystem.createFileExclusively(Native Method)
	at java.io.File.createNewFile(Unknown Source)
	at testat2.MsgServer.createAnswer(MsgServer.java:101)
	at testat2.MsgServer.run(MsgServer.java:67)
	at testat2.MsgServer.main(MsgServer.java:47)
```
### Auswertung

Wenn das Dateiverzeichnis schreibgeschützt ist, dann kann die Datei nicht erstellt werden und es wird "FAILED" zurückgegeben.

## Beispiel 5 - Ungültiger Schlüssel

Wenn ein Schlüssel mit dem GET-Befehl benutzt wird, der nicht existiert, soll eine FileNotFoundException geworfen werden und der Client bekommt "FAILED" zurück.

### Ausgabe Client
Zeile 1 wird dem User beim Start des Clients angezeigt

Zeile 2, 4 und 6 sind Eingaben vom Client

Zeile 3 und 5 sind Antworten vom Server

``` java
Verbunden!
SAVE Ich bin ein Test
KEY 73a78b8e-2fb2-48b7-a0d6-38c49f23e6b2
GET falscherKey123
FAILED (Die Datei falscherKey123 konnte nicht gefunden werden)
.
```

### Ausgabe Server
Zeile 1 wird beim Starten des Servers ausgegeben

Die restlichen Zeilen sind der Stack Trace der geworfenen FileNotFoundException

``` java
Nachrichtenserver gestartet! (Port 7777)
java.io.FileNotFoundException: C:\Users\Mathis Neunzig\Desktop\Messages\falscherKey123.txt (Das System kann die angegebene Datei nicht finden)
	at java.io.FileInputStream.open0(Native Method)
	at java.io.FileInputStream.open(Unknown Source)
	at java.io.FileInputStream.<init>(Unknown Source)
	at java.io.FileInputStream.<init>(Unknown Source)
	at java.io.FileReader.<init>(Unknown Source)
	at testat2.MsgServer.createAnswer(MsgServer.java:121)
	at testat2.MsgServer.run(MsgServer.java:66)
	at testat2.MsgServer.main(MsgServer.java:46)

```
### Auswertung

Wenn die Datei mit dem gegebenen Key nicht gefunden werden kann und nicht existiert, dann kann wird dem Client "FAILED" übergeben.
