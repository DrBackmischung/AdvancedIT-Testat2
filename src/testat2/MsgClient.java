package testat2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgClient {
	
	public static final int PORT = 7777;
	
	/**
	 * Main-Methode des Clients
	 * @param args Argumente, mit dem das Programm gestartet wird
	 * @throws UnknownHostException Wird geworfen, wenn aus dem host keine IP Adresse gemacht werden kann
	 * @throws IOException Wird geworden, wenn es Probleme mit InputStreams und OutputStreams gibt
	 */
	public static void main(String[] args) {
		
		// Der Host ist lokal und mit "localhost" vordefiniert
		String host = "localhost";
		Socket s = null;
		BufferedReader nIn = null;
		PrintWriter nOut = null;
		
		try {
			
			// Tastatureingabe über Konsole
			BufferedReader uIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Verbunden!");
			
			while(true) {
				String line = uIn.readLine();
				// Aufhören, wenn 
				if(line.equals(".")) 
					break;
				// Neuen Socket mit host erstellen
				s = new Socket(host, PORT);
				// Input- und OutputStreams verbinden und speichern
				nIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
				nOut = new PrintWriter(s.getOutputStream());
				// Nachricht absenden
				nOut.println(line);
				nOut.flush();
				// Auf Antwort warten und ausgeben
				System.out.println(nIn.readLine());
				// Input- und OutputStreams schließen
				if(nIn != null)
					nIn.close();
				if(nOut != null)
					nOut.close();
				if(s != null) 
					s.close();
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Bei Fehlern oder bei "." als Befehl müssen hier noch alle Reader/Writer geschlossen werden
			if(nIn != null)
				try {
					nIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(nOut != null)
				nOut.close();
			if(s != null)
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}

}
