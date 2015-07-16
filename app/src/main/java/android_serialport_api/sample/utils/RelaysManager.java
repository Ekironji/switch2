package android_serialport_api.sample.utils;

/**
 * Created by Luigi on 14/07/2015.
 */
public class RelaysManager {

    /* execute a command
            Process process = Runtime.getRuntime().exec(commandLine);
     */

    final public static String RELAY1 = "1";
    final public static String RELAY2 = "2";
    final public static String RELAY3 = "3";

    final public static String ON  = "on";
    final public static String OFF = "off";

    public RelaysManager(){
        /*try {
            Process process = Runtime.getRuntime().exec("echo out > /sys/class/gpio7/direction");
            Process process1 = Runtime.getRuntime().exec("echo out > /sys/class/gpio85/direction");
            Process process2 = Runtime.getRuntime().exec("echo out > /sys/class/gpio41/direction");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void switchRelay(String relay, String status) {
//        String commandLine = "echo " + status + " > /sys/class/gpio/gpio" + relay + "/value";
        String commandLine = "/system/bin/switch" + relay + status;
        try {
            Process process = Runtime.getRuntime().exec(commandLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
