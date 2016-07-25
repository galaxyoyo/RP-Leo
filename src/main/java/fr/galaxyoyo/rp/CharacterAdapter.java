package fr.galaxyoyo.rp;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import fr.galaxyoyo.rp.packets.PacketMixSendModification;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

public class CharacterAdapter extends TypeAdapter<Character>
{
	private static final ObjectPropertyAdapter disciplinAdapter = new ObjectPropertyAdapter();

	@Override
	public void write(JsonWriter out, Character value) throws IOException
	{
		out.beginObject();
		out.name("uuid").value(value.getUuid().toString());
		for (Field f : Character.class.getDeclaredFields())
		{
			try
			{
				f.setAccessible(true);
				Object o = f.get(value);
				if (o instanceof Property)
				{
					Property p = (Property) o;
					out.name(f.getName());
					Object v = p.getValue();
					if (v instanceof String)
						out.value((String) v);
					else if (v instanceof Integer)
						out.value((Integer) v);
				}
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		out.name("physicDisciplins");
		out.beginArray();
		for (ObjectProperty<Disciplin> p : value.getPhysicDisciplins())
			RP.getGson().toJson(p, ObjectProperty.class, out);
		out.endArray();

		out.name("mentalDisciplins");
		out.beginArray();
		for (ObjectProperty<Disciplin> p : value.getMentalDisciplins())
			RP.getGson().toJson(p, ObjectProperty.class, out);
		out.endArray();

		out.name("specialAbilities");
		out.beginArray();
		for (StringProperty p : value.getSpecialAbilities())
			out.value(p.get());
		out.endArray();

		out.name("specialEffects");
		out.beginArray();
		for (StringProperty p : value.getSpecialEffects())
			out.value(p.get());
		out.endArray();

		out.endObject();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Character read(JsonReader in) throws IOException
	{
		PacketMixSendModification.setModifying(true);

		Character c = new Character();

		in.beginObject();
		while (in.peek() != JsonToken.END_OBJECT)
		{
			try
			{
				Field f = Character.class.getDeclaredField(in.nextName());
				f.setAccessible(true);
				switch (f.getName())
				{
					case "uuid":
						c.setUuid(UUID.fromString(in.nextString()));
						break;
					case "physicDisciplins":
						in.beginArray();
						while (in.peek() != JsonToken.END_ARRAY)
							c.addPhysicDisciplin(disciplinAdapter.read(in));
						in.endArray();
						break;
					case "mentalDisciplins":
						in.beginArray();
						while (in.peek() != JsonToken.END_ARRAY)
							c.addMentalDisciplin(disciplinAdapter.read(in));
						in.endArray();
						break;
					case "specialAbilities":
						in.beginArray();
						while (in.peek() != JsonToken.END_ARRAY)
							c.addSpecialAbility(in.nextString());
						in.endArray();
						break;
					case "specialEffects":
						in.beginArray();
						while (in.peek() != JsonToken.END_ARRAY)
							c.addSpecialEffect(in.nextString());
						in.endArray();
						break;
					default:
						Property p = (Property) f.get(c);
						if (in.peek() == JsonToken.STRING)
							p.setValue(in.nextString());
						else if (in.peek() == JsonToken.NUMBER)
							p.setValue(in.nextInt());
						else
						{
							System.out.println(in.peek());
						}
						break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		in.endObject();

		PacketMixSendModification.setModifying(false);

		return c;
	}
}
