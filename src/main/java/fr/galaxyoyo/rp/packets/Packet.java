package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Player;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class Packet
{
	protected Player player;

	public abstract void read(ByteBuf buf) throws IOException;

	public abstract void write(ByteBuf buf);

	public Player getPlayer()
	{
		return player;
	}

	public final String readUTF(ByteBuf buf)
	{
		short length = buf.readShort();
		if (length < 0)
			return null;
		byte[] array = new byte[length];
		buf.readBytes(array);
		return new String(array, StandardCharsets.UTF_8);
	}

	public final void writeUTF(String utf, ByteBuf buf)
	{
		if (utf == null)
		{
			buf.writeShort(-1);
			return;
		}
		byte[] array = utf.getBytes(StandardCharsets.UTF_8);
		if (array.length > Short.MAX_VALUE)
			throw new IllegalArgumentException("length must be <= " + Short.MAX_VALUE);
		buf.writeShort(array.length);
		buf.writeBytes(array);
	}
}
