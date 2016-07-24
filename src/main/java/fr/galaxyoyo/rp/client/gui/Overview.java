package fr.galaxyoyo.rp.client.gui;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.packets.PacketInCreateCharacter;
import fr.galaxyoyo.rp.packets.PacketManager;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

public class Overview extends AbstractController
{
	private static Overview instance;
	private ChangeListener<Number> listener;
	private Map<Character, Sheet> sheets = new HashMap<>();

	@FXML
	private TabPane pane;

	public static Overview instance()
	{
		while (instance == null)
		{
			try
			{
				Thread.sleep(100L);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		return instance;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		instance = this;
		listener = (observable, oldValue, newValue) ->
		{
			if (pane.getTabs().size() != 1 && newValue.intValue() == pane.getTabs().size() - 1)
				create();
		};
		pane.getSelectionModel().selectedIndexProperty().addListener(listener);
	}

	public void create()
	{
		PacketManager.sendPacketToServer(PacketManager.createPacket(PacketInCreateCharacter.class));
	}

	public void addTab(Character character, Sheet sheet, Tab tab)
	{
		sheets.put(character, sheet);
		pane.getSelectionModel().selectedIndexProperty().removeListener(listener);
		pane.getTabs().add(pane.getTabs().size() - 1, tab);
		pane.getSelectionModel().select(tab);
		pane.getSelectionModel().selectedIndexProperty().addListener(listener);
	}

	public Sheet getSheet(Character character)
	{
		return sheets.get(character);
	}

	public Character getCharacter(UUID uuid)
	{
		return sheets.keySet().stream().filter(c -> c.getUuid().equals(uuid)).findAny().orElse(null);
	}
}
