package ui.gui;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Book;
import data.BookException;
import data.BookGenre;

public class UserDialogWindow extends JDialog implements ActionListener {

    private Book book;

    JLabel titleLabel = new JLabel("Title");
    JLabel authorLabel = new JLabel("Author");
    JLabel dateOfPublicationLabel = new JLabel("Date of publication");
    JLabel genreLabel = new JLabel("Genre");
    JLabel pagesLabel = new JLabel("Number of pages");

    JTextField titleTextField = new JTextField(10);
    JTextField authorTextField = new JTextField(10);
    JTextField dateOfPublicationTextField = new JTextField(10);
    JComboBox<BookGenre> genreTextField = new JComboBox<BookGenre>(BookGenre.values());
    JTextField pagesTextField = new JTextField(10);

    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");

    private UserDialogWindow(Window parent, Book book){
        super(parent, Dialog.ModalityType.DOCUMENT_MODAL);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(135,342);
        setLocationRelativeTo(parent);

        this.book = book;

        if(book==null){
            setTitle("Add book");
        }
        else{
            setTitle(book.toString());
            titleTextField.setText(book.getTitle());
            authorTextField.setText(book.getAuthor());
            dateOfPublicationTextField.setText(book.getDateOfPublication());;
            genreTextField.setSelectedItem(book.getGenre());
            pagesTextField.setText(""+book.getPages());
        }


        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel panel = new JPanel();

        panel.add(titleLabel);
        panel.add(titleTextField);
        panel.add(authorLabel);
        panel.add(authorTextField);
        panel.add(dateOfPublicationLabel);
        panel.add(dateOfPublicationTextField);
        panel.add(genreLabel);
        panel.add(genreTextField);
        panel.add(pagesLabel);
        panel.add(pagesTextField);
        panel.add(okButton);
        panel.add(cancelButton);

        setContentPane(panel);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if(source==okButton){
            try{
                if (book==null){
                    book = new Book(titleTextField.getText(),authorTextField.getText());
                }
                else{
                    book.setTitle(titleTextField.getText());
                    book.setAuthor(authorTextField.getText());
                }
                book.setDateOfPublication(dateOfPublicationTextField.getText());
                book.setGenre((BookGenre) genreTextField.getSelectedItem());
                book.setPages(pagesTextField.getText());
                dispose();
            }
            catch (BookException e){
                JOptionPane.showMessageDialog(this, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);

            }
        }
        if(source==cancelButton){
            dispose();
        }

        
        
    }

    public static Book createNewBook(Window parent){
        UserDialogWindow dialog = new UserDialogWindow(parent, null);
        return dialog.book;
    }

    public static void modifyBook(Window parent,Book book){
        new UserDialogWindow(parent, book);
    }
    
}
