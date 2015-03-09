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
 * <li>��Ŀ���ƣ�nioDemo1 </li>
 * <li>�����ƣ�  FFService </li>
 * <li>��������NIO service   </li>
 * <li>�����ˣ�jy </li>
 * <li>����ʱ�䣺2015��3��9�� </li>
 * <li>�޸ı�ע��</li>
 * </ul>
 */
public class FFService {

	static final String ADDR = "127.0.0.1";
	static final int PORT = 7777;

	public void stop() throws IOException {
		System.out.println("��������....");
		// ����һ��selector
		Selector selector = Selector.open();
		// ����һ������ͨ��
		ServerSocketChannel serChannel = ServerSocketChannel.open();
		// �󶨵�һ���˿�����
		serChannel.socket().bind(new InetSocketAddress(ADDR, PORT));
		// ���÷�����ģʽ
		serChannel.configureBlocking(false);
		// ע�ᵽѡ����
		serChannel.register(selector, SelectionKey.OP_ACCEPT);
		for (;;) {
			if (selector.select() == 0) {
				continue;
			}
			System.out.println("��ͨ��....." + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			// ��ȡ�����˹�עʱ���Key���ϣ�ÿ��SelectionKey��Ӧ��ע���һ��ͨ��
			Set<SelectionKey> keys = selector.selectedKeys();
			// ��˵һ��selector.keys()�������е�SelectionKey(����û�з����¼���)
			for (SelectionKey key : keys) {
				if (key.isValid()) {
					System.out.println("key.isValid()..........");
				}
				// OP_ACCEPT ���ֻ��ServerSocketChannel���п��ܴ���
				if (key.isAcceptable()) {
					System.out.println("key.isAcceptable()..........");
					// �õ���ͻ��˵��׽���ͨ��
					SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
					// ͬ������Ϊ������ģʽ
					channel.configureBlocking(false);
					// ͬ�����ڿͻ��˵�ͨ����selector��ע�ᣬOP_READ��Ӧ�ɶ��¼�(�Է���д������),����ͨ��key��ȡ������ѡ����
					channel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				}
				// OP_READ �����ݿɶ�
				if (key.isReadable()) {
					System.out.println("key.isReadable()..........");
					SocketChannel channel = (SocketChannel) key.channel();
					// �õ���������������SocketChannel����register��ʱ��ĵ���������,��Ϊ����Object
					ByteBuffer buffer = (ByteBuffer) key.attachment();
					// ������ ����ͼ�дһ�£�ʵ����Ӧ�û���ѭ����ȡ����������Ϊֹ��
					channel.read(buffer);
					// �ı������ע�¼���������λ�����|���ʱ��
					key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				}
				// OP_WRITE ��д״̬ ���״̬ͨ�����Ǵ����ģ�����ֻ����Ҫд����ʱ�Ž��й�ע
				if (key.isWritable()) {
					System.out.println("key.isWritable()..........");
					// д�����ӹ��������Խ�buffer��Ҳ���ø�������(�����),ע��bufferд�����Ҫflip
					
					// д��Ͱ�д״̬��עȥ���������һֱ����д�¼�
					key.interestOps(SelectionKey.OP_READ);
				}
				if (key.isConnectable()) {
					System.out.println("key.isConnectable()..........");
				}
				
				// ����select����ֻ�ܶ�selectedKeys������ӣ�����key�����������Ҫ�������keyȥ��
				keys.remove(key);
				System.out.println("������һ������...");
			}
		}

	}

}
