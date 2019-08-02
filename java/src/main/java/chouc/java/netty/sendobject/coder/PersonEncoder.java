package chouc.java.netty.sendobject.coder;

import chouc.java.netty.sendobject.bean.Person;
import chouc.java.netty.sendobject.utils.ByteObjConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PersonEncoder extends MessageToByteEncoder<Person> {

   @Override
   protected void encode(ChannelHandlerContext ctx, Person msg, ByteBuf out) throws Exception {
       //工具类：将object转换为byte[]
       byte[] datas = ByteObjConverter.objectToByte(msg);
       out.writeBytes(datas);
       ctx.flush();
   }
}
