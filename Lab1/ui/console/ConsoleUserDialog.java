/*
Program: Biblioteka ulatwiajaca budowanie i przesylanie informacji do konsoli
    Plik: ConsoleUserDialog.java

Autor: Paul Paczyński
Data: 28.10.21
*/




package ui.console;

import java.util.Scanner;

public class ConsoleUserDialog {
    private final String ERROR_MESSAGE = 
        "Wrong input.\nTry again.";
    private Scanner scan = new Scanner(System.in);

    public void printMessage(String message){
        System.out.println(message);
    }

    public void printErrorMessage(String message){
        System.err.println(message);
        System.err.println("Press ENTER");
        inputString("");
    }

    public void printInfoMessage(String message){
        System.out.println(message);
        inputString("Press Enter");
    }

    public void clearConsole(){
        System.out.println("\n\n\n");
    }

    public String inputString(String prompt){
        System.out.print(prompt);
        return scan.nextLine();
    }


    public int intInput(String prompt){
        boolean error;
        int i = 0;
        do{
            error = false;
            try{
                i = Integer.parseInt(inputString(prompt));
            } catch (NumberFormatException e){
                System.err.println(ERROR_MESSAGE);
                error = true;

            }
        }while(error);
        return i;
    }

}
