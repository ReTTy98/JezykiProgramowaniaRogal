package ui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Book;
import data.BookException;



public class GuiMain extends JFrame implements ActionListener {

    private Book currentBook;
    private static final String INFO_MESSAGE = 
        "Program Book - wersja okienkowa\n" +
        "Autor: Paul Paczynski\n "+
        "Data 01.12.21";
    
    
    

    JLabel titleLabel = new JLabel("Title");
    JLabel authorLabel = new JLabel("Author");
    JLabel dateOfPublicationLabel = new JLabel("Date of publication");
    JLabel genreLabel = new JLabel("Genre");
    JLabel pagesLabel = new JLabel("Number of pages");

    JTextField titleTextField = new JTextField(10);
    JTextField authorTextField = new JTextField(10);
    JTextField dateOfPublicationTextField = new JTextField(10);
    JTextField genreTextField = new JTextField(10);
    JTextField pagesTextField = new JTextField(10);

    JButton newBookButton = new JButton("Add new book");
    JButton deleteBookButton = new JButton("Delete book");
    JButton modifyBookButton = new JButton("Modify book");
    JButton loadBookButton = new JButton("Load book from file");
    JButton saveBookButton = new JButton("Save book to file");
    JButton programAuthorButton = new JButton("About program");
    JButton exitButton = new JButton("Exit");



    public GuiMain(){
        setTitle("Library Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setResizable(false);
        setLocationRelativeTo(null);

        titleTextField.setEditable(false);
        authorTextField.setEditable(false);
        dateOfPublicationTextField.setEditable(false);
        genreTextField.setEditable(false);
        pagesTextField.setEditable(false);

        newBookButton.addActionListener(this);
        deleteBookButton.addActionListener(this);
        modifyBookButton.addActionListener(this);
        loadBookButton.addActionListener(this);
        saveBookButton.addActionListener(this);
        programAuthorButton.addActionListener(this);
        exitButton.addActionListener(this);



        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 10));

        infoPanel.add(titleLabel);
        infoPanel.add(titleTextField);
        infoPanel.add(authorLabel);
        infoPanel.add(authorTextField);
        infoPanel.add(dateOfPublicationLabel);
        infoPanel.add(dateOfPublicationTextField);
        infoPanel.add(genreLabel);
        infoPanel.add(genreTextField);
        infoPanel.add(pagesLabel);
        infoPanel.add(pagesTextField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 10));
        newBookButton.setPreferredSize(new Dimension(50,50));
        buttonPanel.add(newBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(modifyBookButton);
        buttonPanel.add(loadBookButton);
        buttonPanel.add(saveBookButton);
        buttonPanel.add(programAuthorButton);
        buttonPanel.add(exitButton);



        Container conteiner =  getContentPane();
        conteiner.add(infoPanel,BorderLayout.EAST);
        conteiner.add(buttonPanel,BorderLayout.WEST);

        pack();

        
        setContentPane(conteiner);
        ShowCurrentBook();
        setVisible(true);

    }

    void ShowCurrentBook(){
        if(currentBook==null){
            titleTextField.setText("");
            authorTextField.setText("");
            dateOfPublicationTextField.setText("");
            genreTextField.setText("");
            pagesTextField.setText("");
        }
        else{
            titleTextField.setText(currentBook.getTitle());
            authorTextField.setText(currentBook.getAuthor());
            dateOfPublicationTextField.setText(currentBook.getDateOfPublication());
            genreTextField.setText(""+currentBook.getGenre());
            pagesTextField.setText(""+currentBook.getPages());
        }
        

        

    }
    
    public static void main(String[] args){
        new GuiMain();

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object eventSource = event.getSource();

        try{
            if(eventSource==newBookButton){
                currentBook = UserDialogWindow.createNewBook(this);
            }
            if(eventSource==deleteBookButton){
                currentBook = null;
            }
            if(eventSource == modifyBookButton){
                if(currentBook==null) {
                    throw new BookException("Database's empty");
                }
                UserDialogWindow.modifyBook(this, currentBook);
                
            }
            if(eventSource == loadBookButton){
                String fileName = JOptionPane.showInputDialog("Insert file's name");
				if (fileName == null || fileName.equals("")) return;  
				currentBook = Book.fromFile(fileName);

            }
            if(eventSource == saveBookButton){
                String fileName = JOptionPane.showInputDialog("Insert file's name");
                if (fileName == null || fileName.equals("")) return;
                Book.toFile(fileName, currentBook);
            }
            if(eventSource == programAuthorButton){
                JOptionPane.showMessageDialog(this, INFO_MESSAGE);
            }
            if(eventSource == exitButton){
                System.exit(0);
            }
        }
        catch(BookException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        ShowCurrentBook();
        
    }
}
