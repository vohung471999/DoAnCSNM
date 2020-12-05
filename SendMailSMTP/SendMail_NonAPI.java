package SendMailSMTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Date;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SendMail_NonAPI {
	private static SSLSocketFactory sslsocketfactory;
	private static SSLSocket clientSocket;
	private static BufferedReader in;
	private static PrintWriter out;
	public SendMail_NonAPI() throws UnknownHostException, IOException{
		
			sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			clientSocket = (SSLSocket) sslsocketfactory.createSocket("smtp.gmail.com", 465);
			clientSocket.startHandshake();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			System.out.println(in.readLine());
		
	}
	public boolean Authentication(String user, String pass){
		try {
			String user_encode = Base64.getMimeEncoder().encodeToString(user.getBytes());
			String pass_encode = Base64.getMimeEncoder().encodeToString(pass.getBytes());
			
			out.println("HELO gmail.com");
			System.out.println(in.readLine());
			out.println("AUTH LOGIN");
			System.out.println(in.readLine());
			out.println(user_encode);
			System.out.println(in.readLine());
			out.println(pass_encode);
	        if(!in.readLine().contains("535"))
	        	return true;
	        else {
	        	in.close();
	        	out.close();
	        	clientSocket.close();
	        	return false;
	        }
		} catch (Exception e) {
			e.printStackTrace();
        	return false;
		}
	}
	public void sendEmail(String from,String[] listTo, String[] listCC, String[] listBcc, String content, String filepath, String subject) {
		try {
			
			String data ="";
			if(filepath.equals("")) {
				data = SetDataWithoutAttachment(from, listTo, listCC, listBcc, content, subject);
			}else {
				data = SetDataWithAttachment(from, listTo, listCC, listBcc, content, filepath, subject);
			}
			out.println("MAIL FROM: <"+from+">");
			System.out.println(in.readLine());
			if(listTo!=null) {
				for(int i = 0;i<listTo.length;i++) {
					out.println("RCPT TO:<"+listTo[i]+">");
					System.out.println(in.readLine());
				}
			}
			if(listCC!=null) {
				for(int i = 0;i<listCC.length;i++) {
					out.println("RCPT TO:<"+listCC[i]+">");
					System.out.println(in.readLine());
				}
			}
			if(listBcc!=null) {
				for(int i = 0;i<listBcc.length;i++) {
					out.println("RCPT TO:<"+listBcc[i]+">");
					System.out.println(in.readLine());
				}
			}
			out.println("DATA");
			System.out.println(in.readLine());
			out.println(data);
			System.out.println(in.readLine());
			out.println("QUIT");
			System.out.println(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String SetDataWithAttachment(String from,String[] listTo, String[] listCC, String[] listBcc, String content, String filepath, String subject) throws IOException {
		String m = 
				Header(from, listTo, listCC, listBcc, subject) +
				"Content-Type: multipart/mixed; boundary=\"MixedBoundaryString\"\r\n\r\n" + 
				"--MixedBoundaryString\r\n" + 
				Content_Part(content)+
				"--MixedBoundaryString\r\n" + 
				File_Part(filepath)+
				"--MixedBoundaryString--\r\n"+
				".\r\n";
		return m;
    }
	public static String SetDataWithoutAttachment(String from,String[] listTo, String[] listCC, String[] listBcc, String content, String subject){
		String m = 
				Header(from, listTo, listCC, listBcc, subject)+
				Content_Part(content)+
				".\r\n";
		return m;
	}
	public static String Header(String from,String[] listTo, String[] listCC, String[] listBcc,String subject) {
		Date d = new Date();
		String list_to = "To: ";
		for(int i = 0; i<listTo.length;i++) {
			list_to+= "<"+listTo[i]+">,\n\t";
		}
		list_to=list_to.substring(0, list_to.length()-4);
		String header =
				"MIME-Version: 1.0\r\n" +
				"From: <" + from + ">\r\n"+
				"Date: "+ d +"\r\n"+ 
				"Subject: " + subject + "\r\n" +
				list_to+"\r\n";
		return header;
	}
	public static String Content_Part(String content) {
		String content_part =
				"Content-Type: multipart/alternative; boundary=\"AlternativeBoundaryString\"\r\n\r\n" + 
				"--AlternativeBoundaryString\r\n" + 
				"Content-Type: text/plain;charset=\"utf-8\"\r\n" + 
				"Content-Transfer-Encoding: quoted-printable\r\n\r\n" + 
				content+"\r\n\r\n" + 
				"--AlternativeBoundaryString\r\n" + 
				"Content-Type: text/html;charset=\"utf-8\"\r\n" + 
				"Content-Transfer-Encoding: quoted-printable\r\n\r\n" + 
				"<div dir=\"ltr\">"+content+"</div>\r\n\r\n" +
				"--AlternativeBoundaryString--\r\n";
		return content_part;
	}
	public static String File_Part(String filepath) throws IOException {
		File file = new File(filepath);
		byte[] b = new byte[(int)file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(b);
		fis.close();
		DataSource source = new FileDataSource(file);
		String file_base64 = Base64.getMimeEncoder().encodeToString(b);
		String file_part =
				"Content-Type: "+source.getContentType()+";name=\""+file.getName()+"\"\r\n" + 
				"Content-Transfer-Encoding: base64\r\n" + 
				"Content-Disposition: attachment;filename=\""+file.getName()+"\"\r\n\r\n" + 
				file_base64+"\r\n";
		return file_part;
	}
}
