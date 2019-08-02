package chouc.java.rpc.common;

import chouc.java.netty.sendobject.utils.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC 编码器
 *
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {

	private Class<?> genericClass;

	// 构造函数传入向反序列化的class
	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	public void encode(ChannelHandlerContext ctx, Object inob, ByteBuf out)
			throws Exception {
		System.out.println(inob);
		byte[] datas = SerializationUtil.serialize(genericClass.cast(inob));
		System.out.println(datas.length);
		out.writeBytes(datas);
		ctx.flush();
	}

}