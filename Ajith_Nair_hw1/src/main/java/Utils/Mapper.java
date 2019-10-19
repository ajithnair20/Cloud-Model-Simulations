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

public class Mapper extends CloudletSimple {
    //Maintain ID corresponding to a reducer
    private int mapperId;
    private boolean isAlive;

    public Mapper(final long length, final int pesNumber, final UtilizationModel utilizationModel){
        super(length,pesNumber,utilizationModel);
        this.isAlive = true;
    }
     //Getters and Setters
    public int getMapperId(){
        return this.mapperId;
    }
    public boolean getIsAlive(){
        return this.isAlive;
    }
    public void setMapperId(int id){
        this.mapperId = id;
    }
}
