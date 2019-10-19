package Utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/*
DatacenterUtils comprises of functions to create Datacenter, hosts, VMs and cloudlets
*/

public class DatacenterUtils {
    private static Config config;
    private static Logger logger;

    public DatacenterUtils(String configPath){
        config = ConfigFactory.load(configPath);
        logger = LoggerFactory.getLogger(DatacenterUtils.class);
    }

    //Create Simple datacenter based on the configuration provided
    public static DatacenterSimple createSimpleDatacenter(CloudSim simulation, int numHosts, String hostType, double costPerSec, double costPerMem, double costPerStorage, double costPerBW, VmScheduler scheduler, VmAllocationPolicy policy){
        //declaring variables
        DatacenterSimple dc= null;
        List<Host> hostList;

        //Initialiizing members
        hostList = new ArrayList<>();
        while(numHosts > 0){
            hostList.add(createHost(hostType,scheduler));
            numHosts--;
        }
        dc = new DatacenterSimple(simulation, hostList, policy);
        dc.getCharacteristics()
                .setCostPerSecond(costPerSec)
                .setCostPerMem(costPerMem)
                .setCostPerStorage(costPerStorage)
                .setCostPerBw(costPerBW);

        return dc;
    }

    //Create Network datacenter based on the configuration provided
    public static NetworkDatacenter createNetworkDatacenter(CloudSim simulation, int numHosts, String hostType, double costPerSec, double costPerMem, double costPerStorage, double costPerBW, VmScheduler scheduler, VmAllocationPolicy policy){
        //declaring variables
        NetworkDatacenter dc= null;
        List<Host> hostList;

        //Initialiizing members
        hostList = new ArrayList<>();
        while(numHosts > 0){
            hostList.add(createHost(hostType,scheduler));
            numHosts--;
        }
        dc = new NetworkDatacenter(simulation, hostList, policy);
        dc.getCharacteristics()
                .setCostPerSecond(costPerSec)
                .setCostPerMem(costPerMem)
                .setCostPerStorage(costPerStorage)
                .setCostPerBw(costPerBW);

        return dc;
    }

    //Create mappers based on the type and utilization model provided
    public static Cloudlet createMapper(String type, UtilizationModel utilizationModel){
        Cloudlet mp = null;
        long length, fileSize, outputSize, pesNumber;

        try {
            length = config.getLong(type + ".length");
            fileSize = config.getLong(type + ".fileSize");
            outputSize = config.getLong(type + ".outputSize");
            pesNumber = config.getLong(type + ".pesNumber");
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }

        mp = new CloudletSimple(length, pesNumber)
                        .setFileSize(fileSize)
                        .setOutputSize(outputSize)
                        .setUtilizationModel(utilizationModel);
        return mp;
    }

    //Create reducers based on the type and utilization model provided
    public static Cloudlet createReducer(String type, UtilizationModel utilizationModel){
        Cloudlet rd = null;
        long length, fileSize, outputSize, pesNumber;

        try {
            length = config.getLong(type + ".length");
            fileSize = config.getLong(type + ".fileSize");
            outputSize = config.getLong(type + ".outputSize");
            pesNumber = config.getLong(type + ".pesNumber");
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }

        rd = new CloudletSimple(length, pesNumber)
                .setFileSize(fileSize)
                .setOutputSize(outputSize)
                .setUtilizationModel(utilizationModel);
        return rd;
    }

    //Create hosts based on the type and scheduler provided
    public static Host createHost(String type, VmScheduler scheduler){
        Host ht = null;
        long ram = 0,storage=0,bw=0, peMips=0, noOfPes = 1;
        List<Pe> hostPes = new ArrayList<>(1);

        try{
            ram = config.getInt((type + ".ram"));
            storage = config.getLong(type + ".storage");
            bw = config.getLong(type + ".bw");
            peMips = config.getLong("Pe.mips");
            noOfPes = config.getLong(type + ".noOfPes");
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }

        while(noOfPes>0) {
            hostPes.add(new PeSimple(peMips));
            noOfPes--;
        }

        ht = new HostSimple(ram, bw, storage, hostPes).setVmScheduler(scheduler);;
        return ht;
    }

    //Create VMs based on the scheduler provided
    public static Vm createVM(CloudletScheduler scheduler){
        Vm vm = null;
        long diskSize,ram,mips,bandwidth,noPes;

        try{
            diskSize = config.getLong("Vm.diskSize");
            ram = config.getLong("Vm.ram");
            mips = config.getLong("Vm.mips");
            bandwidth = config.getLong("Vm.bandwidth");
            noPes = config.getLong("Vm.noPes");
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }
        vm = new VmSimple(mips, noPes);
        vm.setRam(ram).setBw(bandwidth).setSize(diskSize).setCloudletScheduler(scheduler);
        return vm;
    }

    //Create Datacenterbroker based on the scheduler provided
    public static DatacenterBroker createBroker(CloudSim simulation, String name) {
        return new DatacenterBrokerSimple(simulation, name);
    }
}
