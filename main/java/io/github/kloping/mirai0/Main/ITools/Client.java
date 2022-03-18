package io.github.kloping.mirai0.Main.ITools;

import io.github.kloping.mirai0.Main.Resource;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @author github.kloping
 */
public class Client extends Thread {
    public static ChannelFuture f;
    public static Client INSTANCE = null;
    private String ip;
    private int port;
    private long gid;

    public Client(String ip, int port, long gid) {
        this.ip = ip;
        this.port = port;
        this.gid = gid;
        start();
        INSTANCE = this;
    }

    public static ChannelHandlerContext CHContext = null;

    public Client(String property, String property1, String property2) {
        this(property, Integer.parseInt(property1), Integer.parseInt(property2));
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

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
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
            f = b.connect(ip, port).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
        }
    }
}
