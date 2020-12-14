package ReceiveMailPOP3;

public class Mail {
	String from, to, cc, bcc, date, subject, content, file;
	public Mail(String from, String to, String cc, String bcc,String date, String subject, String content, String file) {
		this.cc = cc;
		this.from = from;
		this.to = to;
		this.bcc = bcc;
		this.date = date;
		this.subject = subject;
		this.content = content;
		this.file = file;
	}
}
