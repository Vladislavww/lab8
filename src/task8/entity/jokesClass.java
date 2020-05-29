package task8.entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Timer;

public class jokesClass{
	private ChatUser author;
	private ArrayList<ChatMessage> messages;
	private Timer onlineActionTimer;
	private final String Joke = "ќп€ть чиновники что-то напутали и не так доложили наверх! –осси€не просили обнулить ипотеки!";
	
	public jokesClass(ChatUser user, ArrayList<ChatMessage> messages_new, int time){
		author = user;
		messages = messages_new;
		onlineActionTimer = new Timer(time, new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				messages.add(new ChatMessage("abcde", author, Calendar.getInstance().getTimeInMillis()));
			}
		});
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
