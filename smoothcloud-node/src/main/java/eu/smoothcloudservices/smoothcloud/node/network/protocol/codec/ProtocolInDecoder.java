package eu.smoothcloudservices.smoothcloud.node.network.protocol.codec;

import eu.smoothcloudservices.smoothcloud.node.network.protocol.ProtocolBuffer;
import eu.smoothcloudservices.smoothcloud.node.network.protocol.ProtocolProvider;
import eu.smoothcloudservices.smoothcloud.node.network.protocol.ProtocolStream;
import eu.smoothcloudservices.smoothcloud.node.network.protocol.IProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtocolInDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        ProtocolBuffer protocolBuffer = ProtocolProvider.protocolBuffer(byteBuf);

        for (IProtocol iProtocol : ProtocolProvider.protocols()) {
            try {
                ProtocolStream protocolStream = iProtocol.createEmptyElement();
                protocolStream.read(protocolBuffer.clone());
                list.add(protocolStream);
                break;
            } catch (Exception ex) {

            }
        }
    }
}
