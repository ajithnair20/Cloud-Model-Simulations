import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFieldsCheckTest {

    Config config1 = ConfigFactory.load("Configuration1.conf");
    Config config2 = ConfigFactory.load("Configuration2.conf");

    //Test to check if configuration files are loaded correctly
    @Test
    public void checkConfigFileLoad(){
        assert(!(config1 == null));
        assert(!(config2 == null));
    }

    //Test to check if ram of Hosts assigned is greater than requested VMs
    @Test
    public void checkRamAllocation(){

        //For Config File 1
        int NO_OF_HOST1 = config1.getInt("numHosts");
        int NO_OF_VM1 = config1.getInt("numVm");
        long RAM_OF_HOST1_CONFIG1 = config1.getInt("HostType1.ram");
        long RAM_OF_HOST2_CONFIG1 = config1.getInt("HostType2.ram");
        long RAM_OF_VM_CONFIG1 = config1.getInt("Vm.ram");

        //Compare total ram of hosts on datacenter with total ram of VMs requested for config file 1
        if((NO_OF_HOST1 * RAM_OF_HOST2_CONFIG1)> (NO_OF_VM1 * RAM_OF_VM_CONFIG1))
            assert(true);
        else
            assert(false);

        //For Config File 2
        int NO_OF_HOST2 = config2.getInt("numHosts");
        int NO_OF_VM2 = config2.getInt("numVm");
        long RAM_OF_HOST1_CONFIG2 = config1.getInt("HostType1.ram");
        long RAM_OF_HOST2_CONFIG2 = config1.getInt("HostType2.ram");
        long RAM_OF_VM_CONFIG2 = config1.getInt("Vm.ram");

        //Compare total ram of hosts on multiple datacenters with total ram of VMs requested for config file 1
        if((NO_OF_HOST2 * RAM_OF_HOST1_CONFIG2 + NO_OF_HOST2 * RAM_OF_HOST2_CONFIG2)> (NO_OF_VM2 * RAM_OF_VM_CONFIG2))
            assert(true);
        else
            assert(false);
    }

    //Test to check if badnwidth of Hosts assigned is greater than requested VMs
    @Test
    public void checkBadnwidthAllocation(){

        //For Config File 1
        int NO_OF_HOST1 = config1.getInt("numHosts");
        int NO_OF_VM1 = config1.getInt("numVm");
        long BW_OF_HOST2_CONFIG1 = config1.getInt("HostType2.ram");
        long BW_OF_VM_CONFIG1 = config1.getInt("Vm.ram");

        //Compare total badnwidth of hosts on datacenter with total rabadnwidthm of VMs requested for config file 1
        if((NO_OF_HOST1 * BW_OF_HOST2_CONFIG1)> (NO_OF_VM1 * BW_OF_VM_CONFIG1))
            assert(true);
        else
            assert(false);

        //For Config File 2
        int NO_OF_HOST2 = config2.getInt("numHosts");
        int NO_OF_VM2 = config2.getInt("numVm");
        long BW_OF_HOST1_CONFIG2 = config1.getInt("HostType1.ram");
        long BW_OF_HOST2_CONFIG2 = config1.getInt("HostType2.ram");
        long BW_OF_VM_CONFIG2 = config1.getInt("Vm.ram");

        //Compare total badnwidth of hosts on multiple datacenters with total badnwidth of VMs requested for config file 1
        if((NO_OF_HOST2 * BW_OF_HOST1_CONFIG2 + NO_OF_HOST2 * BW_OF_HOST2_CONFIG2)> (NO_OF_VM2 * BW_OF_VM_CONFIG2))
            assert(true);
        else
            assert(false);
    }

    //Test to check if storage of Hosts assigned is greater than requested VMs
    @Test
    public void checkStorageAllocation(){

        //For Config File 1
        int NO_OF_HOST1 = config1.getInt("numHosts");
        int NO_OF_VM1 = config1.getInt("numVm");
        long STORAGE_OF_HOST2_CONFIG1 = config1.getInt("HostType2.ram");
        long STORAGE_OF_VM_CONFIG1 = config1.getInt("Vm.ram");

        //Compare total storage of hosts on datacenter with total storage of VMs requested for config file 1
        if((NO_OF_HOST1 * STORAGE_OF_HOST2_CONFIG1)> (NO_OF_VM1 * STORAGE_OF_VM_CONFIG1))
            assert(true);
        else
            assert(false);

        //For Config File 2
        int NO_OF_HOST2 = config2.getInt("numHosts");
        int NO_OF_VM2 = config2.getInt("numVm");
        long STORAGE_OF_HOST1_CONFIG2 = config1.getInt("HostType1.ram");
        long STORAGE_OF_HOST2_CONFIG2 = config1.getInt("HostType2.ram");
        long STORAGE_OF_VM_CONFIG2 = config1.getInt("Vm.ram");

        //Compare total storage of hosts on multiple datacenters with total storage of VMs requested for config file 1
        if((NO_OF_HOST2 * STORAGE_OF_HOST1_CONFIG2 + NO_OF_HOST2 * STORAGE_OF_HOST2_CONFIG2)> (NO_OF_VM2 * STORAGE_OF_VM_CONFIG2))
            assert(true);
        else
            assert(false);
    }
}
