/*
Program: Operacje na obiektach klasy Book 
    Plik: Book.java
        -definicja publicznej klasy Book odpowiedzialnej za konstrukcje danych o ksiazkach


Autor: Paul Paczyński
Data: 28.10.21
*/




package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Book {
    private String title;
    private String author;
    private String dateOfPublication;
    private BookGenre genre;
    private int pages;

    public Book(String title_input, String author_input) throws BookException {
        setTitle(title_input);
        setAuthor(author_input);
        genre = BookGenre.UNKNOWN;

    }

    public void setTitle(String title_input) throws BookException {
        if ((title_input == null) || title_input.equals(""))
            throw new BookException("Book title cant be empty");
        this.title = title_input;

    }

    public void setAuthor(String author_input) {
        if ((author_input == null || author_input.equals("")))
            this.author = "UNKKNOWN";
        else
            this.author = author_input;
    }

    public void setDateOfPublication(int date_input) throws BookException {
        if (date_input < 0){

        
            date_input *=(-1);
            String s_date = String.valueOf(date_input);
            this.dateOfPublication = s_date + "BC";
        }
        else{
            String s_date = String.valueOf(date_input);
            this.dateOfPublication = s_date;
        }
        
    }

    public void setDateOfPublication(String date_input) throws BookException{
        if (date_input == null || date_input.equals("")){
            setDateOfPublication(0);
            return;
        }
        try {
            setDateOfPublication(Integer.parseInt(date_input));
        } catch (NumberFormatException e) {
            throw new BookException("Date of publication has to be an integer!");
        }
    }
    

    public void setGenre(BookGenre genre_input)  {
        this.genre = genre_input;
    }

    public void setGenre(String genre_input) throws BookException{
        if (genre_input == null || genre_input.equals("")) {
			this.genre = BookGenre.UNKNOWN;
			return;
		}
		for(BookGenre genre : BookGenre.values()){
			if (genre.genreName.equals(genre_input)) {
				this.genre = genre;
				return;
			}
		}
		throw new BookException("This genre does not exist");
    }

    public void setPages(int pages_input) throws BookException{
        if (pages_input <=0 ){
            throw new BookException("Number of pages has to be greater than 0");
        }
        else{
            this.pages = pages_input;
        }
        
    }
    public void setPages(String pages_input) throws BookException{
        if (pages_input ==null || pages_input.equals("")){
            throw new BookException("Number of pages has to be specified");
        }
        try {
            setPages(Integer.parseInt(pages_input));
        } catch (NumberFormatException e) {
            throw new BookException("Number of pages has to be an integer");
        }

        
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public int getPages(){
        return pages;
    }

    public static void toFile(PrintWriter writer, Book book) {
        writer.println(book.title + ";" + book.author + ";" + book.dateOfPublication + ";" + book.genre + ";" + book.pages);

    }

    public static void toFile(String file_name, Book book) throws BookException{
        try (PrintWriter writer = new PrintWriter(file_name)){
            toFile(writer, book);
        } catch (FileNotFoundException e){
            throw new BookException("File" + " " + file_name + " not found.");
        } catch (NullPointerException e){
            throw new BookException("No data to save");
        }
    }

    public static Book fromFile(BufferedReader reader) throws BookException{
        try {
            String line = reader.readLine();
            String[] text = line.split(";");
            Book book = new Book(text[0],text[1]);
            book.setDateOfPublication(text[2]);
            book.setGenre(text[3]);
            book.setPages(text[4]);
            return book;
        } catch (IOException e) {
            throw new BookException("Couldnt read files.");
        }
    }
    public static Book fromFile(String file_name) throws BookException{
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))){  
            return Book.fromFile(reader);

        } catch (FileNotFoundException e) {
            throw new BookException("Couldnt find " + file_name);
        } catch (IOException e){
            throw new BookException("An error has occured");

        }
    }
}