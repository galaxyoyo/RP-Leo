package fr.galaxyoyo.rp;

import io.netty.channel.Channel;

public class Player
{
	private transient Channel channel;
	private String lastIp;

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}

	public String getLastIp()
	{
		return lastIp;
	}

	public void setLastIp(String lastIp)
	{
		this.lastIp = lastIp;
	}
}
