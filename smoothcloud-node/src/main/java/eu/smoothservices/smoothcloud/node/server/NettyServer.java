package eu.smoothservices.smoothcloud.node.server;

import eu.smoothservices.smoothcloud.api.misc.NettyUtils;
import eu.smoothservices.smoothcloud.node.SmoothCloudNode;
import eu.smoothservices.smoothcloud.node.config.MainConfig;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.Future;

public final class NettyServer {
    private final EventLoopGroup bossGroup = new MultithreadEventLoopGroup(NettyUtils.getFactory());
    private final EventLoopGroup workGroup = new MultithreadEventLoopGroup(NettyUtils.getFactory());

    private Future<Void> future;

    public NettyServer() {

        MainConfig config = ((SmoothCloudNode) SmoothCloudNode.getInstance()).getConfig();

        /*new ServerBootstrap()
                .channelFactory(NettyUtils.getChannelFactory())
                .group(bossGroup, workGroup)
                .childHandler(new NettyNetworkServerInitializer())
                .bind(config.getAddress().getHostName(), Integer.parseInt(config.getAddress().getHostPort()))
                .addListener(futures -> {
                });
        ((SmoothCloudNode) SmoothCloudNode.getInstance()).getTerminal().closeAppend(SmoothCloudNode.PREFIX, STR."Netty Connection successfully started on HostAddress: \{config.getAddress().getHostAddress()}");
    */
    }


    public void close() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
