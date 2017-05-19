/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajpassignmentfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 *
 * @author Wee Kiat
 */
public class BusStop implements Comparable{
    
    private final String bus_stop_code;
    private final String road_desc;
    private final String bus_stop_desc;
    private final ArrayList<Bus> buses;
    private double X, Y;
    
    public BusStop()
    {
        bus_stop_code = "";
        road_desc = "";
        bus_stop_desc = "";
        buses = new ArrayList<>();
    }
    
    public BusStop(String BusCode,String RoadDesc,String BusStopDesc)
    {
    	bus_stop_code = BusCode;
        road_desc = RoadDesc;
        bus_stop_desc = BusStopDesc;
        buses = new ArrayList<>();
    }
    
    public String getBusStopCode()
    {
        return bus_stop_code;
    }
    
    public String getRoadDesc()
    {
        return road_desc;
    }
    
    public String getBusStopDesc()
    {
        return bus_stop_desc;
    }
    
    public ArrayList<Bus> GetBuses()
    {
        return buses;
    }
    
    public void SetXY(double X,double Y)
    {
        this.X = X;
        this.Y = Y;
    }
    
    public double GetX()
    {
        return X;
    }
    
    public double GetY()
    {
        return Y;
    }
    
    @Override
    public String toString() 
    {
    	return bus_stop_code + " - " + bus_stop_desc + " , " + road_desc;
    }
    
    public void PopulateBuses(HashMap<String,Bus> bus_svcs)
    {
        for(Bus b : bus_svcs.values()){
            if (b.ServingBusStop(this)){
                buses.add(b);
            }
        }
    }
    
    public LinkedHashSet<BusStop> GetAllReachableBusStop(BusStop prev,HashSet<Bus> ignore_buses)
    {
    	LinkedHashSet<BusStop> list = new LinkedHashSet<>();
    	for(Bus b : buses){
            if (!ignore_buses.contains(b))
                list.addAll(b.GetReachableBusStops(prev,this));
    	}
    	return list;
    }
    
    public LinkedHashSet<BusStop> GetPreviousBusStop(HashSet<Bus> ignore_buses)
    {
    	LinkedHashSet<BusStop> list = new LinkedHashSet<>();
    	for(Bus b : buses){
            if (ignore_buses.contains(b))
                continue;
            list.addAll(b.GetPreviousBusStops(this));
    	}
    	return list;
    }
    
    @Override
    public int compareTo(Object o) {
        BusStop bs2 = (BusStop)o;
        boolean isFirstCharAlpha1 = bus_stop_code.charAt(0) >= 'A';
        boolean isFirstCharAlpha2 = bs2.bus_stop_code.charAt(0) >= 'A';
        if (isFirstCharAlpha1 && isFirstCharAlpha2){
            return bus_stop_code.compareTo(bs2.bus_stop_code);
        }else if (isFirstCharAlpha1 && !isFirstCharAlpha2){
            return 1;
        }else if (isFirstCharAlpha2 && !isFirstCharAlpha1){
            return -1;
        }
        int BusStop1 = Integer.parseInt(bus_stop_code);
        int BusStop2 = Integer.parseInt(bs2.bus_stop_code);
        if (BusStop1 > BusStop2){
            return 1;
        }else if (BusStop1 == BusStop2){
            return 0;
        }
        return -1;
    }
}
