package org.tiny.net.core;

import org.tiny.net.log.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * client
 * 
 * @author chunbo
 */
public class Connector {

	private String ip;
	private int port = 8800;
	private ChannelInitializer<SocketChannel> channelInitializer;

	// 配置客户端NIO线程组
	private EventLoopGroup group = new NioEventLoopGroup();

	public Connector(String ip, int port, ChannelInitializer<SocketChannel> channelInitializer) {
		this.ip = ip;
		this.port = port;
		this.channelInitializer = channelInitializer;

		start();
	}

	private void start() {

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							ch.pipeline().addLast(new ProtobufEncoder());
							ch.pipeline().addLast(channelInitializer);
						}
					});

			// 发起异步连接操作
			ChannelFuture f = b.connect(ip, port).sync();
			if (f.isSuccess()) {
				Logger.LOG.error("connect{}:{} success", ip, port);
			}

			// 当客户端链路关闭
			// f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logger.LOG.error("connect{}:{} fail", ip, port);
			close();
		}
	}
	
	// TODO 连接断开的时候 需要手动调用
	private void close() {
		// 优雅退出，释放NIO线程组
		group.shutdownGracefully();
	}
}
