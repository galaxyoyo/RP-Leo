package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.Client;
import fr.galaxyoyo.rp.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> obj) throws Exception
	{
		if (buf instanceof EmptyByteBuf)
			return;
		byte dis = buf.readByte();
		Packet pkt = PacketManager.createPacket(dis);
		if (RP.isServer())
			pkt.player = Server.getServer().getPlayer(ctx.channel());
		else
			pkt.player = Client.getClient().getPlayer();
		try
		{
			pkt.read(buf);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		obj.add(pkt);
		if (buf.isReadable())
			System.out.println("Warning: " + buf.readableBytes() + " bytes remains");
		buf.clear();
		ctx.flush();
	}
}
