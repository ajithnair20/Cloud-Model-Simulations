import Utils.DatacenterUtils;
import Utils.Master;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyWorstFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*
    Simulation to demonstrate Map-Reduce Operation using two Datacenters.
    Configuration Details:
    -----------------------------------------------------------------------------------------------------
    Number of Datacenters: 2
    Number of Mappers: 10
    Number of Reducers: 5
    Number of VMs: 10
    Number of Hosts: 1 on each Datacenter
    Cost of Operation Per Sec: 3.0
    Cost Per Memory: 0.05
    Cost Per Storage: 0.1
    Cost Per BandWidth: 0.1
    VM Scheduling Policy: Time Shared
    VM Allocation Policy: Worst Fit
    Cloudlet Scheduling Policy: Time Shared

*/

public class Simulation6 {
    private final DatacenterBroker broker1;
    private final Datacenter datacenter1,datacenter2;
    private List<Cloudlet> cloudletList;
    private List<Vm> vmlist;
    private CloudSim simulation;
    private DatacenterUtils utils;
    private static Master master;
    private ArrayList<Cloudlet> mapperList;
    private ArrayList<Cloudlet> reducerList;
    private int numMappers = 1, numReducers = 1, numHosts = 1, numVm = 1;
    private double costPerSec = 0.0, costPerMem = 0.0, costPerStorage = 0.0, costPerBW = 0.0;
    Logger logger;
    Config config;
    private String CONFIG_PATH = "Configuration2.conf";

    public static void main(String[] args) {
        new Simulation6();
    }

    Simulation6() {
        //Declaring and initialising variables
        logger = LoggerFactory.getLogger(DatacenterUtils.class);
        config = ConfigFactory.load(CONFIG_PATH);
        utils = new DatacenterUtils(CONFIG_PATH);

        try{
            numMappers = config.getInt("mapperCount");
            numReducers = config.getInt("mapperCount");
            costPerSec = config.getDouble("costPerSec");
            costPerMem = config.getDouble("costPerMem");
            costPerStorage = config.getDouble("costPerStorage");
            costPerBW = config.getDouble("costPerBW");
            numHosts = config.getInt("numHosts");
            numVm = config.getInt("numVm");
        }
        catch(Exception e){
            logger.error("Error reading configuration details. Kindly restart the application");
            e.printStackTrace();
        }
        master = new Master();
        mapperList = new ArrayList<Cloudlet>();
        reducerList = new ArrayList<Cloudlet>();
        logger.info("Starting simulation");


        vmlist = new ArrayList<>();
        cloudletList = new ArrayList<>();

        simulation = new CloudSim();
        datacenter1 = utils.createSimpleDatacenter(simulation, numHosts, "HostType1",costPerSec, costPerMem, costPerStorage, costPerBW,new VmSchedulerTimeShared(), new VmAllocationPolicyWorstFit());
        datacenter2 = utils.createSimpleDatacenter(simulation, numHosts, "HostType2",costPerSec, costPerMem, costPerStorage, costPerBW, new VmSchedulerTimeShared(), new VmAllocationPolicyWorstFit());
        broker1 = utils.createBroker(simulation,"Broker1");
        configureNetwork();
        master.initializeMapperReducerMapping(numMappers);
        createAndSubmitVms(numVm);
        mapReduce();

        simulation.start();

        displayReducerMapperMapping();
        new CloudletsTableBuilder(broker1.getCloudletFinishedList()).build();

        logger.info("Simulation Completed");
    }

    private void configureNetwork() {
        //Configure network by mapping CloudSim entities to BRITE entities
        //Mesh Topology
        NetworkTopology networkTopology = new BriteNetworkTopology();
        simulation.setNetworkTopology(networkTopology);
        networkTopology.addLink(datacenter1.getId(), broker1.getId(), 10.0, 10);
        networkTopology.addLink(datacenter1.getId(), datacenter2.getId(), 10.0, 10);
        networkTopology.addLink(datacenter2.getId(), broker1.getId(), 10.0, 10);
    }

    private void createAndSubmitVms(int numVm) {
        while(numVm>0){
            vmlist.add(utils.createVM(new CloudletSchedulerSpaceShared()));
            numVm--;
        }
        broker1.submitVmList(vmlist);
    }

    private void mapReduce(){
        int i;
        Cloudlet red;
        //Creating a queue with reducers available for masters
        for(i=0;i<numReducers;i++){
            red = utils.createReducer("Reducer1",new UtilizationModelFull());
            red.setId(numMappers + 100 + i);
            master.reducerList.add(red);
        }
        //Creating mappers and assigning to broker
        for(i=0; i < numMappers; i++){
            Cloudlet mapper = utils.createMapper("Mapper1", new UtilizationModelFull());
            mapper.setId(i);
            mapper.addOnFinishListener(v -> {
                executeReduce(mapper);
            });
            mapper.assignToDatacenter(datacenter1);
            mapperList.add(mapper);
        }
        broker1.submitCloudletList(mapperList);
    }

    public void executeReduce(Cloudlet mapper){
        try {
            long reducerId = master.mapperRedcuerMapping.get(mapper.getId());
            int executedMappers = master.invokeReducer.get(reducerId);
            master.invokeReducer.put(reducerId,++executedMappers);

            if(executedMappers == 2) {
                ArrayList<Cloudlet> reducer = new ArrayList<>();
                Cloudlet red = utils.createReducer("Reducer1", new UtilizationModelFull());
                red.setId(reducerId);
                red.assignToDatacenter(datacenter2);
                reducerList.add(red);
                broker1.submitCloudlet(red);
                logger.info("Reducer with ID: " + red.getId() + " submitted for execution");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("Completed execution of Mapper with ID: " + mapper.getId());
    }

    public static void displayReducerMapperMapping(){
        System.out.println("MAPPER  AND REDUCER MAPPING");
        System.out.println("Mapper ID\tReducer ID");
        System.out.println("---------------------");
        master.mapperRedcuerMapping.forEach((k,v) -> System.out.println(k.toString() + "\t\t\t\t" + v.toString()));
    }
}
