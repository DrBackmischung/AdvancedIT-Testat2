package testat2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class MsgServer {
	
	private static final int PORT = 7777;
	private ServerSocket server = null;
	// Der folgende Pfad funktioniert auf Windows (deutsch/englisch/französisch)
	// Für andere Sprachen/Betriebssysteme muss der Desktoppfad umgestellt werden.
	private final String PATH = System.getProperty("user.home") + "/Desktop/Messages/";
	
	public MsgServer(int port) {
		
		try {
			server = new ServerSocket(port);
			System.out.println("Nachrichtenserver gestartet! (Port "+port+")");
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
		
		MsgServer msgServer = new MsgServer(PORT);
		msgServer.run();
		
	}
	
	public void run() {
		String line, msg = null;
		while(true) {
			try(
					Socket s = server.accept();
					BufferedReader uIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter uOut = new PrintWriter(s.getOutputStream());
				) {
				line = uIn.readLine();
				if(line != null) {
					msg = createAnswer(line);
					uOut.println(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String createAnswer(String cmd) {
		
		String answer = "FAILED";
		UUID uuid = UUID.randomUUID();
		String[] args = cmd.split(" ", 2);
		PrintWriter writer = null;
		if(args.length != 2)
			answer = answer + " Syntax-Error. Folgende Syntax ist erlaubt: SAVE <Nachricht> oder GET <Schlüssel>";
		if(args[0].equalsIgnoreCase("SAVE")) {
			File f = new File(PATH, uuid.toString()+".txt");
			if(!f.exists())
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			try {
				writer = new PrintWriter(f);
				writer.println(args[1]);
				answer = "KEY "+uuid;
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(args[0].equalsIgnoreCase("GET")) {
			try(BufferedReader reader = new BufferedReader(new FileReader(PATH+args[1]+".txt"))) {
				String r = reader.readLine();
				if(r != null) 
					answer = "OK "+r;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			answer = answer + " Befehl nicht bekannt. Folgende Befehle sind erlaubt: SAVE <Nachricht> oder GET <Schlüssel>";
		}
		
		return answer;
	}
	
}
