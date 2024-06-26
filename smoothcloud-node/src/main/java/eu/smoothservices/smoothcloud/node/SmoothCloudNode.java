package eu.smoothservices.smoothcloud.node;

import com.github.lalyos.jfiglet.FigletFont;
import eu.smoothservices.smoothcloud.api.SmoothCloudAPI;
import eu.smoothservices.smoothcloud.api.group.ICloudGroupProvider;
import eu.smoothservices.smoothcloud.api.player.ICloudPlayerProvider;
import eu.smoothservices.smoothcloud.api.service.ICloudServiceProvider;
import eu.smoothservices.smoothcloud.node.command.CommandProvider;
import eu.smoothservices.smoothcloud.node.config.MainConfig;
import eu.smoothservices.smoothcloud.node.group.ICloudGroupProviderImpl;
import eu.smoothservices.smoothcloud.node.player.CloudPlayerProviderImpl;
import eu.smoothservices.smoothcloud.node.server.NettyServer;
import eu.smoothservices.smoothcloud.node.service.CloudServiceProviderImpl;
import eu.smoothservices.smoothcloud.node.terminal.JavaColor;
import eu.smoothservices.smoothcloud.node.terminal.TerminalManager;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;

@Getter
public final class SmoothCloudNode extends SmoothCloudAPI {

    public static boolean isSettingUp = false;
    public static boolean isCreatingGroup = false;
    public static String path = "E:/Desktop/SCS - Testing";

    public static final String PREFIX = "&9Smooth&bCloud &8» &7";

    private final MainConfig config;
    private final TerminalManager terminal;
    private CommandProvider commandProvider;

    private ICloudGroupProvider groupProvider;
    private ICloudServiceProvider serviceProvider;
    private ICloudPlayerProvider playerProvider;

    private NettyServer nettyServer;

    @SneakyThrows
    public SmoothCloudNode() {
        File configFile = new File(path, "config.yml");

        this.config = new MainConfig(configFile);

        this.terminal = new TerminalManager();

        if(!isSettingUp/* && this.config.isLoaded()*/) {
            //this.config.load();
            startCloud();
        }
    }

    @SneakyThrows
    public void startCloud() {
        this.terminal.clearScreen();
        this.terminal.closeAppend(JavaColor.apply(STR."&b\{FigletFont.convertOneLine("SmoothCloud")}"));
        this.terminal.closeAppend("\n");

        this.terminal.closeAppend(PREFIX, "Starting CommandProvider...");
        this.commandProvider = new CommandProvider();
        this.terminal.closeAppend(PREFIX, "CommandProvider started.");

        this.terminal.closeAppend(PREFIX, "Starting CloudGroupProvider...");
        this.groupProvider = new ICloudGroupProviderImpl();
        this.terminal.closeAppend(PREFIX, "CloudGroupProvider started.");

        this.terminal.closeAppend(PREFIX, "Starting CloudServiceProvider...");
        this.serviceProvider = new CloudServiceProviderImpl();
        this.terminal.closeAppend(PREFIX, "CloudServiceProvider started.");

        this.terminal.closeAppend(PREFIX, "Starting CloudPlayerProvider...");
        this.playerProvider = new CloudPlayerProviderImpl();
        this.terminal.closeAppend(PREFIX, "CloudPlayerProvider started.");

        this.terminal.closeAppend(PREFIX, "Starting Connection for the wrapper...");
        //this.nettyServer = new NettyServer();
        this.terminal.closeAppend(PREFIX, "Connection for the wrapper started.");

        this.terminal.closeAppend(PREFIX, "Starting Internal Wrapper...");
        //this.wrapper = new SmoothCloudWrapper(config.getAddress().getHostName(), config.getAddress().getHostPort());
        this.terminal.closeAppend(PREFIX, "Internal Wrapper started.");

        this.terminal.closeAppend("\n");
        this.terminal.userAppend(PREFIX);

        Runtime.getRuntime().addShutdownHook(new Thread(SmoothCloudShutdownHandler::run));
    }
}
