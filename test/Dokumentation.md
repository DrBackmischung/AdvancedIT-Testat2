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

Zeile 2 und 4 sind Eingaben vom Client

Zeile 3 und 5 sind Antworten vom Server

``` java
Verbunden! 
SAVE Ich bin ein Test
KEY 7c184ab7-a8e4-41c6-a213-d3bc14295f31
GET 7c184ab7-a8e4-41c6-a213-d3bc14295f31
OK Ich bin ein Test
.
```

### Ausgabe Server
Zeile 1 wird beim Starten des Servers ausgegeben

``` java
Nachrichtenserver gestartet! (Port 7777)
```
### Auswertung

Die korrekte Ausführung funktioniert, die Nachricht beim SAVE wird als "7c184ab7-a8e4-41c6-a213-d3bc14295f31.txt" im "Messages" Ordner abgespeichert. Beim Lesen der Datei mit GET wird die Datei gefunden und aus ihr gelesen. Der Client wird beendet, wenn "." eingegeben wird. Die Befehle SAVE und GET werden korrekt vom Server interpretiert und abgearbeitet.
