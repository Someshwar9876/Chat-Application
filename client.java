package application;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class client {
	Socket socket;
	DataInputStream readFromSocket;
	DataOutputStream writeToSocket;
	
	public client() {
		try {
			socket = new Socket("192.168.56.1", 6668);
			System.out.println("Connected to server");
			readFromSocket = new DataInputStream(socket.getInputStream());
			writeToSocket = new DataOutputStream(socket.getOutputStream());
			writeToSocket.writeUTF(application.appPage.userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeMessageToServer(String sendMsg, String receiverName) throws Exception {
		if (readFromSocket == null || writeToSocket == null) {
			System.out.println("Server has closed its connection");
		}
		if (sendMsg.equals("")) {
			return;
		}
		writeToSocket.writeUTF(receiverName + "@#" + sendMsg);
		writeToSocket.flush();
	}

	public void sendFile(String receiverName,File file,String option) throws Exception {
		writeToSocket.writeUTF(receiverName+"@#"+option);
		writeToSocket.writeLong(file.length());
		System.out.println(file.length());
		writeToSocket.flush();
		byte[] fileNameInBytes = file.getName().getBytes(); 
		writeToSocket.writeInt(fileNameInBytes.length);
		writeToSocket.write(fileNameInBytes);
		
		//sending file content
		FileInputStream fileInputStream = new FileInputStream(file);
		int bytes = 0;
		byte[] buffer = new byte[4*1024];
		while((bytes = fileInputStream.read(buffer)) != -1) {
			writeToSocket.write(buffer,0,bytes);
			writeToSocket.flush();
		}
		fileInputStream.close();
		System.out.println("file sent");
	}
	
	public File receiveFile() {
		try {
			long fileSize = readFromSocket.readLong();
			System.out.println(fileSize);
			int fileNameSize = readFromSocket.readInt();
			System.out.println(fileNameSize);
			byte[] fileNameBytes = new byte[fileNameSize];
			readFromSocket.readFully(fileNameBytes,0,fileNameSize);
			System.out.println("C:\\Users\\Dell\\Downloads\\"+new String(fileNameBytes));
			
			//receiving real file content
			int bytes = 0;
			byte[] buffer = new byte[4*1024];
			File file = new File("C:\\Users\\Dell\\Downloads\\"+new String(fileNameBytes));
			int i = 0;
			while(file.exists()) {
				file = new File("C:\\Users\\Dell\\Downloads\\"+new String(fileNameBytes)+"("+ ++i +")");
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			while(fileSize>0 && (bytes = readFromSocket.read(buffer, 0,(int) Math.min(fileSize, buffer.length))) != -1 ) {
				fileOutputStream.write(buffer, 0, bytes);
				fileSize -= bytes;
				System.out.println(bytes+"inside while");
			}
			fileOutputStream.close();
			System.out.println("file read sucessfully");
			return file;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void readMessageFromServer(appPage appPage) {
		Thread thread2 = new Thread() {
			public void run() {
				String msgString;
				try {
					if (readFromSocket == null || writeToSocket == null) {
						System.out.println("Server has closed its connection");
						System.exit(1);
					}
					while ((msgString = readFromSocket.readUTF()) != null) {
						System.out.println("client :"+msgString);
						StringTokenizer stringTokenizer = new StringTokenizer(msgString,"@#");
						String receiverName = stringTokenizer.nextToken();
						String msg = stringTokenizer.nextToken(); 
						if(msg.equals("***File***")) {
							File file;
							file = receiveFile();
							appPage.receiveMsg(receiverName, file);
						}
						else if(msg.equals("***Photo***")) {
							File file;
							file = receiveFile();
							appPage.addImageToVbox(receiverName,file);
						}
						else {
							appPage.receiveMsg(receiverName, msg);
						}
						System.out.println("Msg read");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						readFromSocket.close();
						writeToSocket.close();
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread2.start();
	}
}