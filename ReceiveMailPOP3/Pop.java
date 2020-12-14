package ReceiveMailPOP3;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class Pop extends javax.swing.JFrame{

	private javax.swing.JList dsmail;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea taNoidung;
	private javax.swing.JPasswordField tfPassword;
	private javax.swing.JTextField tfUsername;
	
	ArrayList<Mail> mailList; 
	String user, pass;
	int count;

	public Pop() {
		initComponents();
	}

	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		dsmail = new javax.swing.JList();
		jScrollPane2 = new javax.swing.JScrollPane();
		taNoidung = new javax.swing.JTextArea();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		tfUsername = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		tfPassword = new javax.swing.JPasswordField();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jLabel1.setText("RECIEVE MAIL - POP 3");
		jLabel2.setText("Danh sach mail");
		dsmail.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int i = dsmail.getSelectedIndex();
				if (i >= 0) {
					viewMail(i);
				}
			}
		});
		jScrollPane1.setViewportView(dsmail);

		taNoidung.setColumns(20);
		taNoidung.setRows(5);
		jScrollPane2.setViewportView(taNoidung);

		jLabel3.setText("Noi dung mail");
		jLabel4.setText("Username");
		jLabel5.setText("Password");

		tfPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				user = tfUsername.getText();
				pass = new String(tfPassword.getPassword());
				mailList = new ArrayList<Mail>();
				ReceiveMail_NonAPI rmn;
				try {
					rmn = new ReceiveMail_NonAPI();
					if(rmn.Authentication(user, pass)) {
						mailList = rmn.GetMail();
						count = mailList.size();
						if(count<1) {
							JOptionPane.showMessageDialog(null, "Không có mail mới nào cả!");
							taNoidung.setText("");
						}
						else {
							String[] s = new String[count];
							for (int i = 0; i < count; i++) {
								s[i] = (i + 1) + " - " + mailList.get(i).from + " - " + mailList.get(i).subject;
							}
							dsmail.setListData(s);
							taNoidung.setText("");
						}
						
					}else {
						JOptionPane.showMessageDialog(null, "Username and Password were not accepted");
					}
				}catch (Exception ex) {
					ex.getStackTrace();
		        	JOptionPane.showMessageDialog(null, "Can't connect to pop3.gmail");
				}
				
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel2)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel3)))
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel1)
								.addGroup(layout.createSequentialGroup()
										.addComponent(jLabel4)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(28, 28, 28)
										.addComponent(jLabel5)))
						.addGap(18, 18, 18)
						.addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGap(11, 11, 11)
						.addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel4)
								.addComponent(jLabel5)
								.addComponent(tfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
								.addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
						.addContainerGap())
				);

		pack();
	}

	void viewMail(int i) {
		Mail mail = mailList.get(i);
		taNoidung.setText("mail " + (i + 1));
		taNoidung.append("\n------------------------------------------------");
		taNoidung.append("\n-From: " + mail.from);
		if (!mail.to.equals("")) {
			taNoidung.append("\n-To: " + mail.to);
		}
		if(!mail.cc.equals("")) {
			taNoidung.append("\n-Cc: " + mail.cc);
		}
		if(!mail.bcc.equals("")) {
			taNoidung.append("\n-Bcc: "+user);
		}
		taNoidung.append("\n-Date: " + mail.date);
		taNoidung.append("\n-Subject: " + mail.subject);
		taNoidung.append("\n-Content:\n" + mail.content);
		if(!mail.file.equals("")) {
			taNoidung.append("\n-Attachment file:\n"+mail.file);
		}
		taNoidung.append("\n--------------------------------------------------");
	}


	public static void main(String args[]) {
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			new Pop().setVisible(true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
