package ReceiveMailPOP3;

public class Mail {
	public String from, to,cc,date, subject, content;
	public Mail(String from, String to,String cc,String date, String subject, String content) {
		this.cc = cc;
		this.from = from;
		this.to = to;
		this.date = date;
		this.subject = subject;
		this.content = content;
	}
}
