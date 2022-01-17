package testat2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class MsgServer {
	
	// Vorgegebener Port 
	private static final int PORT = 7777;
	private ServerSocket server = null;
	// Der folgende Pfad funktioniert auf Windows für gängige Sprachen. 
	// Auf Linux wird zusätzlich noch der Ordner "/Desktop/" erstellt, in dem dann der Ordner "/Messages/" ist.
	// Für andere Sprachen/Betriebssysteme muss der Desktoppfad umgestellt werden.
	private final String PATH = System.getProperty("user.home") + "/Desktop/Messages/";
	
	public MsgServer(int port) {
		
		try {
			// Neuen ServerSocket erstellen, der auf dem angegebenen Port läuft
			server = new ServerSocket(port);
			System.out.println("Nachrichtenserver gestartet! (Port "+port+")");
			// Wenn der Pfad für die Testdateien nicht existiert, soll dieser erstellt werden.
			File dir = new File(PATH);
			if(!dir.exists())
				dir.mkdirs();
		} catch (IOException e) {
			e.printStackTrace();
			// Wenn Server nicht gestartet werden kann, soll das Programm sich komplett beenden.
			System.exit(1);
		}
		
	}

	public static void main(String[] args) {
		
		// Da der Port vorgegeben ist, kann "args" unberührt bleiben
		MsgServer msgServer = new MsgServer(PORT);
		msgServer.run();
		
	}
	
	/**
	 * Startet den Server
	 * @throws IOException Wird geworfen, wenn nicht vom Input-Stream gelesen werden kann
	 */
	public void run() {
		String line, msg = null;
		while(true) {
			try(
					Socket s = server.accept();
					BufferedReader uIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter uOut = new PrintWriter(s.getOutputStream());
				) {
				// Befehl lesen
				line = uIn.readLine();
				// Falls Befehl gelesen wurde, Antwort erstellen und zurückgeben
				if(line != null) {
					msg = createAnswer(line);
					uOut.println(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Nimmt den Befehl, der vom User eingegeben wurde und erstellt die Antwort
	 * @param cmd Eingegebener Befehl
	 * @return Antwort, die dem Client zurückgegeben wird
	 * @throws IOException Wird geworfen, wenn es Fehler mit InputStreams oder OutputStreams gibt
	 * @throws FileNotFoundException Wird geworden, wenn es eine Datei nicht gefunden wird.
	 */
	private String createAnswer(String cmd) {
		
		// Falls aus irgendwelchen Umständen keine Antwort erstellt wird, wird einfach nur "FAILED" ausgegeben
		String answer = "FAILED";
		// Der Key ist eine zufällige UUID (universally unique identifier) und kann niemals gleich einer anderen generierten UUID sein.
		UUID uuid = UUID.randomUUID();
		// Der Befehl besteht aus zwei Teilen, dem Befehl selber und der damit übergebenen Nachricht/Schlüssel
		String[] args = cmd.split(" ", 2);
		PrintWriter writer = null;
		//  Der Befehl muss aus zwei Teilen bestehen, sonst fehlt etwas
		if(args.length != 2)
			answer = answer + " (Syntax-Error. Folgende Syntax ist erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)";
		else if(args[0].equalsIgnoreCase("SAVE")) {
			// Die Datei hat die UUID als Namen und .txt als Endung der Textdatei
			File f = new File(PATH, uuid.toString()+".txt");
			if(!f.exists())
				try {
					// Datei erstellen
					f.createNewFile();
				} catch (IOException e) {
					answer = answer + "\n (Die Datei konnte nicht erstellt werden)";
					e.printStackTrace();
					return answer;
				}
			try {
				// In die Datei schreiben
				writer = new PrintWriter(f);
				writer.println(args[1]);
				answer = "KEY "+uuid;
				writer.flush();
				// Schreiben beenden und schließen
				writer.close();
			} catch (IOException e) {
				answer = answer + "\n (Es gab einen Fehler mit dem Output-Stream. Bitte Starten Sie das Programm neu)";
				e.printStackTrace();
				return answer;
			}
		} else if(args[0].equalsIgnoreCase("GET")) {
			// Try-with-ressources mit dem BufferedReader
			try(BufferedReader reader = new BufferedReader(new FileReader(PATH+args[1]+".txt"))) {
				// Inhalt der Datei lesen
				String r = reader.readLine();
				if(r != null) 
					answer = "OK "+r;
			} catch (FileNotFoundException e) {
				answer = answer + "\n (Die Datei "+args[1]+" konnte nicht gefunden werden)";
				e.printStackTrace();
				return answer;
			} catch (IOException e) {
				answer = answer + "\n (Aus der Datei mit dem Key "+args[1]+" konnte nicht gelesen werden)";
				e.printStackTrace();
				return answer;
			}
		} else {
			// Wenn weder SAVE noch GET als Befehl mitgegeben worden sind
			answer = answer + " (Befehl nicht bekannt. Folgende Befehle sind erlaubt: SAVE <Nachricht> oder GET <Schlüssel>)";
		}
		
		return answer;
	}
	
}
