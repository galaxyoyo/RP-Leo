package fr.galaxyoyo.rp.packets;

import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.Client;
import fr.galaxyoyo.rp.client.gui.Overview;
import fr.galaxyoyo.rp.client.gui.Sheet;
import io.netty.buffer.ByteBuf;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PacketOutSendCharacter extends Packet
{
	private Character character;

	@Override
	public void read(ByteBuf buf) throws IOException
	{
		character = RP.getGson().fromJson(readUTF(buf), Character.class);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/Sheet.fxml"));
		Parent parent = loader.load();
		Platform.runLater(() ->
		{
			VBox box = new VBox(parent);
			box.setAlignment(Pos.CENTER);
			VBox.setVgrow(parent, Priority.ALWAYS);
			StackPane pane = new StackPane(box);
			pane.prefWidthProperty().bind(Client.getStage().widthProperty());
			pane.prefHeightProperty().bind(Client.getStage().heightProperty());
			pane.setAlignment(Pos.CENTER);

			Sheet sheet = loader.getController();
			sheet.setCharacter(character);
			sheet.init();

			Tab tab = new Tab();
			tab.setContent(pane);
			tab.textProperty().bind(character.nameProperty());
			Overview.instance().addTab(character, sheet, tab);
		});
	}

	@Override
	public void write(ByteBuf buf)
	{
		writeUTF(RP.getGson().toJson(character), buf);
	}

	public void setCharacter(Character character)
	{
		this.character = character;
	}
}
