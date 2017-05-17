/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajpassignmentfx;

import java.util.Objects;

/**
 *
 * @author Wee Kiat
 */
public class BusStopPath {
    
    private BusStop Source;
    private BusStop Destination;
    private Bus BusTaken;
    
    public BusStopPath(BusStop Src,BusStop Dest,Bus b)
    {
        Source = Src;
        Destination = Dest;
        BusTaken = b;
    }
    
    public BusStop GetSrc()
    {
        return Source;
    }
    
    public BusStop GetDest()
    {
        return Destination;
    }
    
    public Bus GetBus()
    {
        return BusTaken;
    }
    
    @Override
    public String toString()
    {
        return "Taking " + BusTaken + " from " + Source + " _To_ " + Destination;
    }
    
    @Override
    public boolean equals(Object o)
    {
        BusStopPath p = (BusStopPath)o;
        return Destination == p.Destination;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.Destination);
        return hash;
    }
}
