package task8.entity;

import java.util.ArrayList;
import java.util.Calendar;

public class jokesClass implements Runnable{
	private ChatUser author;
	private ArrayList<ChatMessage> messages;
	private Thread thread;
	private int a=0;
	private boolean working = false;
	
	public jokesClass(ChatUser user, ArrayList<ChatMessage> messages_new){
		super();
		System.out.println('+');
		author = user;
		messages = messages_new;
		thread = new Thread(this);
		
	}
	
	public void run() {
		try {
			while(working){
				Thread.sleep(20000);
				System.out.println(a);
				messages.add(new ChatMessage("abcde", author, Calendar.getInstance().getTimeInMillis()));
				a += 1;
					
			} 
		}
		catch (InterruptedException e) {
		}
		
	}
	
	public void start(){
		working = true;
		thread.start();
	}
	
	public void stop(){
		working = false;
	}

}
