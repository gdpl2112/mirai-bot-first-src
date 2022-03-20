package io.github.kloping.mirai0.Main.ITools;

import io.github.kloping.mirai0.Main.Resource;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

import static io.github.kloping.mirai0.Main.Resource.THREADS;

/**
 * @author github.kloping
 */
public class Client implements Runnable {
    public static ChannelFuture f;
    public static Client INSTANCE = null;
    private String ip;
    private int port;
    private long gid;
    private boolean reconnect = false;

    public Client(String ip, int port, long gid) {
        this.ip = ip;
        this.port = port;
        this.gid = gid;
        INSTANCE = this;
        THREADS.submit(this);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public static ChannelHandlerContext CHContext = null;

    public Client(String property, String property1, String property2) {
        this(property, Integer.parseInt(property1), Integer.parseInt(property2));
    }

    public Client(String property, String property1, String property2, String property3) {
        this(property, Integer.parseInt(property1), Integer.parseInt(property2));
        if (property3 != null) {
            setReconnect(Boolean.valueOf(property3));
        }
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public long getGid() {
        return gid;
    }

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
                                Resource.bot.getGroup(gid).sendMessage("mc server: " + dataStr);
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

    @Override
    public void run() {
        try {
            f = b.connect(ip, port).sync();
            System.out.println("connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                f.channel().closeFuture().sync();
            }
        });
    }
}
