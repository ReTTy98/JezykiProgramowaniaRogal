/*
Plik: PhoneBook.java
Program: Ksiazka telefoniczna - prosty program serwer-klient
Autor: Paul Paczyński

Klasa definiujaca dzialanie ksiazki telefonicznej. Obiekt tej klasy tworozny jest tylko raz
podczas startu serwera i na nim operuja wszyscy klienci i serwer.

Data: Styczeń 2022


*/

package src;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook {

    private ConcurrentHashMap<String, String> phoneList;

    PhoneBook() {

        phoneList = new ConcurrentHashMap<String, String>();

    }

    String load(String file_name) {
        String message;
        try {
            ObjectInputStream o = new ObjectInputStream(new FileInputStream(file_name + ".bin"));
            ConcurrentHashMap<String, String> loadedBook = (ConcurrentHashMap<String, String>) o.readObject();
            o.close();
            for (String key : loadedBook.keySet()) {
                if (phoneList.containsKey(key)) {
                    replaceNumber(key, loadedBook.get(key));
                } else {
                    phoneList.put(key, loadedBook.get(key));
                }

            }
            message = "Zakonczono wczytywanie ksiazki z pliku\n";
        } catch (FileNotFoundException er) {
            message = "Nie znaleziono pliku \n";
        } catch (IOException er) {
            message = "ERROR: " + er.getMessage() + "\n";

        } catch (ClassNotFoundException er) {
            message = "ERROR " + er.getMessage() + "\n";
        }

        return message.toString();

    }

    String save(String file_name) {
        String message;

        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file_name + ".bin"));
            o.writeObject(phoneList);
            o.close();
            message = "Zapisano ksiazke do pliku " + file_name + ".bin" + "\n";
        } catch (FileNotFoundException er) {
            message = "ERROR: " + er.getMessage() + "\n";
        } catch (IOException er) {
            message = "ERROR: " + er.getMessage() + "\n";
        }

        return message;
    }

    String get(String name) {
        String number = phoneList.get(name);
        String message = "Osoba " + name + " posiada numer: " + number + "\n";
        return message;
    }

    String put(String name, String number) {
        String message;
        if (phoneList.containsKey(name)) {
            message = "Taka osoba juz istnieje, uzyj /replace, aby zmienic jej numer";

        } else {
            phoneList.put(name, number);

            message = "Dodano: " + name + " :" + number + "\n";

        }

        return message;

    }

    String replaceNumber(String name, String number) {
        String message;
        if (phoneList.containsKey(name)) {
            phoneList.replace(name, number);
            message = "Imie " + name + " ma nowy numer: " + number + "\n";
        } else {
            message = "Nie znaleziono takiego imienia w bazie\n";
        }
        return message;

    }

    String delete(String name) {
        String number = phoneList.get(name);
        String message;
        if (number != null) {
            try {
                phoneList.remove(name);
                message = "Usunieto " + name + " o numerze " + number + "\n";

            } catch (NullPointerException er) {
                message = "Nie wprowadzono imienia" + "\n";

            }

        } else {
            message = "Nie znaleziono osoby w bazie\n";
        }

        return message;

    }

    String list() {
        StringBuilder message = new StringBuilder();
        for (String key : phoneList.keySet()) {
            message.append(key + ": " + phoneList.get(key) + "\n");
        }
        return message.toString();
    }

}
