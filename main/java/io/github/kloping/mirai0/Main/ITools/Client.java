package io.github.kloping.mirai0.Main.ITools;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

import static io.github.kloping.mirai0.Main.BotStarter.test;
import static io.github.kloping.mirai0.Main.Resource.*;

/**
 * @author github.kloping
 */
public class Client implements Runnable {
    public static ChannelFuture f;
    public static Client INSTANCE = null;
    public static ChannelHandlerContext CHContext = null;
    private String ip;
    private int port;
    private long gid;
    private boolean reconnect = false;
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap b = new Bootstrap();

    {
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                CHContext = ctx;
                                ByteBuf buf = (ByteBuf) msg;
                                String dataStr = buf.toString(StandardCharsets.UTF_8);
                                MessageTools.sendMessageInGroup("mc server: " + dataStr, gid);
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                CHContext = ctx;
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) {

                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                cause.printStackTrace();
                                ctx.close();
                                CHContext = null;
                            }
                        });
                    }
                }).option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535));
    }

    public Client(String ip, int port, long gid) {
        this.ip = ip;
        this.port = port;
        this.gid = gid;
        INSTANCE = this;
        THREADS.submit(this);
    }

    public Client(String property, String property1, String property2) {
        this(property, Integer.parseInt(property1), Integer.parseInt(property2));
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    @Override
    public void run() {
        try {
            if (f != null && f.channel().isOpen()) {
                f.channel().closeFuture().sync();
            }
            f = b.connect(ip, port).sync();
            System.out.println("connected");
        } catch (Exception e) {
            e.printStackTrace();
            if (!test)
                bot.getFriend(superQL).sendMessage(e.getMessage());
        }
    }
}
