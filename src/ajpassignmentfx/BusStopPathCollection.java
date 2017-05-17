/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajpassignmentfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author wee
 */
public class BusStopPathCollection implements Comparable{
    
    private final LinkedList<BusStopPath> path;
    private final ArrayList<BusStop> transfers;
    
    public BusStopPathCollection(LinkedList<BusStopPath> p)
    {
        path = p;
        transfers = null;
    }
    
    public BusStopPathCollection(LinkedList<BusStopPath> p,ArrayList<BusStop> t)
    {
        path = p;
        transfers = t;
    }
    
    public LinkedList<BusStopPath> GetPath()
    {
        return path;
    }
    
    private BusStop GetStartLocation()
    {
        return path.get(0).GetSrc();
    }
    
    private BusStop GetDestination()
    {
        return path.get(path.size() - 1).GetDest();
    }

    public ArrayList<Bus> GetBusesTaken()
    {
        ArrayList<Bus> buses = new ArrayList<>();
        for(BusStopPath p : path){
            if (!buses.contains(p.GetBus()))
                buses.add(p.GetBus());
        }
        return buses;
    }
    
    private HashSet<Bus> GetBusesAvilToTake()
    {
        BusStop StartLoc = GetStartLocation();
        ArrayList<Bus> buses = StartLoc.GetBuses();
        HashSet<Bus> avail_buses = new HashSet<>();
        for(int i = 0;i < transfers.size();i++){
            for(Bus b : buses){
                if (b.CanReachDestination(i == 0 ? StartLoc : transfers.get(i - 1), 
                        transfers.get(i))){
                    avail_buses.add(b);
                }
            }
            buses = transfers.get(i).GetBuses();
        }
        BusStop LastStop = transfers.get(transfers.size() - 1);
        BusStop Dest = GetDestination();
    	for(Bus b : buses){
            if (b.CanReachDestination(LastStop, Dest)){
                avail_buses.add(b);
            }
        }
        return avail_buses;
    }
    
    @Override
    public int compareTo(Object o) {
        BusStopPathCollection c = (BusStopPathCollection)o;
        if (path.size() > c.path.size())
            return 1;
        else if (path.size() == c.path.size()){
            ArrayList<Bus> BusListA = GetBusesTaken();
            ArrayList<Bus> BusListB = c.GetBusesTaken();
            if (BusListA.equals(BusListB)){
                HashSet<Bus> BusAvailA = GetBusesAvilToTake();
                HashSet<Bus> BusAvailB = c.GetBusesAvilToTake();
                if (BusAvailA.equals(BusAvailB))
                    return 0;
                else if (BusAvailA.size() > BusAvailB.size())
                    return 1;
                else if (BusAvailA.size() < BusAvailA.size())
                    return -1;
                return 0;
            }else if (BusListA.size() > BusListB.size())
                return 1;
            else if (BusListA.size() < BusListB.size())
                return -1;
            return 0;
        }
        return -1;
    }
    
    @Override
    public boolean equals(Object o)
    {
        BusStopPathCollection c = (BusStopPathCollection)o;
        if (path.size() != c.path.size())
            return false;
        if (GetBusesTaken().equals(c.GetBusesTaken())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.path);
        return hash;
    }
    
    @Override
    public String toString()
    {
        return "[BusStops:" + path.size() + " Buses:" + GetBusesTaken().size() + "]";
    }
}
