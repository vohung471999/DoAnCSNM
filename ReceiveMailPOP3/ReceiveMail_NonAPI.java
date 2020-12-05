package ReceiveMailPOP3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import ReceiveMailPOP3.Mail;

public class ReceiveMail_NonAPI {
	private static SSLSocketFactory sslsocketfactory;
	private static SSLSocket clientSocket;
	private static BufferedReader in;
	private static PrintWriter out;
	public ReceiveMail_NonAPI() throws UnknownHostException, IOException{

		sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		clientSocket = (SSLSocket) sslsocketfactory.createSocket("pop.gmail.com", 995);
		clientSocket.startHandshake();
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		System.out.println(in.readLine());

	}
	public boolean Authentication(String user, String pass){
		try {
			out.println("USER "+user);
			System.out.println(in.readLine());
			out.println("PASS "+pass);
			if(in.readLine().contains("OK"))
				return true;
			else {
				in.close();
				out.close();
				clientSocket.close();
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public ArrayList<Mail> GetMail() {
		ArrayList<Mail> rs= new ArrayList<Mail>();
		try {
			out.println("STAT");
			String a = in.readLine();
			System.out.println(a);
			String[] num = a.split(" ");
			int count = Integer.parseInt(num[1]);
			System.out.println(count);
			for (int i = 1; i <= count; i++) {
				out.println("RETR "+i);
				String message = "";
				String content="";
				String from = "";
				String to = "";
				String cc="";
				String subject = "";
				String date ="";
				String file_name = "";
				String file_content ="";
				String mixed_boundary ="--";
				String alternative_boundary ="--";
				System.out.println(in.readLine());
				while (true) {
					String msg = in.readLine();
					message+=msg;
					if (msg.equals(".")) {
						break;
					}
					if (msg.equals("")) {
						message+="\r\n";
					}
					if (msg.startsWith("From:")) {
						from = msg.split("<")[1].split(">")[0]+", ";
						while(msg.contains(",")) {
							msg = in.readLine();
							from+=msg.split("<")[1].split(">")[0]+", ";
						}
						from = from.substring(0, from.length()-2);
					}
					if (msg.startsWith("To:")) {
						to+=msg.split("<")[1].split(">")[0]+", ";
						while(msg.contains(",")) {
							msg = in.readLine();
							message+=msg;
							to+=msg.split("<")[1].split(">")[0]+", ";
						}
						to = to.substring(0, to.length()-2);
					}
					if (msg.startsWith("Cc:")) {
						cc+=msg.split("<")[1].split(">")[0]+", ";
						while(msg.contains(",")) {
							msg = in.readLine();
							cc+=msg.split("<")[1].split(">")[0]+", ";
						}
						cc = cc.substring(0, cc.length()-2);
					}
					if (msg.startsWith("Subject:")) {
						subject = msg.substring(8);
					}
					if (msg.contains("Date:")&& !msg.contains("PST")) {
						date = msg.split("Date:")[1];
					}
					if (msg.contains("multipart/mixed")) {
						mixed_boundary +=msg.split("\"")[1];
					}
					if (msg.contains("multipart/alternative")) {
						alternative_boundary +=msg.split("\"")[1];
					}
					if (msg.contains("filename")) {
						file_name =msg.split("\"")[1];
					}
				}
				if (!mixed_boundary.equals("--")) {
					System.out.println(message);
					String[] mess_content = message.split(mixed_boundary);
					String[] mess_text = mess_content[1].split(alternative_boundary);
					String[] m = mess_text[1].split("\r\n");
					content = m[1]+"\n";
					String[] m2 = mess_content[2].split("\r\n");
					file_content = m2[1];
					File f = new File("E://File_Mail_Receive//" + file_name);
					FileOutputStream fos = new FileOutputStream(f);
					byte[] t = Base64.getMimeDecoder().decode(file_content);
					fos.write(t, 0, t.length);
					fos.close();
					content+="Attachment file:"+f.getPath()+"\n";
					Mail mail = new Mail(from, to,cc, date, subject, content);
					rs.add(mail);
				}else {
					System.out.println(message);
					String[] mess_text = message.split(alternative_boundary);
					String[] m = mess_text[1].split("\r\n");
					content = m[1]+"\n";
					Mail mail = new Mail(from, to,cc, date, subject, content);
					rs.add(mail);
				}
			}
			out.println("QUIT");
			System.out.println(in.readLine());
			clientSocket.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
