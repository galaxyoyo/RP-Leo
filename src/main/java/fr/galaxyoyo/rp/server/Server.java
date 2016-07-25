package fr.galaxyoyo.rp.server;

import com.google.gson.reflect.TypeToken;
import fr.galaxyoyo.rp.Character;
import fr.galaxyoyo.rp.Player;
import fr.galaxyoyo.rp.RP;
import fr.galaxyoyo.rp.packets.PacketManager;
import fr.galaxyoyo.rp.packets.PacketOutSendCharacter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Server
{
	private static Server instance;
	private final List<Player> players = new ArrayList<>();
	private final ObservableList<Character> characters = FXCollections.observableArrayList();

	public static void setup()
	{
		instance = new Server();

		File file = new File("characters.json");
		try
		{
			instance.characters.addListener((ListChangeListener<Character>) observable -> getServer().save());

			if (file.exists())
			{
				List<Character> allCharacters = RP.getGson().fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), new TypeToken<ArrayList<Character>>() {}
						.getType());
				instance.characters.addAll(allCharacters);
			}
			else
			{
				Character c = new Character();
				c.setUuid(UUID.randomUUID());
				instance.characters.add(c);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		ServerBootstrap boot =
				new ServerBootstrap().group(new NioEventLoopGroup(1), new NioEventLoopGroup(1)).channel(NioServerSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
						.option(ChannelOption.SO_BACKLOG, 100).childOption(ChannelOption.SO_RCVBUF, 0x42666).childOption(ChannelOption.TCP_NODELAY, true)
						.childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(0x42666)).childHandler(new ServerChannelInitializer());
		ChannelFuture f = boot.bind(42999);
		f.awaitUninterruptibly();

		new Thread("Keep-Alive Thread")
		{
			@Override
			public void run()
			{
				//noinspection InfiniteLoopStatement
				while (true)
				{
					try
					{
						sleep(Long.MAX_VALUE);
					}
					catch (InterruptedException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void save()
	{
		try
		{
			File file = new File("characters.json");
			//noinspection ResultOfMethodCallIgnored
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			RP.getGson().toJson(instance.characters, bw);
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Server getServer()
	{
		return instance;
	}

	public void disconnect(Channel channel)
	{
		disconnect(getPlayer(channel));
	}

	public void disconnect(Player player)
	{
		player.setChannel(null);
		players.remove(player);
	}

	public Player getPlayer(Channel channel)
	{
		Player player = players.stream().filter(p -> p.getChannel() == channel).findAny().orElse(null);
		if (player == null)
		{
			player = new Player();
			player.setChannel(channel);
			players.add(player);

			Player finalPlayer = player;
			characters.forEach(character ->
			{
				PacketOutSendCharacter pkt = PacketManager.createPacket(PacketOutSendCharacter.class);
				pkt.setCharacter(character);
				PacketManager.sendPacketToPlayer(finalPlayer, pkt);
			});
		}
		return player;
	}

	public List<Player> getConnectedPlayers()
	{
		return players.stream().filter(player -> player.getChannel() != null).collect(Collectors.toList());
	}

	public void addCharacter(Character c)
	{
		characters.add(c);
	}

	public Character getCharacter(UUID uuid)
	{
		return characters.stream().filter(c -> c.getUuid().equals(uuid)).findAny().orElse(null);
	}
}
