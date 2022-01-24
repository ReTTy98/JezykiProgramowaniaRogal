package src;


import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook {


    private ConcurrentHashMap<String,String> phoneList;

    PhoneBook(){

        this.phoneList = new ConcurrentHashMap<String,String>();


    }

    String load(String file_name){
        String message= "";

        return message;
        
    }

    String save(String file_name){
        String message=" ";
        return message;
    }

    String get(String name){
        String number  = this.phoneList.get(name);
        String message = "Osoba " + name + " posiada number: " + number + "\n";
        return message;
    }

    String put(String name, String number){
        this.phoneList.put(name, number);

        String message = "Dodano: " + name + " :" + number + "\n"; 
        return message;

    }

    String replace(String name, String number){
        String message="";
        return message;

    }

    String delete(String name){
        String message="";
        return message;

    }

    String list(){
        StringBuilder message = new StringBuilder();
        for(String key : this.phoneList.keySet()){
            message.append(key + ": " + this.phoneList.get(key) + "\n");
        }
        System.out.println(message.toString());
        return message.toString();
    }
    
}
