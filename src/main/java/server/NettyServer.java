package server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务启动
 */
public class NettyServer {
    private static Integer post = 9500;
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                /**配置一些信息, 后续可以使用channel.attr()*/
                .childAttr(AttributeKey.newInstance("childKey"), "childValue")
                /** 给条连接设置tcp底层相关的属性*/
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                /**表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数*/
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    protected void initChannel(NioServerSocketChannel ch) {
                        System.out.println("服务端启动...");
                    }
                })
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        System.out.println("有新的连接接入 远程ip"+ch.remoteAddress()+"本地ip"+ch.localAddress());
                        ch.pipeline().addLast(new FirstServerHandler());
                    }
                });
        bindPort(serverBootstrap);
    }

    public static void bindPort(final ServerBootstrap serverBootstrap) {
        serverBootstrap.bind(post).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口绑定成功!");
            } else {
                System.err.println("端口绑定失败!");
                post++;
                bindPort(serverBootstrap);
            }
        });
    }

}
