package fr.galaxyoyo.rp;

import fr.galaxyoyo.rp.packets.PacketManager;
import fr.galaxyoyo.rp.packets.PacketMixSendModification;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.util.UUID;

public class Character
{
	private final StringProperty name = new SimpleStringProperty("");
	private final StringProperty desc = new SimpleStringProperty("");
	private final IntegerProperty power = new SimpleIntegerProperty(0);
	private final IntegerProperty toughness = new SimpleIntegerProperty(0);
	private final IntegerProperty speed = new SimpleIntegerProperty(0);
	private final IntegerProperty prec = new SimpleIntegerProperty(0);
	private final IntegerProperty resistance = new SimpleIntegerProperty(0);
	private final IntegerProperty charism = new SimpleIntegerProperty(0);
	private final IntegerProperty perception = new SimpleIntegerProperty(0);
	private final IntegerProperty physicPower = new SimpleIntegerProperty(0);
	private final IntegerProperty psychicPower = new SimpleIntegerProperty(0);
	private final IntegerProperty elementaryPower = new SimpleIntegerProperty(0);
	private final ObservableList<ObjectProperty<Disciplin>> physicDisciplins = FXCollections.observableArrayList();
	private final ObservableList<ObjectProperty<Disciplin>> mentalDisciplins = FXCollections.observableArrayList();
	private final IntegerProperty courage = new SimpleIntegerProperty(5);
	private final IntegerProperty lachete = new SimpleIntegerProperty(5);
	private final IntegerProperty style = new SimpleIntegerProperty(4);
	private final IntegerProperty sauvagerie = new SimpleIntegerProperty(4);
	private final IntegerProperty synergie = new SimpleIntegerProperty(0);
	private final IntegerProperty alignment = new SimpleIntegerProperty(0);
	private final IntegerProperty luck = new SimpleIntegerProperty(0);
	private final ObservableList<StringProperty> specialAbilities = FXCollections.observableArrayList();
	private final ObservableList<StringProperty> specialEffects = FXCollections.observableArrayList();
	private final IntegerProperty encombrement = new SimpleIntegerProperty(0);
	private UUID uuid;

