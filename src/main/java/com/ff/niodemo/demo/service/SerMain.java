package com.ff.niodemo.demo.service;

import java.io.IOException;

public class SerMain {

	public static void main(String[] args) throws IOException {
		FFService service = new FFService();
		try {
			service.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("“Ï≥£Ω· ¯");
		System.in.read();
	}

}
