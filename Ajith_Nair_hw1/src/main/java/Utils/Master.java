package Utils;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import java.util.*;

/*
Master class included functions to manage implementation of map-reduce.
*/

public class Master {

    //Declaring members
    public Hashtable<Long, Long> mapperRedcuerMapping;  //includes mapping of mappers and corresponding reducers
    public Hashtable<Long, Integer> invokeReducer;      //Maintains count of mappers executed to
    public ArrayList<Cloudlet> reducerList; //to maintain records of available reducers sorted in ascending order

    public Master(){
        //Initializing members
        mapperRedcuerMapping = new Hashtable<Long, Long>();
        invokeReducer = new Hashtable<>();
        reducerList = new ArrayList<Cloudlet>();
    }

    //Initialize mapping of reducers and mappers
    public void initializeMapperReducerMapping(long mapperCount){
        //Set same reducer for 2 mappers
        long reducerCount=100;
        while(mapperCount>0) {
            this.invokeReducer.put(reducerCount,0);
            this.mapperRedcuerMapping.put(--mapperCount,reducerCount);
            this.mapperRedcuerMapping.put(--mapperCount,reducerCount++);
        }
    }

    //Finds a reducer which matches mapper's requirement and is alive and records entry in the mapping table
    public Cloudlet allocateReducer(Cloudlet mapper){
        Cloudlet red = null;
        for(Cloudlet reducer: this.reducerList){
            if(reducer.getLength() > mapper.getOutputSize()){
                mapperRedcuerMapping.put(mapper.getId(),reducer.getId());
                red = reducer;
                reducerList.remove(reducer);
            }
        }
        return red;
    }

    //Fetches mapper and reducer mapping
    public Hashtable<Long,Long> fetchMapperReducerMapping(){
        return this.mapperRedcuerMapping;
    }
}
