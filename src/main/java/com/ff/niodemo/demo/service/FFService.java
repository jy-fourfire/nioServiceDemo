package com.ff.niodemo.demo.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

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
	// static Logger log = Logger.getLogger(FFService.class);
	//

	static final String ADDR = "127.0.0.1";
	static final int PORT = 7777;
	static private Charset charset;

	public FFService() {
		charset = Charset.forName("UTF-8");
	}

	public static long index = 0;

	public void start() throws Exception {
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
		while (true) {
			int n = selector.select();
			if (n == 0) {
				continue; // nothing to do
			}
			System.out.println(n+"有通信....." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			Iterator it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					SocketChannel channel = server.accept();
					if (channel == null) {
						System.out.println("channel == null");// //handle code, could happen
						break;
					}
					channel.configureBlocking(false);
					channel.register(selector, SelectionKey.OP_READ);

				}
				if (key.isReadable()) {
					System.out.println("-----------------"+index);
					readDataFromSocket(key);
				}
				
				selector.selectedKeys().remove(it);
				//it.remove();
			}
		}

	}

	private void readDataFromSocket(SelectionKey key) {
		index++;
		if (index > 1000 && (index / 1000 == 0)) {
			System.out.println("==>" + index);
		}
	}

	public static synchronized void waitTest() {
		index++;
		System.out.println("处理完一个请求..." + index);
		if (1 == 1)
			return;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("模拟暂停错误--waitTest-->" + e.getMessage());
		}
	}

	private static void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
		byteBuffer.flip();
		String data = decode(byteBuffer);

		if (data.indexOf("\r\n") == -1) {
			return;
		}
		String outputData = data.substring(0, data.indexOf("\n") + 1);
		System.out.println("--->" + outputData);
		ByteBuffer outputBuffer = encode("echo:" + outputData);
		while (outputBuffer.hasRemaining()) {
			socketChannel.write(outputBuffer);
		}

		// ByteBuffer temp = encode(outputData);
		// byteBuffer.position(temp.limit());
		//
		// byteBuffer.compact();

		if (outputData.equals("bye\r\n")) {
			key.cancel();
			socketChannel.close();
			System.out.println("链接已经关闭");
		}
	}

	/**
	 * 解码
	 * @param byteBuffer
	 * @return
	 */
	private static synchronized String decode(ByteBuffer byteBuffer) {
		CharBuffer charBuffer = charset.decode(byteBuffer);
		return charBuffer.toString();
	}

	/**
	 * 编码
	 * @param str
	 * @return
	 */
	private static synchronized ByteBuffer encode(String str) {
		return charset.encode(str);
	}

}
