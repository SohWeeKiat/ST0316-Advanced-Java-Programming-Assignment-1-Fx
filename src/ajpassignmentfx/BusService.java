/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajpassignmentfx;

import java.io.*;
import java.util.*;

/**
 *
 * @author Wee Kiat
 */
public class BusService {
    private static final BusService BS_Singleton = new BusService();
    private final HashMap<String,BusStop> bus_stops;
    private final HashMap<String,Bus> bus_svcs;

    static public BusService get()
    {
        return BS_Singleton;
    }
    
    private BusService()
    {
        bus_stops = new HashMap<>();
        bus_svcs = new HashMap<>();
    }
    
    public ArrayList<BusStop> GetAllBusStop()
    {
        ArrayList<BusStop> list = new ArrayList<>(bus_stops.values());
        Collections.sort(list);
        return list;
    }
    
    public List<Bus> GetAllBuses()
    {
        List<Bus> list = new ArrayList<>(bus_svcs.values());
        Collections.sort(list);
        return list;
    }
    
    public boolean Initialize()
    {
        try{
            if (!InitializeBusStops()){
                return false;
            }else if (!InitializeBusSvcs("lta-sbst_route.csv",true)){
                return false;
            }else if (!InitializeBusSvcs("lta-smrt_route.csv",false)){
                return false;
            }else if (!InitializeBusStopLocation()){
                return false;
            }
        }catch(IOException e){
            return false;
        }
        for(BusStop bs : bus_stops.values()){
            bs.PopulateBuses(bus_svcs);
        }
        return true;
    }
    
    private boolean InitializeBusStops() throws IOException
    {
        FileReader BusStopFile;
        try{
            BusStopFile = new FileReader("lta-bus_stop_codes.csv");
        }catch(FileNotFoundException e){
            return false;
        }
        BufferedReader BusStopReader = new BufferedReader(BusStopFile);

        BusStopReader.readLine();
        String s = BusStopReader.readLine();
        do{
            StringTokenizer st = new StringTokenizer(s,",",false);
            String BusCode = st.nextToken();
            String RoadDesc = st.nextToken();
            String BusStopDesc = st.nextToken();
            bus_stops.put(BusCode,new BusStop(BusCode,RoadDesc,BusStopDesc));

            s = BusStopReader.readLine();
        }while(s != null && !s.isEmpty());
        
        BusStopReader.close();
        return true;
    }
    
    private boolean InitializeBusSvcs(String FileName,boolean SBS_Bus) throws IOException
    {
        FileReader BusSvcFile;
        try{
            BusSvcFile = new FileReader(FileName);
        }catch(FileNotFoundException e){
            return false;
        }
        BufferedReader BusSvcReader = new BufferedReader(BusSvcFile);

        BusSvcReader.readLine();
        String s = BusSvcReader.readLine();
        do{
            StringTokenizer st = new StringTokenizer(s,",",false);
            String BusCode = st.nextToken();
                
            Bus b = bus_svcs.get(BusCode);
            int Dir = Integer.parseInt(st.nextToken());
            st.nextToken();//route seq
            String BusStopCode = st.nextToken();
            if (b != null){
                BusStop bs = bus_stops.get(BusStopCode);
                b.AddBusStop(Dir,bs);
            }else{
                b = new Bus(BusCode,SBS_Bus);
                bus_svcs.put(BusCode,b);
                b.AddBusStop(Dir,bus_stops.get(BusStopCode));
            }
            s = BusSvcReader.readLine();
        }while(s != null && !s.isEmpty());
        BusSvcReader.close();
        return true;
    }
    
        
    private boolean InitializeBusStopLocation() throws IOException
    {
        FileReader BusStopLocFile;
        try{
            BusStopLocFile = new FileReader("lta-bus_stop_locations.csv");
        }catch(FileNotFoundException e){
            return false;
        }
        BufferedReader BusStopLocReader = new BufferedReader(BusStopLocFile);

        BusStopLocReader.readLine();
        String s = BusStopLocReader.readLine();
        do{
            StringTokenizer st = new StringTokenizer(s,",",false);
            double X = Double.parseDouble(st.nextToken());
            double Y = Double.parseDouble(st.nextToken());
            st.nextToken();
            String BusCode = st.nextToken();
            if (bus_stops.containsKey(BusCode)){
                bus_stops.get(BusCode).SetXY(X, Y);
            }
            
            s = BusStopLocReader.readLine();
        }while(s != null && !s.isEmpty());
        
        BusStopLocReader.close();
        return true;
    }
    
    public ArrayList<BusStop> SearchBusStop(String sub_string)
    {
        sub_string = sub_string.toLowerCase();
        ArrayList<BusStop> BS_List = new ArrayList<>();
        for(BusStop bs : bus_stops.values()){
            if (bs.toString().toLowerCase().contains(sub_string)){
                BS_List.add(bs);
            }
        }
        Collections.sort(BS_List);
        return BS_List;
    }
    
    public ArrayList<Bus> SearchBus(String sub_string)
    {
        sub_string = sub_string.toLowerCase();
        ArrayList<Bus> B_List = new ArrayList<>();
        for(Bus b : bus_svcs.values()){
            if (b.getBusCode().toLowerCase().contains(sub_string)){
                B_List.add(b);
            }
        }
        Collections.sort(B_List);
        return B_List;
    }

    public ArrayList<BusStopPathCollection> GeneratePath(BusStop Start, BusStop Dest)
    {
        ArrayList<BusStopPathCollection> paths = CommonBusStopSearch.GeneratePath(Start, Dest);
        Collections.sort(paths);
        return paths;
    }
}
