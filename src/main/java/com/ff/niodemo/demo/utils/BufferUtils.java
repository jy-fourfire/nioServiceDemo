package com.ff.niodemo.demo.utils;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class BufferUtils {

	  /**
     * NIO 读取标准模板
     * @param k
     * @return
     */
//    private boolean read(SelectionKey k) {
//        final SocketChannel ch = (SocketChannel) k.channel();
//        final NioSocketChannel channel = (NioSocketChannel) k.attachment();
//
//        final ReceiveBufferSizePredictor predictor =
//            channel.getConfig().getReceiveBufferSizePredictor();
//        final int predictedRecvBufSize = predictor.nextReceiveBufferSize();
//
//        int ret = 0;
//        int readBytes = 0;
//        boolean failure = true;
//        //取出一个buffer
//        ByteBuffer bb = recvBufferPool.acquire(predictedRecvBufSize);
//        try {
//        	//条件为>0,=0 会造成循环
//            while ((ret = ch.read(bb)) > 0) {
//                readBytes += ret;
//                if (!bb.hasRemaining()) {	//读取操作会直到buffer没有remaning,所以这里安全，下次会再读取
//                    break;
//                }
//            }
//            failure = false;
//        } catch (ClosedChannelException e) {	//对于closedChannelException 不做处理
//            // Can happen, and does not need a user attention.
//        } catch (Throwable t) {
//            fireExceptionCaught(channel, t);
//        }
//
//        if (readBytes > 0) {					//如果读取出来数据个数大于0则进行处理
//            bb.flip();
//
//            final ChannelBufferFactory bufferFactory =
//                channel.getConfig().getBufferFactory();
//            final ChannelBuffer buffer = bufferFactory.getBuffer(readBytes);
//            buffer.setBytes(0, bb);
//            buffer.writerIndex(readBytes);
//
//            recvBufferPool.release(bb);
//
//            // Update the predictor.
//            predictor.previousReceiveBufferSize(readBytes);
//
//            // Fire the event.
//            fireMessageReceived(channel, buffer);
//        } else {
//            recvBufferPool.release(bb);
//        }
//
//        if (ret < 0 || failure) {			//如果最后一次读取出来　len = -1 表示client shutdown 服务端应用　close channel 
//            close(channel, succeededFuture(channel));
//            return false;
//        }
//
//        return true;
//    }
}
