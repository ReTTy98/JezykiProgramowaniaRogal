/*
	Program: Prosta aplikacja wielowątkowa
	Plik: Bus.java
	autor: Paul Paczyński
	data: styczeń 2022



*/




package data;

import java.util.concurrent.ThreadLocalRandom;

import data.NarrowBridge.BusDirection;

public class Bus implements Runnable {

    public static final int MIN_BOARDING_TIME = 1000;
    public static final int MAX_BOARDING_TIME = 10000;

    public static final int GETTING_TO_BRIDGE_TIME = 500;

    public static final int CROSSING_BRIDGE_TIME = 3000;

    public static final int GETTING_PARKING_TIME = 500;

    public static final int UNLOADING_TIME = 500;

    private static int numberOfBuses = 0;


    NarrowBridge bridge;

    int id;
    BusDirection dir;

    @Override
    public void run() {
        bridge.bussesAll.add(this);

        boarding();
        goToTheBridge();
        bridge.getOnTheBridge(this);
        rideTheBridge();
        bridge.getOffTheBridge(this);
        goToTheParking();
        unloading();
        bridge.bussesAll.remove(this);
        

    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public static void sleep(int min_millis, int max_milis) {
        sleep(ThreadLocalRandom.current().nextInt(min_millis, max_milis));
    }

    public Bus(NarrowBridge bridge) {
        this.bridge = bridge;
        this.id = ++numberOfBuses;
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0)
			this.dir = BusDirection.EAST;
		else this.dir = BusDirection.WEST;
    }

   
	void printBusInfo(String message){
		bridge.frame.updateLog(("Bus[" + id + "->"+dir+"]: " + message));
	}
	
	
	
	void boarding() {
		printBusInfo("Czeka na nowych pasazerow");
		sleep(MIN_BOARDING_TIME, MAX_BOARDING_TIME);
	}

	
	void goToTheBridge() {
		printBusInfo("Jazda w strone mostu");
		sleep(GETTING_TO_BRIDGE_TIME);
	}

	
	void rideTheBridge(){
		printBusInfo("Przejazd przez most");
		sleep(CROSSING_BRIDGE_TIME);
	}

	
	void goToTheParking(){
		printBusInfo("Jazda w strone koncowego parkingu");
		sleep(GETTING_PARKING_TIME);
	}
	
	
	void unloading(){
		printBusInfo("Rozladunek pasazerow");
		sleep(UNLOADING_TIME);
	}

}