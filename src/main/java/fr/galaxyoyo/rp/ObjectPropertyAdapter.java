package fr.galaxyoyo.rp;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.util.UUID;

public class ObjectPropertyAdapter extends TypeAdapter<ObjectProperty<Disciplin>>
{
	@Override
	public void write(JsonWriter out, ObjectProperty<Disciplin> value) throws IOException
	{
		Disciplin d = value.get();
		out.beginObject();
		out.name("name").value(d.getName());
		out.name(d.getBonus1Name()).value(d.getBonus1Value());
		out.name(d.getBonus2Name()).value(d.getBonus2Value());
		out.name("owner").value(d.getOwner().toString());
		out.name("uuid").value(d.getUuid().toString());
		out.name("physic").value(d.isPhysic());
		out.endObject();
	}

	@Override
	public ObjectProperty<Disciplin> read(JsonReader in) throws IOException
	{
		Disciplin d = new Disciplin();
		in.beginObject();
		in.nextName();
		d.setName(in.nextString());
		d.setBonus1Name(in.nextName());
		d.setBonus1Value(in.nextString());
		d.setBonus2Name(in.nextName());
		d.setBonus2Value(in.nextString());
		in.nextName();
		d.setOwner(UUID.fromString(in.nextString()));
		in.nextName();
		d.setUuid(UUID.fromString(in.nextString()));
		in.nextName();
		if (in.nextBoolean())
			d.setPhysic();
		else
			d.setMental();
		in.endObject();

		return new SimpleObjectProperty<>(d);
	}
}
