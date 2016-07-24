package fr.galaxyoyo.rp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.galaxyoyo.rp.client.Client;
import fr.galaxyoyo.rp.server.Server;
import javafx.beans.property.ObjectProperty;

public class RP
{
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Character.class, new CharacterAdapter())
			.registerTypeAdapter(ObjectProperty.class, new ObjectPropertyAdapter()).create();
	private static boolean server = false;

	public static Gson getGson()
	{
		return gson;
	}

	public static void main(String... args)
	{
		String joined = "";
		for (String arg : args)
			joined += arg;

		if (joined.toLowerCase().contains("--server"))
			server = true;

		if (server)
			Server.setup();
		else
			Client.launch(Client.class, args);
	}

	public static boolean isServer()
	{
		return server;
	}
}
