package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.Disciplin;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.gui.Overview;
import fr.galaxyoyo.rp.server.Server;
import io.netty.buffer.ByteBuf;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

public class PacketMixSendModification extends Packet
{
	private static boolean modifying = false;

	private Character character;
	private String id;
	private Object value;
	private UUID disciplinId;

	public static boolean isModifying()
	{
		return modifying;
	}

	public static void setModifying(boolean modifying)
	{
		PacketMixSendModification.modifying = modifying;
	}

	@Override
	public void read(ByteBuf buf) throws IOException
	{
		if (RP.isServer())
			character = Server.getServer().getCharacter(new UUID(buf.readLong(), buf.readLong()));
		else
			character = Overview.instance().getCharacter(new UUID(buf.readLong(), buf.readLong()));
		id = readUTF(buf);
		assert id != null;
		disciplinId = new UUID(buf.readLong(), buf.readLong());
		if (disciplinId.getMostSignificantBits() == 0L && disciplinId.getLeastSignificantBits() == 0L)
			disciplinId = null;
		Field f;
		try
		{
			Property prop;
			if (id.startsWith("specialAbility"))
				prop = character.getSpecialAbilities().get(Integer.parseInt(id.substring(14)));
			else if (id.startsWith("specialEffect"))
				prop = character.getSpecialEffects().get(Integer.parseInt(id.substring(13)));
			else
			{
				if (disciplinId != null)
					f = Disciplin.class.getDeclaredField(id);
				else
					f = Character.class.getDeclaredField(id);

				f.setAccessible(true);

				Object owner;
				if (disciplinId != null)
					owner = character.getDisciplin(disciplinId);
				else
					owner = character;
				prop = (Property) f.get(owner);
			}
			modifying = true;
			if (prop instanceof StringProperty)
			{
				value = readUTF(buf);
				if (RP.isServer())
					((StringProperty) prop).set((String) value);
				else
					Platform.runLater(() -> ((StringProperty) prop).set((String) value));
			}
			else if (prop instanceof IntegerProperty)
			{
				value = buf.readInt();
				if (RP.isServer())
					((IntegerProperty) prop).set((int) value);
				else
					Platform.runLater(() -> ((IntegerProperty) prop).set((int) value));
			}
			modifying = false;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (RP.isServer())
		{
			Server.getServer().save();
			Server.getServer().getConnectedPlayers().stream().filter(player -> player != getPlayer()).forEach(player -> PacketManager.sendPacketToPlayer(player, this));
		}
	}

	@Override
	public void write(ByteBuf buf)
	{
		buf.writeLong(character.getUuid().getMostSignificantBits());
		buf.writeLong(character.getUuid().getLeastSignificantBits());
		writeUTF(id, buf);
		if (disciplinId != null)
			buf.writeLong(disciplinId.getMostSignificantBits()).writeLong(disciplinId.getLeastSignificantBits());
		else
			buf.writeLong(0L).writeLong(0L);
		if (value instanceof String)
			writeUTF((String) value, buf);
		else if (value instanceof Integer)
			buf.writeInt((Integer) value);
		else
			System.err.println("type of '" + id + "' is '" + (value == null ? null : value.getClass().getSimpleName()) + "'");
	}

	public void setCharacter(Character character)
	{
		this.character = character;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	public void setDisciplinId(UUID disciplinId)
	{
		this.disciplinId = disciplinId;
	}
}
