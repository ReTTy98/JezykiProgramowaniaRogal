/*
Plik: Tester.java
Program: Ksiazka telefoniczna - prosty program serwer-klient
Autor: Paul Paczyński

Jest to główna klasa aplikacji. Po jej uruchomieniu tworzy się serwer i 3 klientów.

Data: Styczeń 2022


*/




package src;

class Tester {
    public static void main(String[] args){
        new PhoneBookServer();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        new PhoneBookClient("Hyzio", "localhost");
        new PhoneBookClient("Zyzio", "localhost");
        new PhoneBookClient("Dyzio", "localhost");
    }

    

    
}
