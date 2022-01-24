package src;

class Tester {
    public static void main(String[] args){
        new PhoneBookServer();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            //TODO: handle exception
        }
        new PhoneBookClient("ewa", "localhost");
        new PhoneBookClient("Paul", "localhost");
    }

    

    
}
