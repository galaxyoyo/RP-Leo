package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Player;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.Client;
import fr.galaxyoyo.rp.server.Server;
import io.netty.channel.ChannelFutureListener;

import java.util.HashMap;
import java.util.Map;

public class PacketManager
{
	private static final Map<Byte, Class<? extends Packet>> packets = new HashMap<>(64);
	private static final Map<Class<? extends Packet>, Byte> packetIds = new HashMap<>(64);

	static
	{
		registerPacket(0x00, PacketOutSendCharacter.class);
		registerPacket(0x01, PacketInCreateCharacter.class);
		registerPacket(0x02, PacketMixSendModification.class);
		registerPacket(0x03, PacketMixNewDisciplin.class);
		registerPacket(0x04, PacketMixNewSpecial.class);
	}

	private static void registerPacket(int discriminator, Class<? extends Packet> pktClazz)
	{
		if (packets.containsKey((byte) discriminator))
			throw new IllegalArgumentException("A packet with id '" + discriminator + "' already exists!'");
		if (packets.containsValue(pktClazz))
			throw new IllegalArgumentException("The packet '" + pktClazz.getSimpleName() + "' already exists!");
		if (discriminator >= 64)
			throw new IllegalArgumentException("Can't create packet with id >= 64!");
		if (discriminator < 0)
			throw new IllegalArgumentException("Can't create packet with id < 0!");
		if (pktClazz.isInterface())
			throw new IllegalArgumentException("Class '" + pktClazz.getSimpleName() + "' isn't defined!");
		packets.put((byte) discriminator, pktClazz);
		packetIds.put(pktClazz, (byte) discriminator);
	}

	protected static byte getPacketId(Packet pkt) { return packetIds.get(pkt.getClass()); }

	public static Packet createPacket(int packetId) { return createPacket(packets.get((byte) packetId)); }

	public static <T extends Packet> T createPacket(Class<T> pktClazz)
	{
		try
		{
			return pktClazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public static void sendPacketToServer(Packet pkt)
	{
		if (RP.isServer())
			throw new UnsupportedOperationException("Can't send a packet server->server !");
		if (Client.getClient().getPlayer().getChannel().eventLoop().inEventLoop())
			Client.getClient().getPlayer().getChannel().writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		else
			Client.getClient().getPlayer().getChannel().eventLoop().execute(
					() -> Client.getClient().getPlayer().getChannel().writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE));
	}

	public static void sendPacketToAll(Packet pkt)
	{
		for (Player player : Server.getServer().getConnectedPlayers())
			sendPacketToPlayer(player, pkt);
	}

	public static void sendPacketToPlayer(Player player, Packet pkt)
	{
		if (!RP.isServer())
			throw new UnsupportedOperationException("Can't send a packet player->player !");
		pkt.player = player;
		if (player.getChannel().eventLoop().inEventLoop())
			player.getChannel().writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		else
			player.getChannel().eventLoop().execute(() -> player.getChannel().writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE));
	}
}
