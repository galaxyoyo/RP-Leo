package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.gui.Overview;
import fr.galaxyoyo.rp.server.Server;
import io.netty.buffer.ByteBuf;
import javafx.application.Platform;

import java.io.IOException;
import java.util.UUID;

public class PacketMixNewSpecial extends Packet
{
	private Character character;
	private boolean ability;

	@Override
	public void read(ByteBuf buf) throws IOException
	{
		if (RP.isServer())
			character = Server.getServer().getCharacter(new UUID(buf.readLong(), buf.readLong()));
		else
			character = Overview.instance().getCharacter(new UUID(buf.readLong(), buf.readLong()));

		ability = buf.readBoolean();

		int index;

		if (ability)
			index = character.addSpecialAbility();
		else
			index = character.addSpecialEffect();

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
			Platform.runLater(() ->
			{
				if (ability)
					Overview.instance().getSheet(character).addSpecialAbility(character.getSpecialAbilities().get(index));
				else
					Overview.instance().getSheet(character).addSpecialEffect(character.getSpecialEffects().get(index));
			});
		}
	}

	@Override
	public void write(ByteBuf buf)
	{
		buf.writeLong(character.getUuid().getMostSignificantBits()).writeLong(character.getUuid().getLeastSignificantBits());
		buf.writeBoolean(ability);
	}

	public void setAbility()
	{
		this.ability = true;
	}

	public void setEffect()
	{
		this.ability = false;
	}

	public void setCharacter(Character character)
	{
		this.character = character;
	}
}
