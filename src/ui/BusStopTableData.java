/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.Bus;
import ajpassignmentfx.BusStop;

/**
 *
 * @author wee
 */
public class BusStopTableData {
    
    private final String Sequence;
    private final BusStop bus_stop;
    private final Bus bus_taken;
    
    public BusStopTableData(String seq,BusStop bs)
    {
        Sequence = seq;
        bus_stop = bs;
        bus_taken = null;
    }
    
    public BusStopTableData(String seq,BusStop bs, Bus b_taken)
    {
        Sequence = seq;
        bus_stop = bs;
        bus_taken = b_taken;
    }
    
    public String getSequence()
    {
        return Sequence;
    }
    
    public String getBusStopCode()
    {
        return bus_stop.getBusStopCode();
    }
    
    public String getBusStopDesc()
    {
        return bus_stop.getBusStopDesc();
    }
    
    public String getRoadDesc()
    {
        return bus_stop.getRoadDesc();
    }
    
    public String getBusTaken()
    {
        return bus_taken.getBusCode();
    }
}
