import Utils.DatacenterUtils;
import org.cloudbus.cloudsim.core.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Booter {
    //Class to load and run simulations from Gradle command line
    //User enter the simulation number at command line.
    public static void main(String[] args){

        Logger logger = LoggerFactory.getLogger(Booter .class);

        try {
            //Cases to run simulation corresponding to the number entered by user at command line
            switch (Integer.parseInt(args[0])) {
                case 0:
                    Simulation0 sim1 = new Simulation0();
                    break;
                case 1:
                    Simulation1 sim2 = new Simulation1();
                    break;
                case 2:
                    Simulation2 sim3 = new Simulation2();
                    break;
                case 3:
                    Simulation3 sim4 = new Simulation3();
                    break;
                case 4:
                    Simulation4 sim5 = new Simulation4();
                    break;
                case 5:
                    Simulation5 sim6 = new Simulation5();
                    break;
                case 6:
                    Simulation6 sim7 = new Simulation6();
                    break;
                default:
                    logger.error("Please enter a valid Simulation Number(0-6).");
            }
        }
        catch(Exception e){
            logger.error("Please enter a valid Simulation Number(0-6) as argument. Enter the command in the format gradle run --args '<Simulation No>'");
        }
    }
}
