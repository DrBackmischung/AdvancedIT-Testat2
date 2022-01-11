package testat2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgClient {
	
	public static final int PORT = 7777;
	
	public static void main(String[] args) {
		
		String host = "localhost";
		Socket s = null;
		BufferedReader nIn = null;
		PrintWriter nOut = null;
		
		try {
			
			BufferedReader uIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Connected!");
			
			while(true) {
				String line = uIn.readLine();
				if(line.equals(".")) break;
				s = new Socket(host, PORT);
				nIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
				nOut = new PrintWriter(s.getOutputStream());
				nOut.println(line);
				nOut.flush();
				System.out.println(nIn.readLine());
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
