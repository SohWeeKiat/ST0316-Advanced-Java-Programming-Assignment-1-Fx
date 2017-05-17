/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajpassignmentfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 *
 * @author Wee Kiat
 */
public class CommonBusStopSearch {
    
    public static ArrayList<BusStopPathCollection> GeneratePath(BusStop Start,BusStop Dest)
    {
        ArrayList<Bus> direct_buses = new ArrayList<>();//direct paths
        ArrayList<Bus> StartBuses = Start.GetBuses();
        for(Bus b : StartBuses){
            if (b.CanReachDestination(Start, Dest)){
                direct_buses.add(b);
            }
        }
        ArrayList<ArrayList<BusStop>> transfer_list = GenerateTransferPaths(Start,Dest,direct_buses);
        ArrayList<BusStopPathCollection> collection = new ArrayList<>();
        for(Bus b : direct_buses){
            collection.add(new BusStopPathCollection(b.GenerateBusStopPath(Start, Dest)));
        }
        for(ArrayList<BusStop> transfer : transfer_list){
            LinkedList<BusStopPath> path = GeneratePathWithTransfer(Start, Dest, 
                    transfer, direct_buses);
            if (path == null)
                continue;
            BusStopPathCollection c = new BusStopPathCollection(path,transfer);
            if (!collection.contains(c))
                collection.add(c);
        }
        return collection;
    }
    
    private static  ArrayList<ArrayList<BusStop>> GenerateTransferPaths(BusStop Start,BusStop Dest,
            ArrayList<Bus> direct_buses)
    {
        HashSet<BusStop> bus_stops_checked = new HashSet<>();
        HashSet<Bus> buses_checked = new HashSet<>(direct_buses);
        
        ArrayList<BusStop> transfer_stops = new ArrayList<>();
        ArrayList<ArrayList<BusStop>> transfer_stops_collection = new ArrayList<>();
        HashSet<Bus> Buses_transfered = new HashSet<>();
        
        while(Recursive_DepthFirst_Search(Start,Start,Dest.GetPreviousBusStop(buses_checked),
                bus_stops_checked,buses_checked,transfer_stops) && transfer_stops.size() < 20){
            buses_checked.clear();
            buses_checked.addAll(direct_buses);
            bus_stops_checked.clear();
            transfer_stops_collection.add(new ArrayList<>(transfer_stops));
            Buses_transfered.addAll(GetBusesInTransfer(Start,Dest,transfer_stops,direct_buses));
            buses_checked.addAll(Buses_transfered);
            for(ArrayList<BusStop> transfer : transfer_stops_collection){
                bus_stops_checked.addAll(transfer);
            }
            transfer_stops.clear();
        }
        
        return transfer_stops_collection;
    }
    
    private static boolean Recursive_DepthFirst_Search(
    		BusStop Prev, 
                BusStop Current, 
    		LinkedHashSet<BusStop> end_bus_stop_list,
    		HashSet<BusStop> bus_stops_checked,
    		HashSet<Bus> buses_checked,
    		ArrayList<BusStop> transfer_stops)
    {
    	LinkedHashSet<BusStop> bus_stop_list = Current.GetAllReachableBusStop(Prev, buses_checked);
    	bus_stop_list.removeAll(bus_stops_checked);
    	buses_checked.addAll(Current.GetBuses());
    	if (ContainsDestination(bus_stop_list,end_bus_stop_list,transfer_stops)){
            return true;
    	}
    	for(BusStop b : bus_stop_list){
            if (!bus_stops_checked.contains(b)){
                bus_stops_checked.add(b);
                transfer_stops.add(b);
                if (Recursive_DepthFirst_Search(Current,b,end_bus_stop_list,bus_stops_checked,
                    buses_checked,transfer_stops)){
                    return true;
                }
                transfer_stops.remove(b);
            }
    	}
    	return false;
    }
    
    private static boolean ContainsDestination(LinkedHashSet<BusStop> list, HashSet<BusStop> end_list, 
            ArrayList<BusStop> transfer)
    {
    	for(BusStop b : end_list){
            if (list.contains(b)){
                transfer.add(b);
                return true;
            }
    	}
    	return false;
    }
    
    private static ArrayList<Bus> GetBusesInTransfer(BusStop Start,BusStop Dest,
        ArrayList<BusStop> transfers,
        ArrayList<Bus> ignore_direct_bus)
    {
    	ArrayList<Bus> BusTooked = new ArrayList<>();
    	ArrayList<Bus> buses = Start.GetBuses();
    	for(int i = 0;i < transfers.size();i++){
            for(Bus b : buses){
                if (ignore_direct_bus.contains(b))
                    continue;
                if (b.CanReachDestination(i == 0 ? Start : transfers.get(i - 1), transfers.get(i))){
                    BusTooked.add(b);
                    break;
                }
            }
            buses = transfers.get(i).GetBuses();
    	}
    	BusStop LastStop = transfers.get(transfers.size() - 1);
    	for(Bus b : buses){
            if (ignore_direct_bus.contains(b))
                continue;
            if (b.CanReachDestination(LastStop, Dest)){
                BusTooked.add(b);
                break;
            }
        }
    	return BusTooked;
    }
    
    private static LinkedList<BusStopPath> GeneratePathWithTransfer(BusStop Start,BusStop Dest,
        ArrayList<BusStop> transfers,
        ArrayList<Bus> ignore_direct_bus)
    {
        ArrayList<Bus> buses = Start.GetBuses();
        LinkedList<BusStopPath> path = new LinkedList<>();
        for(int i = 0;i < transfers.size();i++){
            boolean BusFound = false;
            for(Bus b : buses){
                if (ignore_direct_bus.contains(b))
                    continue;
                Start = (i == 0) ? Start : transfers.get(i - 1);
                if (b.CanReachDestination(Start, transfers.get(i))){
                    path.addAll(b.GenerateBusStopPath(Start, transfers.get(i)));
                    BusFound = true;
                    break;
                }
            }
            if (!BusFound)
                return null;
            buses = transfers.get(i).GetBuses();
    	}
        BusStop LastStop = transfers.get(transfers.size() - 1);
        boolean BusFound = false;
    	for(Bus b : buses){
            if (ignore_direct_bus.contains(b))
                continue;
            if (b.CanReachDestination(LastStop, Dest)){
                path.addAll(b.GenerateBusStopPath(LastStop, Dest));
                BusFound = true;
                break;
            }
        }
        if (!BusFound)
            return null;
        return path;
    }
}
