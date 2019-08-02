package chouc.java.netty.sendorder.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class EchoOutHandler2 extends ChannelOutboundHandlerAdapter {

	 @Override
	    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
	         //执行下一个OutboundHandler
	        System.out.println("this is out2 msg:"+msg);
	        msg = "hi newed in out2";
	        super.write(ctx, msg, promise);
	        ctx.flush();

	    }

}
