package com.ff.niodemo.demo.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * 
 * <ul>
 * <li>项目名称：nioDemo1 </li>
 * <li>类名称：  FFService </li>
 * <li>类描述：NIO service   </li>
 * <li>创建人：jy </li>
 * <li>创建时间：2015年3月9日 </li>
 * <li>修改备注：</li>
 * </ul>
 */
public class FFService {

	static final String ADDR = "127.0.0.1";
	static final int PORT = 7777;

	public void stop() throws IOException {
		System.out.println("启动服务....");
		// 创建一个selector
		Selector selector = Selector.open();
		// 创建一个服务通道
		ServerSocketChannel serChannel = ServerSocketChannel.open();
		// 绑定到一个端口上面
		serChannel.socket().bind(new InetSocketAddress(ADDR, PORT));
		// 设置非阻塞模式
		serChannel.configureBlocking(false);
		// 注册到选择器
		serChannel.register(selector, SelectionKey.OP_ACCEPT);
		for (;;) {
			if (selector.select() == 0) {
				continue;
			}
			System.out.println("有通信....." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			// 获取发生了关注时间的Key集合，每个SelectionKey对应了注册的一个通道
			Set<SelectionKey> keys = selector.selectedKeys();
			// 多说一句selector.keys()返回所有的SelectionKey(包括没有发生事件的)
			for (SelectionKey key : keys) {
				if (key.isValid()) {
					System.out.println("key.isValid()..........");
				}
				// OP_ACCEPT 这个只有ServerSocketChannel才有可能触发
				if (key.isAcceptable()) {
					System.out.println("key.isAcceptable()..........");
					// 得到与客户端的套接字通道
					SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
					// 同样设置为非阻塞模式
					channel.configureBlocking(false);
					// 同样将于客户端的通道在selector上注册，OP_READ对应可读事件(对方有写入数据),可以通过key获取关联的选择器
					channel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				}
				// OP_READ 有数据可读
				if (key.isReadable()) {
					System.out.println("key.isReadable()..........");
					SocketChannel channel = (SocketChannel) key.channel();
					// 得到附件，就是上面SocketChannel进行register的时候的第三个参数,可为随意Object
					ByteBuffer buffer = (ByteBuffer) key.attachment();
					// 读数据 这里就简单写一下，实际上应该还是循环读取到读不出来为止的
					channel.read(buffer);
					// 改变自身关注事件，可以用位或操作|组合时间
					key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				}
				// OP_WRITE 可写状态 这个状态通常总是触发的，所以只在需要写操作时才进行关注
				if (key.isWritable()) {
					System.out.println("key.isWritable()..........");
					// 写数据掠过，可以自建buffer，也可用附件对象(看情况),注意buffer写入后需要flip
					
					// 写完就吧写状态关注去掉，否则会一直触发写事件
					key.interestOps(SelectionKey.OP_READ);
				}
				if (key.isConnectable()) {
					System.out.println("key.isConnectable()..........");
				}
				
				// 由于select操作只管对selectedKeys进行添加，所以key处理后我们需要从里面把key去掉
				keys.remove(key);
				System.out.println("处理完一个请求...");
			}
		}

	}

}
