/*
Program: Operacje na obiektach klasy Book 
    Plik: BookException.java
        -definicja publicznej klasy BookException.java
        -Pozwala wyswietlac moje bledy w konsoli

Autor: Paul Paczyński
Data: 28.10.21
*/





package data;

public class BookException extends Exception {

    private static final long serialVersionUID = 1L;

    public BookException(String message) {
        super(message);
    }

    
}
