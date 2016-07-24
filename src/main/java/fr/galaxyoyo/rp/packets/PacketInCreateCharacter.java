package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.server.Server;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

public class PacketInCreateCharacter extends Packet
{
	@Override
	public void read(ByteBuf buf) throws IOException
	{
		Character c = new Character();
		c.setUuid(UUID.randomUUID());
		Server.getServer().addCharacter(c);

		PacketOutSendCharacter pkt = PacketManager.createPacket(PacketOutSendCharacter.class);
		pkt.setCharacter(c);
		PacketManager.sendPacketToAll(pkt);
	}

	@Override
	public void write(ByteBuf buf)
	{
	}
}
