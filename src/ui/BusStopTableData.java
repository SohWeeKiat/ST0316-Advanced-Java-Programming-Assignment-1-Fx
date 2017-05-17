/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ajpassignmentfx.BusStop;

/**
 *
 * @author wee
 */
public class BusStopTableData {
    
    private final String Sequence;
    private final BusStop bus_stop;
    
    public BusStopTableData(String seq,BusStop bs)
    {
        Sequence = seq;
        bus_stop = bs;
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
}
