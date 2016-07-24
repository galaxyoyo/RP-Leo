package fr.galaxyoyo.rp.client;

import fr.galaxyoyo.rp.packets.PacketDecoder;
import fr.galaxyoyo.rp.packets.PacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
	@Override
	protected void initChannel(SocketChannel channel) throws Exception
	{
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("encoder", new PacketEncoder());
		pipeline.addLast(new ProtobufVarint32FrameDecoder());
		pipeline.addLast("decoder", new PacketDecoder());
		pipeline.addLast("handler", new ClientChannelHandler());
	}
}
