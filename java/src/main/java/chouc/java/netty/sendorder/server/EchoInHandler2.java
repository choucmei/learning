package chouc.java.netty.sendorder.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoInHandler2 extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
        System.out.println("This is in2 client's msg:" + msg);
        //向客户端写数据
        String currentTime = "channel2 send message";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(currentTime);


//		String currentTime = new Date(System.currentTimeMillis()).toString();
//		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
//		ctx.write(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();//刷新后才将数据发出到SocketChannel
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
