package fr.galaxyoyo.rp.server;

import fr.galaxyoyo.rp.Player;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerChannelHandler extends ChannelHandlerAdapter
{
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		if (cause instanceof IOException)
		{
			System.err.println(cause.getClass().getName() + " : " + cause.getMessage());
			Server.getServer().disconnect(ctx.channel());
		}
		else
			cause.printStackTrace();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		ctx.channel().config().setMaxMessagesPerRead(1);
		Player player = Server.getServer().getPlayer(ctx.channel());
		player.setLastIp(((InetSocketAddress) ctx.channel().remoteAddress()).getHostName());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { Server.getServer().disconnect(ctx.channel()); }

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception { ctx.flush(); }
}
