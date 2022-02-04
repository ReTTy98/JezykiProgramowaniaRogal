/*
Plik: PhoneBookServer.java
Program: Ksiazka telefoniczna - prosty program serwer-klient
Autor: Paul Paczyński

Program tworzy okienko serwera oraz serwer, pozwalajac na polaczenie sie klientow.
Operujac na okienku PhoneBookServer możemy używać prawie wszystkich komend dostępnych
dla klientów, oraz dodatkowej komendy administratora. 
GUI wygenerowane przez plugin  WindowBuilder dla aplikacji Eclipse

Komunikaty wyswietlane w tym okienku są logami wprowadzonych komend przez uzytkownika i odpowiedzia
jaka serwer wyslal klientowi
Np:
Hyzio wprowadzil : /put Paul 254307
Hyzio >>> Dodano Paul 254307

komenda /save zapisuje plik w folderze w ktorym uruchomiony zostal program. 
Tak samo komenda /load wczytuje plik tylko z folderu w ktorym uruchomiony zostal program.

Data: Styczeń 2022


*/

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class PhoneBookServer extends JFrame implements ActionListener, Runnable {

	private JPanel contentPane;
	private JTextField textField;
	static final int SERVER_PORT = 3333;

	static final String COMMANDS = "/get <name>  - Pokaz numer danej osoby\n " +
			"/delete <name> - Usun osobe o danym imieniu z kolekcji\n" +
			"/save <file name> - Zapisz kolekcje do pliku (nie podawaj rozszerzenia)\n" +
			"/load <file name> - Wczytaj kolekcje z pliku. UWAGA! Dodaje brakujace elementy, zastepuje istniejace elementy. (nie podawaj rozszerzenia)\n "
			+
			"/put <name> <number> - Dodaj osobe i jej numer do kolekcji\n" +
			"/replace <name> <new number> - Zmien number osoby istniejacej w kolekcji\n" +
			"/help  - Wyswietl liste komend\n" +
			"/close - Zakoncz prace serwera. Komenda dostępna tylko z okna serera \n" +
			"/bye - Zamknij prace klienta. Komenda dostępna tylko z okna klienta.\n";

	JScrollPane scrollPane = new JScrollPane();
	JTextArea textArea = new JTextArea();
	JPanel panel = new JPanel();
	PhoneBook book = new PhoneBook();
	private final JLabel lblNewLabel = new JLabel("/help - lista komend");

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
		setBounds(100, 100, 434, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setBounds(5, 11, 403, 321);
		contentPane.add(scrollPane);
		textArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		panel.setBounds(5, 338, 403, 96);
		contentPane.add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(10, 36, 383, 38);
		textField.addActionListener(this);
		panel.add(textField);
		textField.setColumns(10);
		lblNewLabel.setBounds(10, 11, 146, 14);

		panel.add(lblNewLabel);
		setVisible(true);
		new Thread(this).start();
	}

	@Override
	public void run() {
		boolean socket_created = false;
		try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
			String host = InetAddress.getLocalHost().getHostName();
			textArea.append("Uruchomiono serwer na hoscie " + host + "\n");
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
				JOptionPane.showMessageDialog(null, "Nie można utworzyć gniazda serwera", "ERROR", 0);
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
			command = textField.getText();
			textField.setText("");
			if (command.trim().equals("/close")) {
				System.exit(0);
			}
			readCommand(null, command);

		}

	}

	synchronized public void readCommand(ClientThread client, String message) {
		String command;
		String outMessage;
		message = message.trim();
		String file_path = System.getProperty("user.dir");
		if (message.contains(" ")) {
			int spaces = message.replaceAll("[^ ]", "").length();
			String arr[] = message.split(" ", 2);
			command = arr[0];
			String args = arr[1];

			if (spaces == 1) {
				switch (command) {
					case "/get":
						outMessage = book.get(args);
						printMessage(client, message, outMessage);
						break;

					case "/delete":
						outMessage = book.delete(args);
						printMessage(client, message, outMessage);
						break;

					case "/save":
						outMessage = book.save(file_path + "\\" + args);
						printMessage(client, message, outMessage);
						break;

					case "/load":
						outMessage = book.load(file_path + "\\" + args);
						printMessage(client, message, outMessage);
						break;

					default:
						outMessage = "Nie rozpoznano komendy\n";
						printMessage(client, message, outMessage);
						break;

				}

			} else if (spaces == 2) {
				String multi_args[] = args.split(" ", 2);
				String arg1 = multi_args[0];
				String arg2 = multi_args[1];

				switch (command) {

					case "/put":
						outMessage = book.put(arg1, arg2);
						printMessage(client, message, outMessage);
						break;

					case "/replace":
						outMessage = book.replaceNumber(arg1, arg2);
						printMessage(client, message, outMessage);
						break;

					default:
						outMessage = "Nie rozpoznano komendy\n";
						printMessage(client, message, outMessage);
						break;

				}

			}
		} else {
			command = message;
			switch (command) {
				case "/list":
					outMessage = book.list();
					printMessage(client, message, outMessage);
					break;
				case "/help":
					outMessage = COMMANDS;
					printMessage(client, message, outMessage);
					break;
				case "/bye":
					textArea.append(client.getName() + " zakonczyl polaczenie\n");
					break;
				default:
					outMessage = "Nie rozpoznano komendy\n";
					printMessage(client, message, outMessage);
					break;

			}

		}

	}

	synchronized public void printMessage(ClientThread client, String inputMessage, String outputMessage) {
		if (client != null) {
			client.sendMessage(outputMessage);
			textArea.append(client.getName() + " Wprowadzil:  " + inputMessage + "\n");
			textArea.append(client.getName() + ">>> " + outputMessage + "\n");

		} else {
			textArea.append("Administrator: " + inputMessage + "\n");
			textArea.append("@>>>" + outputMessage + "\n");

		}

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

	public String getName() {
		return name;
	}

	public void sendMessage(String message) {
		try {
			outputStream.writeObject(message);
		} catch (IOException er) {
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
			name = (String) input.readObject();
			while (true) {
				message = (String) input.readObject();
				output.writeObject(message);
				myServer.readCommand(this, message);

				if (message.trim().equals("/bye")) {
					break;
				}

			}
			socket.close();
			socket = null;
		} catch (Exception er) {

		}

	}

}