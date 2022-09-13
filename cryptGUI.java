import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class cryptGUI extends JFrame implements ActionListener{

	// GUI variables
	JPanel panelMain, panel1, panel2;
	JButton register, login, exit;
	static JTextField username1, username2, key1, key2, message;
	static Object regist[] = {"Username", username1 = new JTextField(), "Key P", key1 = new JTextField(), "Key Q", key2 = new JTextField()};
	static Object log[] = {"Username", username2 = new JTextField()};
	JButton sendM, inbox, logout;
	Container pane = getContentPane();
	static String display="";
	
	// APP variables
	static String names[] = new String[10];
	static int countUser=0;
	//int p,q;

	static ArrayList<cryptUser> users = new ArrayList<cryptUser>();

	public void actionPerformed(ActionEvent e) {
	}

	public static void main(String[] args) {
		cryptGUI frame = new cryptGUI(); 	
		frame.pane.setLayout(new BoxLayout(frame.pane, BoxLayout.Y_AXIS));

		// MAIN PANE
		frame.panelMain = new JPanel();
		//frame.panelMain.setLayout(new BoxLayout(frame.panelMain, BoxLayout.Y_AXIS));
		frame.panelMain.setLayout(new GridLayout(2,1));
		
		// PANEL 1
		frame.panel1 = new JPanel();
		frame.panel1.setLayout(new GridLayout(1,1));
		
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		
		display += ("\n  Welcome to Secret Message App");
		display += ("\n  How to play: \n   1.Register your account \n   2. Put a message in the Message Box \n   3.Read the messages in the box \n   Note: You can only read messages inteded to you, if it's not for you the message is gibberish.");
		display += ("\n\n  Have Fun!\n");
		textArea.append(display);
		frame.panel1.add(textArea);

		
		// PANEL 2
		// 1. Register button
		frame.panel2 = new JPanel();
		frame.panel2.setLayout(new GridLayout(3,1));
		frame.panel2.add(frame.register = new JButton("Register"));
		frame.register.addActionListener(e -> {

			int option = JOptionPane.showConfirmDialog(null, regist, "Register Account",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (option == JOptionPane.OK_OPTION) { //if click OK
				int p = Integer.parseInt(key1.getText());
				int q = Integer.parseInt(key2.getText());
				String name = username1.getText();
				names[countUser] = name;
				System.out.println(names[countUser]);
				countUser++;
				users.add(new cryptUser(p,q,name));
				username1.setText("");
				key1.setText("");
				key2.setText("");
			}
		});

		// 2. Login button
		frame.panel2.add(frame.login = new JButton("Login"));
		frame.login.addActionListener(e -> {
				
				//
				JPanel logIn = new JPanel(new GridLayout(2,1));

				JComboBox<String> comboBox = new JComboBox<>(names);

				logIn.add(comboBox);

				int n = JOptionPane.showConfirmDialog(null,
						logIn,
						"Choose User",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
						);

				String nameUser = (String)comboBox.getSelectedItem();                          // name of the User login
				// clear combobox selection here
				
				for (cryptUser temp: users) {  
					if (temp.name.equals(nameUser)){		

						String accTitle = "Account ("+nameUser+") ";
						Object pilih[] = {"Send Message", "Message Box", "Logout"};
						int option2 = JOptionPane.showOptionDialog(null, 
								"Choose activity",
								accTitle,
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE,
								null,
								pilih, pilih[1]);


						// 1. Send Message
						if (option2 == JOptionPane.YES_OPTION) {

							JPanel fields = new JPanel(new GridLayout(2,1));
							JTextField field = new JTextField(10);

							fields.add(comboBox);
							fields.add(field);

							int m = JOptionPane.showConfirmDialog(null,
									fields,
									accTitle+"- Send Message",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
									);

							if (field.getText() != null) {
								String chosenUser = (String)comboBox.getSelectedItem();     // name of message receiver (chosenuser)
								String messageS = field.getText();                          // message for the receiver

								int chosenE=0;
								int chosenN=0;
								for (cryptUser tmp : users)                            // Taking keys of chosen user, send to everyone.
									if (tmp.name.equals(chosenUser)){
										chosenE = tmp.e;
										chosenN = tmp.n;        
									}
								
								for (cryptUser tmp : users)                              // Sending message to all users but encrypted
									tmp.sendMessage(messageS, chosenE, chosenN);	     // using chosenuser's keys
								                                                         // Meaning when decrypt, only choseusers can read.
							}
						}

						// 2. Check Inbox
						else if (option2 == JOptionPane.NO_OPTION) {
							
							Object op[] = {"View", "Read"};
							int opt = JOptionPane.showOptionDialog(null, 
									"Choose action",
									accTitle+"- Message Box",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.PLAIN_MESSAGE,
									null,
									op, op[1]);
							
							if (opt == JOptionPane.YES_OPTION) {
								JOptionPane.showMessageDialog(frame, temp.viewInbox());
							} else if (opt == JOptionPane.NO_OPTION) {
								JOptionPane.showMessageDialog(frame, temp.readInbox());
							}
							
						} 

						// Logout
						else if (option2 == JOptionPane.CANCEL_OPTION) {
						}
					}
				}


			});
		
		// 3. Exit button
		frame.panel2.add(frame.exit = new JButton("Exit"));
		frame.exit.addActionListener(e -> {
			frame.dispose();
		});
		frame.panelMain.add(frame.panel1);
		frame.panelMain.add(frame.panel2);
		frame.pane.add(frame.panelMain);

		// Enable
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame.setTitle("Calculator App"); 	 	 
		frame.setSize(800, 800);  	 	 	 
		frame.setVisible(true); 
		frame.pack();

	}
}
