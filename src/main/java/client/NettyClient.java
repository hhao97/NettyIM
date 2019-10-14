package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 客户端
 */
public class NettyClient {
    private static Integer MAX_RETRY = 6;
    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("客户端启动...");
//                        ch.pipeline().addLast(new FirstClientHandler());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        connectServer("127.0.0.1", 9500, bootstrap, MAX_RETRY);

    }

    public static void connectServer(String ipAddr, int post, Bootstrap bootstrap, int retry) {
        bootstrap.connect(ipAddr, post).addListener(future -> {
            if (future.isSuccess())
                System.out.println("connect success");
            else if (retry == 0)
                System.out.println("retry eq zero stop retry");
            else {
                int order = (MAX_RETRY - retry) + 1;
                int delay = 1 << order;
                System.err.println(new Date() + ":connect error , retry " + retry);
                bootstrap.config().group().schedule(() -> connectServer(ipAddr, post, bootstrap,
                        retry - 1), delay,
                        TimeUnit.SECONDS);
            }
        });

    }
}
