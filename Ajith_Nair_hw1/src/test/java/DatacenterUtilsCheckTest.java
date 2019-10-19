import Utils.DatacenterUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.junit.Test;

public class DatacenterUtilsCheckTest {
    DatacenterUtils utils1 = new DatacenterUtils("Configuration1.conf");
    DatacenterUtils utils2= new DatacenterUtils("Configuration2.conf");
    Config config1 = ConfigFactory.load("Configuration1.conf");
    Config config2 = ConfigFactory.load("Configuration2.conf");
    private int numMappers1 = 1, numReducers1 = 1, numHosts1 = 1, numVm1 = 1;
    private double costPerSec1 = 0.0, costPerMem1 = 0.0, costPerStorage1 = 0.0, costPerBW1 = 0.0;
    private int numMappers2 = 1, numReducers2 = 1, numHosts2 = 1, numVm2 = 1;
    private double costPerSec2 = 0.0, costPerMem2 = 0.0, costPerStorage2 = 0.0, costPerBW2 = 0.0;

    //Test to check method for creation of Simple Datacenter
    @Test
    public void checkCreateDatacenter(){
        numMappers1 = config1.getInt("mapperCount");
        numReducers1 = config1.getInt("mapperCount");
        costPerSec1 = config1.getDouble("costPerSec");
        costPerMem1 = config1.getDouble("costPerMem");
        costPerStorage1 = config1.getDouble("costPerStorage");
        costPerBW1 = config1.getDouble("costPerBW");
        numHosts1 = config1.getInt("numHosts");
        numVm1 = config1.getInt("numVm");

        numMappers2 = config2.getInt("mapperCount");
        numReducers2 = config2.getInt("mapperCount");
        costPerSec2 = config2.getDouble("costPerSec");
        costPerMem2 = config2.getDouble("costPerMem");
        costPerStorage2 = config2.getDouble("costPerStorage");
        costPerBW2 = config2.getDouble("costPerBW");
        numHosts2 = config2.getInt("numHosts");
        numVm2 = config2.getInt("numVm");
        CloudSim simulation = new CloudSim();

        DatacenterSimple datacenter1 = utils1.createSimpleDatacenter(simulation, numHosts1, "HostType2",costPerSec1, costPerMem1, costPerStorage1, costPerBW1, new VmSchedulerSpaceShared(), new VmAllocationPolicySimple());
        DatacenterSimple datacenter2 = utils1.createSimpleDatacenter(simulation, numHosts2, "HostType2",costPerSec2, costPerMem2, costPerStorage2, costPerBW2, new VmSchedulerSpaceShared(), new VmAllocationPolicySimple());

        if(datacenter1 == null || datacenter2 == null)
            assert(false);
        else
            assert(true);
    }

    //Test to check method for creation of Network Datacenter
    @Test
    public void checkCreateNetworkDatacenter(){
        numMappers1 = config1.getInt("mapperCount");
        numReducers1 = config1.getInt("mapperCount");
        costPerSec1 = config1.getDouble("costPerSec");
        costPerMem1 = config1.getDouble("costPerMem");
        costPerStorage1 = config1.getDouble("costPerStorage");
        costPerBW1 = config1.getDouble("costPerBW");
        numHosts1 = config1.getInt("numHosts");
        numVm1 = config1.getInt("numVm");

        numMappers2 = config2.getInt("mapperCount");
        numReducers2 = config2.getInt("mapperCount");
        costPerSec2 = config2.getDouble("costPerSec");
        costPerMem2 = config2.getDouble("costPerMem");
        costPerStorage2 = config2.getDouble("costPerStorage");
        costPerBW2 = config2.getDouble("costPerBW");
        numHosts2 = config2.getInt("numHosts");
        numVm2 = config2.getInt("numVm");
        CloudSim simulation = new CloudSim();

        DatacenterSimple datacenter1 = utils1.createNetworkDatacenter(simulation, numHosts1, "HostType2",costPerSec1, costPerMem1, costPerStorage1, costPerBW1, new VmSchedulerSpaceShared(), new VmAllocationPolicySimple());
        DatacenterSimple datacenter2 = utils2.createNetworkDatacenter(simulation, numHosts2, "HostType2",costPerSec2, costPerMem2, costPerStorage2, costPerBW2, new VmSchedulerSpaceShared(), new VmAllocationPolicySimple());

        if(datacenter1 == null || datacenter2 == null)
            assert(false);
        else
            assert(true);
    }

    //Test to check method for creation of Maper
    @Test
    public void checkCreateMapper(){
        Cloudlet mapper1 = utils1.createMapper("Mapper1", new UtilizationModelFull());
        Cloudlet mapper2 = utils2.createMapper("Mapper1", new UtilizationModelFull());

        if(mapper1 == null || mapper2 == null)
            assert(false);
        else
            assert(true);
    }

    //Test to check method for creation of Reducer
    @Test
    public void checkCreateReducer(){
        Cloudlet reducer1 = utils1.createReducer("Reducer1", new UtilizationModelFull());
        Cloudlet reducer2 = utils2.createReducer("Reducer1", new UtilizationModelFull());

        if(reducer1 == null || reducer2 == null)
            assert(false);
        else
            assert(true);
    }

    //Test to check method for creation of Host
    @Test
    public void checkCreateHost(){
        Host host1 = utils1.createHost("HostType1", new VmSchedulerSpaceShared());
        Host host2 = utils1.createHost("HostType2", new VmSchedulerSpaceShared());
        Host host3 = utils2.createHost("HostType1", new VmSchedulerSpaceShared());
        Host host4 = utils2.createHost("HostType2", new VmSchedulerSpaceShared());

        if(host1 == null || host2 == null || host3 == null || host4 == null)
            assert(false);
        else
            assert(true);
    }

    //Test to check method for creation of VM
    @Test
    public void checkCreateVM(){
        Vm vm1 = utils1.createVM(new CloudletSchedulerTimeShared());
        Vm vm2 = utils2.createVM(new CloudletSchedulerTimeShared());

        if(vm1 == null || vm2 == null)
            assert(false);
        else
            assert(true);
    }

    //Test to check method for creation of Broker
    @Test
    public void checkCreateBroker(){
        CloudSim simulation = new CloudSim();
        DatacenterBroker broker1 = utils1.createBroker(simulation, "Broker1");
        DatacenterBroker broker2 = utils1.createBroker(simulation, "Broker2");

        if(broker1 == null || broker2 == null)
            assert(false);
        else
            assert(true);
    }
}
