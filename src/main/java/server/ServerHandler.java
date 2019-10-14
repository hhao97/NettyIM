package server;

import contact.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            if (valid(loginRequestPacket)) {
                LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
                loginResponsePacket.setVersion(packet.getVersion());
                if (valid(loginRequestPacket)) {
                    loginResponsePacket.setSuccess(true);
                } else {
                    loginResponsePacket.setReason("账号密码校验失败");
                    loginResponsePacket.setSuccess(false);
                }
                // 编码
                ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
                ctx.channel().writeAndFlush(responseByteBuf);
            } else {
                // 校验失败
            }

        }


    }


    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
