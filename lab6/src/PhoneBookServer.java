package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PhoneBookServer extends JFrame implements ActionListener, Runnable {

	private JPanel contentPane;
	private JTextField textField;
	static final int SERVER_PORT = 3333;

	JScrollPane scrollPane = new JScrollPane();
	JTextArea textArea = new JTextArea();
	JPanel panel = new JPanel();
	PhoneBook book = new PhoneBook();

	public static void main(String[] args) {
		new PhoneBookServer();
	}

	/**
	 * Create the frame.
	 */
	public PhoneBookServer() {
		setTitle("PhoneBookServer");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 405, 473);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane.setBounds(5, 11, 384, 321);
		contentPane.add(scrollPane);

		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		panel.setBounds(5, 338, 384, 96);
		contentPane.add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(0, 65, 374, 20);
		panel.add(textField);
		textField.setColumns(10);
		setVisible(true);
		new Thread(this).start();
	}

	@Override
	public void run() {
		boolean socket_created = false;
		try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
			String host = InetAddress.getLocalHost().getHostName();
			System.out.println("Uruchomiono na hoscie" + host);
			socket_created = true;

			while (true) {
				Socket socket = server.accept();
				if (socket != null) {
					new ClientThread(this, socket);
				}
			}

		} catch (IOException er) {
			System.out.println(er);
			if (!socket_created) {
				JOptionPane.showMessageDialog(null, "Nie można utworzyć gniazda serwera", "Error", 0);
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "ERROR: Nie mozna polaczyc sie z klientem", "ERROR", 0);
			}

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command;
		Object source = e.getSource();
		if (source == textField) {

		}

	}

	synchronized public void readCommand(ClientThread client, String message) {
		String command;
		String out_message;
		if (message.contains(" ")) {
			int spaces = message.replaceAll("[^ ]", "").length();
			String arr[] = message.split(" ", 2);
			command = arr[0];
			String args = arr[1];

			switch (command) {
				case "/get":
					out_message = book.get(args);
					textArea.append(out_message);
					client.sendMessage(out_message);

					break;
				case "/put":
					String multi_args[] = args.split(" ", 2);
					String arg1 = multi_args[0];
					String arg2 = multi_args[1];
					out_message = book.put(arg1,arg2);
					textArea.append(out_message);
					client.sendMessage(out_message);
					break;
				default:
					break;
			}
		} else {
			command = message;
			switch (command) {
				case "/list":
					out_message = book.list();
					textArea.append(out_message);
					client.sendMessage(out_message);
					break;

			}

		}

	}

	synchronized public void printCommand(ClientThread client, String message) {
		textArea.append(message + "\n");
	}

}

class ClientThread implements Runnable {
	private Socket socket;
	private String name;
	private PhoneBookServer myServer;
	private ObjectOutputStream outputStream = null;

	ClientThread(PhoneBookServer server, Socket socket) {
		myServer = server;
		this.socket = socket;
		new Thread(this).start();

	}

	public void sendMessage(String message){
		try{
			outputStream.writeObject(message);
		}
		catch(IOException er){
			er.printStackTrace();
		}
	}

	@Override
	public void run() {
		String message;

		try (
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
			outputStream = output;
			while (true) {
				message = (String) input.readObject();
				output.writeObject(message);
				System.out.println(message);
				myServer.readCommand(this, message);
				myServer.printCommand(this, message);

				if (message.equals("/exit")) {
					break;
				}

			}
			socket.close();
			socket = null;
		} catch (Exception er) {

		}

	}

}