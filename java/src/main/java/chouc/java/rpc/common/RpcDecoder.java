package chouc.java.rpc.common;

import chouc.java.netty.sendobject.utils.ByteBufToBytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * RPC 解码器
 *
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

	// 构造函数传入向反序列化的class
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(" *****decode*****");
        ByteBufToBytes read = new ByteBufToBytes();
        byte[] data = read.read(in);
        System.out.println(" *****decode***** data size:"+data.length);
        System.out.println(" *****decode***** data:"+data);
        Object obj = SerializationUtil.deserialize(data, genericClass);
        System.out.println(" *****decode***** data:"+obj);
        out.add(obj);
    }
}
