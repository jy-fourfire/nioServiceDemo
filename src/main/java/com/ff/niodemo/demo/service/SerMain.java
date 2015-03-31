package com.ff.niodemo.demo.service;

import java.io.IOException;

public class SerMain {

	public static void main(String[] args) throws Exception {
		FFService service = new FFService();
		try {
			service.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("异常结束");
		System.in.read();
	}

}
