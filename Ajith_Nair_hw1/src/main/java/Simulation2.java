import Utils.DatacenterUtils;
import Utils.Master;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*
    Simulation to demonstrate Map-Reduce Operation using one Datacenter.
    Configuration Details:
    -----------------------------------------------------------------------------------------------------
    Number of Datacenters: 1
    Number of Mappers: 4
    Number of Reducers: 2
    Number of VMs: 5
    Number of Hosts: 1
    Cost of Operation Per Sec: 3.0
    Cost Per Memory: 0.05
    Cost Per Storage: 0.1
    Cost Per BandWidth: 0.1
    VM Scheduling Policy: Time Shared
    VM Allocation Policy: Best Fit
    Cloudlet Scheduling Policy: Time Shared
*/

public class Simulation2 {
    private final DatacenterBroker broker1;
    private final Datacenter datacenter1;
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
    private String CONFIG_PATH = "Configuration1.conf";

    public static void main(String[] args) {
        new Simulation2();
    }

    Simulation2() {
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
        datacenter1 = utils.createSimpleDatacenter(simulation, numHosts, "HostType1",costPerSec, costPerMem, costPerStorage, costPerBW, new VmSchedulerSpaceShared(), new VmAllocationPolicyBestFit());
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


    //Setting up network topologies
    //Mesh Topology
    private void configureNetwork() {
        NetworkTopology networkTopology = new BriteNetworkTopology();
        simulation.setNetworkTopology(networkTopology);
        networkTopology.addLink(datacenter1.getId(), broker1.getId(), 10.0, 10);
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
        //Creating mappers and assigning to broker
        for(i=0; i < numMappers; i++){
            Cloudlet mapper = utils.createMapper("Mapper1", new UtilizationModelFull());
            mapper.setId(i);
            mapper.addOnFinishListener(v -> {
                executeReduce(mapper);
            });
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
