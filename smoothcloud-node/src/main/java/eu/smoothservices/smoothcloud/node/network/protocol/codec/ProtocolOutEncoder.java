package eu.smoothservices.smoothcloud.node.network.protocol.codec;

import eu.smoothservices.smoothcloud.node.network.protocol.*;
import io.netty.handler.codec.MessageToByteEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public final class ProtocolOutEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        ProtocolBuffer protocolBuffer = ProtocolProvider.protocolBuffer(byteBuf);

        if (o instanceof ProtocolRequest) {
            ProtocolRequest protocolRequest = ((ProtocolRequest) o);
            IProtocol iProtocol = ProtocolProvider.getProtocol(protocolRequest.getId());
            ProtocolStream protocolStream = iProtocol.createElement(protocolRequest.getElement());
            protocolStream.write(protocolBuffer);
        } else {
            for (IProtocol iProtocol : ProtocolProvider.protocols()) {
                ProtocolStream protocolStream = iProtocol.createElement(o);
                if (protocolStream != null) {
                    protocolStream.write(protocolBuffer);
                    break;
                }
            }
        }
    }
}
