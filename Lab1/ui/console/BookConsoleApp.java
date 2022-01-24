/*
Program: Aplikacja dzialajaca w konsoli. Dzieki niej testujemy operacje
na klasach Book. Znajduje sie tutaj glowna metoda main()
Zalecane uruchomienie programu poprzez uruchomienie pliku Lab1.jar w folderze Lab1
    Plik: BookConsoleApp.java
        -

Autor: Paul Paczyński
Data: 28.10.21
*/





package ui.console;


import java.util.Arrays;

import data.Book;
import data.BookException;
import data.BookGenre;

public class BookConsoleApp {
    private static final String GREETING_MESSAGE = 
    "Program Book - wersja konsolowa\n"+
    "Autor : Paul Paczyński\n"+
    "Data : 28.10.2021\n";

private static final String MENU = 
    "   MENU    \n"+
    "1. Register new book\n"+
    "2. Delete registered book\n"+
    "3. Modify registered book\n"+
    "4. Load data from file\n"+
    "5. Save data to file\n"+
    "0. Kill program\n";
private static final String MODIFY_MENU = 
    "What do you want to change?     \n"+
    "1. Title\n"+
    "2. Author\n"+
    "3. Date of publication\n"+
    "4. Genre\n"+
    "5. Pages\n"+
    "0. Back\n";


private static ConsoleUserDialog ui = new ConsoleUserDialog();
private Book currentBook = null;


public static void main(String[] args){
    BookConsoleApp app = new BookConsoleApp();
    app.mainLoop();
}


static Book createNewBook(){
    String title = ui.inputString("Insert title of the book: ");
    String author = ui.inputString("Insert author of the book (pass nothing if unknown): ");
    String date_of_publication = ui.inputString("Insert date of publication (negative number means BC): ");
    ui.printMessage("Aviable genres: " + Arrays.deepToString(BookGenre.values()));
    String genre = ui.inputString("Insert genre of the book: ");
    String pages = ui.inputString("How many pages does this book have: ");
    Book book;

    try{
        book = new Book(title, author);
        book.setDateOfPublication(date_of_publication);
        book.setGenre(genre);
        book.setPages(pages);
    } catch (BookException e) {
        ui.printErrorMessage(e.getMessage());
        return null;
    }
    return book;


}

static void modifyBookData(Book book){
    while(true){
        ui.clearConsole();
        showBook(book);

        try{
            switch(ui.intInput(MODIFY_MENU)){
            case 1:
                book.setTitle(ui.inputString("Insert title of the book "));
                break;
            case 2:
                book.setAuthor(ui.inputString("Insert author of the book (pass nothing if unknown)"));
                break;
            case 3:
                book.setDateOfPublication(ui.inputString("Insert date of publication (negative number means BC)"));
                break;
            case 4:
                ui.printMessage("Aviable genres: " + Arrays.deepToString(BookGenre.values()));
                book.setGenre(ui.inputString("Insert genre of the book"));
                break;
            case 5:
                book.setPages(ui.inputString("How many pages does this book have"));
                break;
            case 0:
                return;
            }
        } catch (BookException e){
            ui.printErrorMessage(e.getMessage());
        }
    }
}
void showCurrentBook(){
    showBook(currentBook);
}
static void showBook(Book book){
    StringBuilder sb = new StringBuilder();

    if (book != null){
        sb.append("Current book:\n");
        sb.append("Title: " + book.getTitle() + "\n");
        sb.append("Author: " + book.getAuthor() + "\n");
        sb.append("Date of publication: " + book.getDateOfPublication() + "\n");
        sb.append("Genre: " + book.getGenre() + "\n");
        sb.append("Number of pages: " + book.getPages() + "\n");
    } else
        sb.append("No data" + "\n");
    ui.printMessage(sb.toString());
}



public void mainLoop() {
    ui.printMessage(GREETING_MESSAGE);

    while (true){
        ui.clearConsole();
        showCurrentBook();
        try{
            switch (ui.intInput(MENU)){
            case 1:
                currentBook = createNewBook();
                break;
            case 2:
                currentBook = null;
                break;
            case 3:
                if (currentBook == null) throw new BookException("There are no book in database");
                modifyBookData(currentBook);
                break;
            case 4:{
                String file_name = ui.inputString("Enter file name: ");
                currentBook = Book.fromFile(file_name);
                ui.printMessage("Data loaded from file ");
            }
                break;
            case 5:{
                String file_name = ui.inputString("Enter file name: ");
                Book.toFile(file_name, currentBook);
                ui.printInfoMessage("Data saved to file ");
            }
                break;
            case 0:
                System.exit(0);
            }
        } catch (BookException e){
            ui.printErrorMessage(e.getMessage());
        }

    }

}

    
}
