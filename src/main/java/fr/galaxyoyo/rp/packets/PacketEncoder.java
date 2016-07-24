package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.Client;
import fr.galaxyoyo.rp.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet pkt, ByteBuf buf) throws Exception
	{
		if (RP.isServer())
			pkt.player = Server.getServer().getPlayer(ctx.channel());
		else
			pkt.player = Client.getClient().getPlayer();
		buf.writeByte(PacketManager.getPacketId(pkt));
		pkt.write(buf);
	}
}
