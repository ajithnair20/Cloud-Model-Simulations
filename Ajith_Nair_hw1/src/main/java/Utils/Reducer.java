package Utils;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.resources.ResourceManageable;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.listeners.CloudletVmEventInfo;
import org.cloudsimplus.listeners.EventListener;

import java.util.List;

public class Reducer extends CloudletSimple {

    //Maintain ID corresponding to a reducer
    private int reducerId;
    private boolean isAlive;

    public Reducer(final int id, final long length, final int pesNumber, final UtilizationModel utilizationModel){
        super(length,pesNumber,utilizationModel);
        this.reducerId = id;
        this.isAlive = true;
    }
    //Getters
    public int getReducerId(){
        return this.reducerId;
    }
    public boolean getIsAlive(){
        return this.isAlive;
    }
}
