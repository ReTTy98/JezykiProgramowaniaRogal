package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PhoneBookClient extends JFrame implements ActionListener, Runnable {

    private JPanel contentPane;
    private JTextField textField;

    static final int SERVER_PORT = 3333;
    private String name;
    private String serverHost;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    JPanel panel = new JPanel();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane();


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        new PhoneBookClient(args[0],args[1]);
    }

    /**
     * Create the frame.
     */
    public PhoneBookClient(String name, String host) {
        setTitle(name);
        this.name = name;
        this.serverHost = host;
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException er) {
                    System.out.println(er);
                }

            }
            @Override
            public void windowClosed(WindowEvent e){
                windowClosing(e);
            }
        });

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
        textField.addActionListener(this);
        setVisible(true);
        new Thread(this).start();
    }


    synchronized public void printReceivedMessage(String message){
        textArea.append(message + "\n");
    }


    @Override
    public void run() {
        if (serverHost.equals("")){
            serverHost = "localhost";
        }
        try {
            socket = new Socket(serverHost,SERVER_PORT);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Polaczenie nie zostalo nawiazane", "ERROR", 0);
            setVisible(false);
            dispose();
            return;
        }
        try {
            while(true){
                String message = (String)inputStream.readObject();
                printReceivedMessage(message);
                if(message.equals("/exit")){
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    setVisible(false);
                    dispose();
                    break;
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Polaczenie zostalo zerwane","ERROR",0);
            setVisible(false);
            dispose();
        }

        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command;
        Object source = e.getSource();
        if(source == textField){
            try {
                command = textField.getText();
                outputStream.writeObject(command);
                textField.setText("");
                if(command.equals("/exit")){
                    System.out.print("CO SIE DZIEJE");
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    setVisible(false);
                    dispose();
                    return;
                }
                
            } catch (IOException er) {
                System.out.println(er);
            }
        } 
        
    }

}
