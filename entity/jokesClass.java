package task8.entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Timer;

public class jokesClass{
	private ChatUser author;
	private ArrayList<ChatMessage> messages;
	//private int a=0;
	private final int Time=20000; //TODO заменить на взятие из дескриптора
	
	private Timer onlineActionTimer = new Timer(Time, new ActionListener(){
		public void actionPerformed(ActionEvent ev){
			messages.add(new ChatMessage("abcde", author, Calendar.getInstance().getTimeInMillis()));
			/*System.out.println(a);
			a+=1;*/
		}
	});
	
	public jokesClass(ChatUser user, ArrayList<ChatMessage> messages_new){
		System.out.println('+');
		author = user;
		messages = messages_new;
	}
	
	public void start(){
		onlineActionTimer.start();
	}
	
	public void stop(){
		onlineActionTimer.stop();
	}
	public void restart(){
		onlineActionTimer.restart();
		
	}

}