	public Character()
	{
		if (RP.isServer())
			return;

		for (Field field : getClass().getDeclaredFields())
		{
			try
			{
				Object o = field.get(this);
				if (o == null)
					return;
				if (o instanceof Property)
				{
					Property<?> p = (Property) o;
					p.addListener((observable, oldValue, newValue) ->
					{
						if (PacketMixSendModification.isModifying())
							return;
						PacketMixSendModification pkt = PacketManager.createPacket(PacketMixSendModification.class);
						pkt.setCharacter(this);
						pkt.setId(field.getName());
						pkt.setValue(newValue);
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

	public int getEncombrement()
	{
		return encombrement.get();
	}

	public void setEncombrement(int encombrement)
	{
		this.encombrement.set(encombrement);
	}

	public IntegerProperty encombrementProperty()
	{
		return encombrement;
	}

	public int getCourage()
	{
		return courage.get();
	}

	public void setCourage(int courage)
	{
		this.courage.set(courage);
	}

	public IntegerProperty courageProperty()
	{
		return courage;
	}

	public int getLachete()
	{
		return lachete.get();
	}

	public void setLachete(int lachete)
	{
		this.lachete.set(lachete);
	}

	public IntegerProperty lacheteProperty()
	{
		return lachete;
	}

	public int getStyle()
	{
		return style.get();
	}

	public void setStyle(int style)
	{
		this.style.set(style);
	}

	public IntegerProperty styleProperty()
	{
		return style;
	}

	public int getSauvagerie()
	{
		return sauvagerie.get();
	}

	public void setSauvagerie(int sauvagerie)
	{
		this.sauvagerie.set(sauvagerie);
	}

	public IntegerProperty sauvagerieProperty()
	{
		return sauvagerie;
	}

	public int getSynergie()
	{
		return synergie.get();
	}

	public void setSynergie(int synergie)
	{
		this.synergie.set(synergie);
	}

	public IntegerProperty synergieProperty()
	{
		return synergie;
	}

	public int getAlignment()
	{
		return alignment.get();
	}

	public void setAlignment(int alignment)
	{
		this.alignment.set(alignment);
	}

	public IntegerProperty alignmentProperty()
	{
		return alignment;
	}

	public int getLuck()
	{
		return luck.get();
	}

	public void setLuck(int luck)
	{
		this.luck.set(luck);
	}

	public IntegerProperty luckProperty()
	{
		return luck;
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

	public String getDesc()
	{
		return desc.get();
	}

	public void setDesc(String desc)
	{
		this.desc.set(desc);
	}

	public StringProperty descProperty()
	{
		return desc;
	}

	public int getPower()
	{
		return power.get();
	}

	public void setPower(int power)
	{
		this.power.set(power);
	}

	public IntegerProperty powerProperty()
	{
		return power;
	}

	public int getToughness()
	{
		return toughness.get();
	}

	public void setToughness(int toughness)
	{
		this.toughness.set(toughness);
	}

	public IntegerProperty toughnessProperty()
	{
		return toughness;
	}

	public int getSpeed()
	{
		return speed.get();
	}

	public void setSpeed(int speed)
	{
		this.speed.set(speed);
	}

	public IntegerProperty speedProperty()
	{
		return speed;
	}

	public int getPrec()
	{
		return prec.get();
	}

	public void setPrec(int prec)
	{
		this.prec.set(prec);
	}

	public IntegerProperty precProperty()
	{
		return prec;
	}

	public int getResistance()
	{
		return resistance.get();
	}

	public void setResistance(int resistance)
	{
		this.resistance.set(resistance);
	}

	public IntegerProperty resistanceProperty()
	{
		return resistance;
	}

	public int getCharism()
	{
		return charism.get();
	}

	public void setCharism(int charism)
	{
		this.charism.set(charism);
	}

	public IntegerProperty charismProperty()
	{
		return charism;
	}

	public int getPerception()
	{
		return perception.get();
	}

	public void setPerception(int perception)
	{
		this.perception.set(perception);
	}

	public IntegerProperty perceptionProperty()
	{
		return perception;
	}

	public int getPhysicPower()
	{
		return physicPower.get();
	}

	public void setPhysicPower(int physicPower)
	{
		this.physicPower.set(physicPower);
	}

	public IntegerProperty physicPowerProperty()
	{
		return physicPower;
	}

	public int getPsychicPower()
	{
		return psychicPower.get();
	}

	public void setPsychicPower(int psychicPower)
	{
		this.psychicPower.set(psychicPower);
	}

	public IntegerProperty psychicPowerProperty()
	{
		return psychicPower;
	}

	public int getElementaryPower()
	{
		return elementaryPower.get();
	}

	public void setElementaryPower(int elementaryPower)
	{
		this.elementaryPower.set(elementaryPower);
	}

	public IntegerProperty elementaryPowerProperty()
	{
		return elementaryPower;
	}

	public Disciplin getPhysicDisciplin(int index)
	{
		return physicDisciplins.get(index).get();
	}

	public ObjectProperty<Disciplin> physicDisciplinProperty(int index)
	{
		return physicDisciplins.get(index);
	}

	public void setPhysicDisciplin(int index, Disciplin disciplin)
	{
		this.physicDisciplins.get(index).set(disciplin);
	}

	public Disciplin getMentalDisciplin(int index)
	{
		return mentalDisciplins.get(index).get();
	}

	public ObjectProperty<Disciplin> mentalDisciplinProperty(int index)
	{
		return mentalDisciplins.get(index);
	}

	public void setMentalDisciplin(int index, Disciplin disciplin)
	{
		this.mentalDisciplins.get(index).set(disciplin);
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public int addPhysicDisciplin()
	{
		Disciplin d = new Disciplin();
		d.setOwner(uuid);
		physicDisciplins.add(new SimpleObjectProperty<>(d));
		return physicDisciplins.size() - 1;
	}

	public int addMentalDisciplin()
	{
		Disciplin d = new Disciplin();
		d.setOwner(uuid);
		mentalDisciplins.add(new SimpleObjectProperty<>(d));
		return mentalDisciplins.size() - 1;
	}

	public ObservableList<ObjectProperty<Disciplin>> getPhysicDisciplins()
	{
		return physicDisciplins;
	}

	public ObservableList<ObjectProperty<Disciplin>> getMentalDisciplins()
	{
		return mentalDisciplins;
	}

	public void addPhysicDisciplin(ObjectProperty<Disciplin> property)
	{
		physicDisciplins.add(property);
	}

	public void addMentalDisciplin(ObjectProperty<Disciplin> property)
	{
		mentalDisciplins.add(property);
	}

	public Disciplin getDisciplin(UUID disciplinId)
	{
		return physicDisciplins.stream().filter(disciplinObjectProperty -> disciplinObjectProperty.get().getUuid().equals(disciplinId)).findAny()
				.orElse(mentalDisciplins.stream().filter(disciplinObjectProperty -> disciplinObjectProperty.get().getUuid().equals(disciplinId)).findAny().orElse(null)).get();
	}

	public int addSpecialAbility()
	{
		StringProperty p = new SimpleStringProperty("");
		specialAbilities.add(p);
		return specialAbilities.size() - 1;
	}

	public int addSpecialEffect()
	{
		StringProperty p = new SimpleStringProperty("");
		specialEffects.add(p);
		return specialEffects.size() - 1;
	}

	public ObservableList<StringProperty> getSpecialAbilities()
	{
		return specialAbilities;
	}

	public ObservableList<StringProperty> getSpecialEffects()
	{
		return specialEffects;
	}

	public void addSpecialAbility(String ability)
	{
		StringProperty p = new SimpleStringProperty(ability);
		specialAbilities.add(p);
	}

	public void addSpecialEffect(String effect)
	{
		StringProperty p = new SimpleStringProperty(effect);
		specialEffects.add(p);
	}
}
