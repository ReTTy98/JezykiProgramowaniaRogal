/*

	Program: Prosta aplikacja wielowątkowa
	Plik: NarrowBridge.java
	autor: Paul Paczyński
	data: styczeń 2022


    Klasa NarrowBridge odpowiedzialna jest za zachowanie się busów na moscie
    Niestety po wielu próbach nie udało mi się zaimplementować wszystkich 
    sposobów ograniczenia ruchu.
    Wszystkie użyte sposoby powodowały błędy programu, dlatego je usunąłem.
    



*/


package data;

import java.util.LinkedList;
import java.util.List;
import gui.MainFrame;

public class NarrowBridge {

    public int traffic = 0;
    public TrafficType trafficType = TrafficType.FREE;
    public int bridgeLimit = 3;

    MainFrame frame;
    List<Bus> bussesAll = new LinkedList<Bus>();
    List<Bus> busesWaiting = new LinkedList<Bus>();
    List<Bus> busesOnBridge = new LinkedList<Bus>();

    public NarrowBridge(MainFrame frame) {
        this.frame = frame;
    }

    public List<Bus> getBussesWaiting() {
        return busesWaiting;
    }

    public void setTrafficType(int type) {
        switch (type) {
            case 0: {
                this.trafficType = TrafficType.FREE;
                break;

            }
            case 1: {
                this.trafficType = TrafficType.ONLY_ONE;
                break;

            }
        }
    }

    public enum TrafficType {
        FREE,
        DOUBLE,
        SINGLE,
        ONLY_ONE;

        @Override
        public String toString() {
            switch (this) {
                case FREE:
                    return "Bez ograniczen";
                case DOUBLE:
                    return "Dwukierunkowy";
                case SINGLE:
                    return "Jednokierunkowy";
                case ONLY_ONE:
                    return "Tylko jeden bus";
            }
            return "";
        }

    }

    public enum BusDirection {
        EAST,
        WEST;

        @Override
        public String toString() {
            switch (this) {
                case WEST:
                    return "W";
                case EAST:
                    return "E";

            }
            return "";
        }

    }

    String printBridgeInfo(Bus bus, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bus[" + bus.id + "->" + bus.dir + "]  ");
        sb.append(message);
        return sb.toString();
    }

    String getQueueInfo() {
        StringBuilder sb = new StringBuilder();
        if (busesWaiting.isEmpty()) {
            sb.append("  ");
        } else {
            for (Bus b : busesWaiting) {
                sb.append(b.id + " ");
            }

        }

        return sb.toString();

    }

    String getBridgeInfo() {

        StringBuilder sb = new StringBuilder();
        if (busesOnBridge.isEmpty()) {
            sb.append("  ");
        } else {
            for (Bus b : busesOnBridge) {
                sb.append(b.id + "  ");
            }

        }

        return sb.toString();

    }

    synchronized void getOnTheBridge(Bus bus) {
        if (trafficType == TrafficType.ONLY_ONE) {

            while (!busesOnBridge.isEmpty()) {
                if (trafficType == TrafficType.FREE) {
                    busesWaiting.remove(bus);
                    frame.updateQueue(getQueueInfo());
                    break;
                }
                busesWaiting.add(bus);
                frame.updateQueue(getQueueInfo());
                frame.updateLog(printBridgeInfo(bus, "Czeka na pojazd"));
                try {
                    wait();
                } catch (InterruptedException e) {
                }
                busesWaiting.remove(bus);
                frame.updateQueue(getQueueInfo());
                ;
            }
            busesOnBridge.add(bus);
            frame.updateBridge(getBridgeInfo());
            frame.updateLog(printBridgeInfo(bus, "Wjechal"));
        } else if (trafficType == TrafficType.FREE) {
            if (busesWaiting.contains(bus)) {
                busesWaiting.remove(bus);
            }
            busesOnBridge.add(bus);
            frame.updateBridge(getBridgeInfo());
            frame.updateLog(printBridgeInfo(bus, "Wjechal"));

        }
    }

    synchronized void getOffTheBridge(Bus bus) {
        busesOnBridge.remove(bus);
        frame.updateBridge(getBridgeInfo());
        frame.updateLog(printBridgeInfo(bus, "OPUSZCZA MOST"));
        if (trafficType == TrafficType.ONLY_ONE) {
            notify();
        } else if (trafficType == TrafficType.FREE) {
            notifyAll();
        }
    }

}