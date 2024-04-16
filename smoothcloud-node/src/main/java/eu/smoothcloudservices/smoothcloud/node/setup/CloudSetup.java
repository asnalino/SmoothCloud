package eu.smoothcloudservices.smoothcloud.node.setup;

import eu.smoothcloudservices.smoothcloud.node.SmoothSmoothCloudNode;
import eu.smoothcloudservices.smoothcloud.node.config.CloudConfig;
import com.github.lalyos.jfiglet.FigletFont;
import eu.smoothcloudservices.smoothcloud.node.terminal.Color;
import eu.smoothcloudservices.smoothcloud.node.terminal.JLine3Terminal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class CloudSetup {
    private final JLine3Terminal terminal;

    public CloudSetup() {
        this.terminal = ((SmoothSmoothCloudNode) SmoothSmoothCloudNode.getInstance()).getTerminal();
    }


    public boolean setup(CloudConfig config) {

        Scanner scanner = new Scanner(System.in);

        boolean eulaAccepted = false;


        try {
            System.out.println(FigletFont.convertOneLine("SmoothCloud  Setup"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String eulaURL = "https://www.minecraft.net/en-us/eula";
        presentEula(eulaURL);
        eulaAccepted = getEulaAgreement(scanner, config);
        if (!eulaAccepted) {
            terminal.write(Color.translate("&0CloudSystem &2» &3EULA not accepted. Aborting setup!"));
            return false;
        }

        if (!chooseNodeIP(scanner, config)) {
            return false;
        }
        chooseNodePort(scanner, config);

        if (!chooseWrapperIP(scanner, config)) {
            return false;
        }
        chooseWrapperPort(scanner, config);

        return true;
    }


    private void presentEula(String eulaUrl) {
        terminal.write(Color.translate("&0CloudSystem &2» &0Do you agree to the Mojang EULA (https://www.minecraft.net/en-us/eula)? Possible answers: yes, no"));
    }

    private boolean getEulaAgreement(Scanner scanner, CloudConfig config) {
        while (true) {
            String answer = scanner.nextLine().toLowerCase();
            if(answer.equals("yes") || answer.equals("y")) {
                config.set("cloud.eula", true);
                return true;
            }
            if(answer.equals("no") || answer.equals("n")) {
                return false;
            }
            terminal.write(Color.translate("&0CloudSystem &2» &3Please answer with yes or no!"));
        }
    }

    private void chooseNodePort(Scanner scanner, CloudConfig config) {
        while (true) {
            terminal.write(Color.translate("&0CloudSystem &2» &0Which port should we use for the node?"));
            int answer = Integer.parseInt(scanner.nextLine().toLowerCase());
            boolean portAvailable = checkPortAvailability(answer);
            if (portAvailable) {
                config.set("cloud.nodeport", scanner.nextLine());
                return;
            }
            terminal.write(Color.translate("&0CloudSystem &2» &3Port not available. Please choose an other Port!"));
        }
    }

    private void chooseWrapperPort(Scanner scanner, CloudConfig config) {
        while (true) {
            terminal.write(Color.translate("&0CloudSystem &2» &0Which port should we use for the wrapper?"));
            int answer = Integer.parseInt(scanner.nextLine().toLowerCase());
            boolean portAvailable = checkPortAvailability(answer);
            if (portAvailable) {
                config.set("cloud.wrapperport", scanner.nextLine());
                return;
            }
            terminal.write(Color.translate("&0CloudSystem &2» &3Port not available. Please choose an other Port!"));
        }
    }

    private boolean chooseNodeIP(Scanner scanner, CloudConfig config) {
        while (true) {
            List<InetAddress> inetAddresses = getAllIPAddresses();
            if (inetAddresses.isEmpty()) {
                terminal.write(Color.translate("&0CloudSystem &2» &3No IP addresses available. Aborting setup!"));
                return false;
            }

            String allIps;
            allIps = null;
            for (InetAddress inetAddress : Collections.unmodifiableList(inetAddresses)) {
                if (allIps == null) {
                    allIps = String.valueOf(inetAddress);
                }
                if (!(allIps == null)) {
                    allIps = STR."\{allIps}, \{inetAddress}";
                }
            }

            terminal.write(Color.translate("&0CloudSystem &2» &0Which IP should we use for the node?"));
            terminal.write(Color.translate(STR."&0CloudSystem &2» &0Available IPs: \{allIps}"));

            if (inetAddresses.contains(scanner.nextLine())) {
                config.set("cloud.nodeip", scanner.nextLine());
                return true;
            }
            terminal.write(Color.translate("&0CloudSystem &2» &3Please choose an IP address from above!"));
        }
    }

    private boolean chooseWrapperIP(Scanner scanner, CloudConfig config) {
        while (true) {
            List<InetAddress> inetAddresses = getAllIPAddresses();
            if (inetAddresses.isEmpty()) {
                terminal.write(Color.translate("&0CloudSystem &2» &3No IP addresses available. Aborting setup!"));
                return false;
            }

            String allIps;
            allIps = null;
            for (InetAddress inetAddress : Collections.unmodifiableList(inetAddresses)) {
                if (allIps == null) {
                    allIps = String.valueOf(inetAddress);
                }
                if (!(allIps == null)) {
                    allIps = STR."\{allIps}, \{inetAddress}";
                }
            }

            terminal.write(Color.translate("&0CloudSystem &2» &0Which IP should we use for the wrapper?"));
            terminal.write(Color.translate(STR."&0CloudSystem &2» &0Available IPs: \{allIps}"));

            if (inetAddresses.contains(scanner.nextLine())) {
                config.set("cloud.wrapperip", scanner.nextLine());
                return true;
            }
            terminal.write(Color.translate("&0CloudSystem &2» &3Please choose an IP address from above!"));
        }
    }

    private boolean checkPortAvailability(int port) {
        Socket s = null;
        try {
            s = new Socket("localhost", port);

            // Port is not available
            return false;
        } catch (IOException e) {
            // Port is available
            return true;
        } finally {
            if( s != null){
                try {
                    s.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
    }

    private List<InetAddress> getAllIPAddresses () {
        List<InetAddress> addrList = new ArrayList<>();
        try {
            for(Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces(); eni.hasMoreElements(); ) {
                final NetworkInterface ifc = eni.nextElement();
                if(ifc.isUp()) {
                    for(Enumeration<InetAddress> ena = ifc.getInetAddresses(); ena.hasMoreElements(); ) {
                        addrList.add(ena.nextElement());
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return addrList;
    }
}