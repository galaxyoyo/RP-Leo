package fr.galaxyoyo.rp;

import fr.galaxyoyo.rp.client.gui.Overview;
import fr.galaxyoyo.rp.packets.PacketManager;
import fr.galaxyoyo.rp.packets.PacketMixSendModification;
import fr.galaxyoyo.rp.server.Server;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

import java.lang.reflect.Field;
import java.util.UUID;

public class Disciplin
{
	private final StringProperty name = new SimpleStringProperty("");
	private final StringProperty bonus1Name = new SimpleStringProperty("--------------");
	private final StringProperty bonus1Value = new SimpleStringProperty("----");
	private final StringProperty bonus2Name = new SimpleStringProperty("--------------");
	private final StringProperty bonus2Value = new SimpleStringProperty("----");
	private final BooleanProperty physic = new SimpleBooleanProperty(true);
	private UUID owner;
	private UUID uuid;

	public Disciplin()
	{
		if (RP.isServer())
			return;

		for (Field field : Disciplin.class.getDeclaredFields())
		{
			try
			{
				field.setAccessible(true);
				Object o = field.get(this);
				if (o instanceof Property)
				{
					Property<?> p = (Property) o;
					p.addListener((observable, oldValue, newValue) ->
					{
						if (PacketMixSendModification.isModifying())
							return;
						PacketMixSendModification pkt = PacketManager.createPacket(PacketMixSendModification.class);
						if (RP.isServer())
							pkt.setCharacter(Server.getServer().getCharacter(owner));
						else
							pkt.setCharacter(Overview.instance().getCharacter(getOwner()));
						pkt.setId(field.getName());
						pkt.setValue(newValue);
						pkt.setDisciplinId(getUuid());
						PacketManager.sendPacketToServer(pkt);
					});
				}
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	public UUID getOwner()
	{
		return owner;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public boolean isMental()
	{
		return !isPhysic();
	}

	public boolean isPhysic()
	{
		return physic.get();
	}

	public void setPhysic()
	{
		this.physic.set(true);
	}

	public void setMental()
	{
		this.physic.set(false);
	}

	public BooleanBinding mentalBinding()
	{
		return physicProperty().not();
	}

	public BooleanProperty physicProperty()
	{
		return physic;
	}

	public String getName()
	{
		return name.get();
	}

	public void setName(String name)
	{
		this.name.set(name);
	}

	public StringProperty nameProperty()
	{
		return name;
	}

	public String getBonus1Name()
	{
		return bonus1Name.get();
	}

	public void setBonus1Name(String name)
	{
		this.bonus1Name.set(name);
	}

	public StringProperty bonus1NameProperty()
	{
		return bonus1Name;
	}

	public String getBonus1Value()
	{
		return bonus1Value.get();
	}

	public void setBonus1Value(String value)
	{
		this.bonus1Value.set(value);
	}

	public StringProperty bonus1ValueProperty()
	{
		return bonus1Value;
	}

	public String getBonus2Name()
	{
		return bonus2Name.get();
	}

	public void setBonus2Name(String name)
	{
		this.bonus2Name.set(name);
	}

	public StringProperty bonus2NameProperty()
	{
		return bonus2Name;
	}

	public String getBonus2Value()
	{
		return bonus2Value.get();
	}

	public void setBonus2Value(String value)
	{
		this.bonus2Value.set(value);
	}

	public StringProperty bonus2ValueProperty()
	{
		return bonus2Value;
	}
}
