package fr.galaxyoyo.rp.client;

import fr.galaxyoyo.rp.Player;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.client.gui.AbstractController;
import fr.galaxyoyo.rp.client.gui.Overview;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Client extends Application
{
	private static Client instance;
	private static Stage stage;
	private static AbstractController currentController;
	private Player player;

	public static Client getClient()
	{
		return instance;
	}

	public static StackPane getStackPane()
	{
		return (StackPane) getStage().getScene().getRoot();
	}

	public static Stage getStage()
	{
		return stage;
	}

	public static <T extends AbstractController> T getCurrentController()
	{
		//noinspection unchecked
		return (T) currentController;
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		instance = this;

		try
		{
			EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap boot = new Bootstrap().group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.SO_RCVBUF, 0x42666).option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(0x42666))
					.option(ChannelOption.SO_REUSEADDR, false).handler(new ClientChannelInitializer());
			ChannelFuture f = boot.connect(RP.isDebug() ? "localhost" : "rp.arathia.fr", 42999);
			f.get(1, TimeUnit.SECONDS);
			f.awaitUninterruptibly();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Platform.runLater(() ->
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Problème de connexion");
				alert.setHeaderText("Connexion au serveur impossible");
				alert.setContentText("Il semblerait qu'un problème de connexion ait lieu avec le serveur. Veuillez réessayer.\nSi le problème persiste, vérifiez votre connection et" +
						" le twitter, et prévenez galaxyoyo en cas de problème");
				alert.showAndWait().ifPresent(buttonType -> System.exit(-1));
			});
		}

		try
		{
			stage = primaryStage;
			stage.setTitle("RP");
			//	stage.getIcons().add(new Image(getClass().getResource("/icons/icon.png").toString()));
			stage.setMaximized(true);
			stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			stage.setOnCloseRequest(event ->
			{
				event.consume();
				//noinspection ConstantConditions
				alert("Êtes-vous sûr ?", "Vous vous apprêtez à quitter le jeu", "Êtes-vous sûr de vraiment vouloir quitter ?", Alert.AlertType.CONFIRMATION)
						.filter(buttonType -> buttonType == ButtonType.OK).ifPresent(buttonType -> close());
			});
			show(Overview.class);
			stage.getScene().setOnKeyReleased(event ->
			{
				if (event.getCode() == KeyCode.F11)
					stage.setFullScreen(!stage.isFullScreen());
				else
					currentController.onKeyReleased(event);
			});
			stage.show();
			stage.toFront();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static Optional<ButtonType> alert(String title, String header, String content, Alert.AlertType type)
	{
		if (!Platform.isFxApplicationThread())
		{
			Platform.runLater(() ->
			{
				Alert alert = new Alert(type);
				alert.setTitle(title);
				alert.setHeaderText(header);
				alert.setContentText(content);
				alert.showAndWait();
			});
			return Optional.empty();
		}
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert.showAndWait();

	}

	public void close()
	{
		stage.close();
		if (player != null)
			player.getChannel().close();
		System.exit(0);
	}

	public static <T extends AbstractController> T show(Class<T> clazz)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(clazz.getResource("/views/" + clazz.getSimpleName() + ".fxml"));
			Parent parent = loader.load();
			VBox box = new VBox(parent);
			box.setAlignment(Pos.CENTER);
			VBox.setVgrow(parent, Priority.ALWAYS);
			StackPane pane = new StackPane(box);
			pane.prefWidthProperty().bind(getStage().widthProperty());
			pane.prefHeightProperty().bind(getStage().heightProperty());
			pane.setAlignment(Pos.CENTER);
			if (stage.getScene() == null)
			{
				Scene scene = new Scene(pane);
				scene.getStylesheets().add("/default.css");
				stage.setScene(scene);
			}
			else
				stage.getScene().setRoot(pane);
			//noinspection unchecked
			return (T) (currentController = loader.getController());
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(ChannelHandlerContext ctx)
	{
		player = new Player();
		player.setChannel(ctx.channel());
	}
}
