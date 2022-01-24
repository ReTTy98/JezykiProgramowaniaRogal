/*
Program: Operacje na obiektach klasy Book 
    Plik: BookGenre.java
        -Typ wyliczeniowy rodzaju ksiazki

Autor: Paul Paczyński
Data: 28.10.21
*/




package data;

public enum BookGenre {
    UNKNOWN("XXXX"),
    FANTASY("Fantasy"),
    SCIFI("Sci-Fi"),
    SCIENCE("Science"),
    HISTORY("History"),
    CHILDREN("For children"),
    HORROR("Horror");


    String genreName;
    private BookGenre(String genre){
        genreName = genre;
    }

    @Override
    public String toString(){
        return genreName;
    }
}
