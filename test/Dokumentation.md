# Testat
	
Samed Demirel - Matrikelnummer 1307732 |
Mathis Neunzig - Matrikelnummer 2240587

Entwerfen Sie mit TCP einen Server, der Nachrichten speichert und zur Abfrage �ber das Netz bereit h�lt.
Zum Ablegen einer Nachricht auf dem Server sendet ein Client einen String mit dem folgenden Format an
den Server:

```
SAVE beliebig langer String mit abschlie�endem Zeilenende
```
	
Der Server generiert nach dem Empfang einen neuen geeigneten eindeutigen Schl�ssel (als String) und
speichert die Nachricht in einer Datei, wobei der Schlu�ssel als Dateiname verwendet wird. Danach sendet
der Server den Schl�ssel zuru�ck an den Client:

```
KEY schluessel
```
	
Alle Dateien sollen auf dem Server auf dem Desktop im Verzeichnis �Messages/� abgespeichert werden, das
Sie vorher schon anlegen sollten.