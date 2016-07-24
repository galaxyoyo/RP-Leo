package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.Disciplin;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.gui.Overview;
import fr.galaxyoyo.rp.server.Server;
import io.netty.buffer.ByteBuf;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.util.UUID;

public class PacketMixNewDisciplin extends Packet
{
	private Character character;
	private Disciplin disciplin;

	@Override
	public void read(ByteBuf buf) throws IOException
	{
		if (RP.isServer())
			character = Server.getServer().getCharacter(new UUID(buf.readLong(), buf.readLong()));
		else
			character = Overview.instance().getCharacter(new UUID(buf.readLong(), buf.readLong()));
		ObjectProperty p = RP.getGson().fromJson(readUTF(buf), ObjectProperty.class);
		disciplin = (Disciplin) p.get();

		if (disciplin.isPhysic())
			//noinspection unchecked
			character.addPhysicDisciplin(p);
		else
			//noinspection unchecked
			character.addMentalDisciplin(p);

		if (RP.isServer())
		{
			Server.getServer().save();
			Server.getServer().getConnectedPlayers().forEach(player ->
			{
				if (player != getPlayer())
					PacketManager.sendPacketToPlayer(player, this);
			});
		}
		else
		{
			if (disciplin.isPhysic())
				Overview.instance().getSheet(character).addPhysicDisciplin(disciplin);
			else
				Overview.instance().getSheet(character).addMentalDisciplin(disciplin);
		}
	}

	@Override
	public void write(ByteBuf buf)
	{
		buf.writeLong(character.getUuid().getMostSignificantBits()).writeLong(character.getUuid().getLeastSignificantBits());
		writeUTF(RP.getGson().toJson(new SimpleObjectProperty<>(disciplin), ObjectProperty.class), buf);
	}

	public void setCharacter(Character character)
	{
		this.character = character;
	}

	public void setDisciplin(Disciplin disciplin)
	{
		this.disciplin = disciplin;
	}
}
