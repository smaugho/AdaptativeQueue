package com.ciberos.queue;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;

public class AdaptativeQueue {
	
	public static boolean alive = false;
	
	public static int count;
	
	public static void main(String[] args) {
		alive = true;
		
		int maxWorkers = 50;
		long sleepTime = 200;
		try {
			maxWorkers = Integer.valueOf(args[0]);
			sleepTime = Long.valueOf(args[1]);
		} catch (Exception e) {}
		
		System.out.println("Running with " + maxWorkers + " max amount of Workers, spawning time: " + sleepTime + " milliseconds");
		
		new WorkersCreator(maxWorkers, sleepTime).start();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (alive) 
			try {
				String cmd = reader.readLine();
				
				if (cmd.equals("q")) {
					alive = false;
				}
				
				if (cmd.equals("w")) {
					System.out.println("Workers: " + count);
					
				}
					
			} catch (Exception e) {
			}
	}
	
	public static class WorkersCreator extends Thread {
		
		private int maxWorkers;
		private long sleepTime;
		
		public WorkersCreator(int maxWorkers, long sleepTime) {
			this.maxWorkers = maxWorkers;
			this.sleepTime = sleepTime;
		}
		
		@Override
		public void run() {
			while (alive) {
				try {
					Thread.sleep(sleepTime);
					
					if (count > this.maxWorkers) {
						System.out.println("Waiting for release of workers");
						continue;
					}
					
					Worker worker = new Worker();
					worker.start();
					
					count++;
				} catch (InterruptedException e) {
				}
			}
		}
	}
		
	
	public static class Worker extends Thread {
		@Override
		public void run() {
			
			String cmd = "php artisan queue:work --memory=512";
			
			try {
				Process p = Runtime.getRuntime().exec(cmd);
				DataInputStream in = new DataInputStream(p.getInputStream());
							
				String line = "";
				while (alive) {
					if (in.available() != 0) {	
						int read = in.read();
						if (read == -1) break;

						if (read < 0x20 || read > 0x7E) 
							if (read != 10 && read != 13) continue;
						
						if (read == 10 || read==13) {
							if (!line.equals("")) System.out.print(count + " - " + line);
							line = "";							
							
							System.out.print((char)read);
						} else {
							line = line + (char)read;
						}
					} else {
						try {
							p.exitValue();
							break;
						} catch (Exception e){}
						
						Thread.sleep(10);
					}
				}
				
				p.destroy();

			} catch (Throwable e) {
			}
			
			count--;
		}
	}

}
