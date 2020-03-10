package task8.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import task8.entity.ChatMessage;
import task8.entity.ChatUser;
import task8.entity.jokesClass;

public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// ����� ������� �������������
	protected HashMap<String, ChatUser> activeUsers;
	// ������ ��������� ����
	protected ArrayList<ChatMessage> messages;
	protected HashMap<String, jokesClass> timers;

	@SuppressWarnings("unchecked")
	public void init() throws ServletException {
		// ������� �������������� �� HttpServlet ������ init()
		super.init();
		// ������� �� ��������� ����� ������������� � ������ ���������
		activeUsers = (HashMap<String, ChatUser>) getServletContext().getAttribute("activeUsers");
		messages = (ArrayList<ChatMessage>) getServletContext().getAttribute("messages");
		timers = (HashMap<String, jokesClass>) getServletContext().getAttribute("timers");
		// ���� ����� ������������� �� ���������� ...
		if (activeUsers == null) {
			// ������� ����� �����
			activeUsers = new HashMap<String, ChatUser>();
			// ��������� �? � �������� ��������,
			// ����� ������ �������� ����� �� ���� ���������
			getServletContext().setAttribute("activeUsers", activeUsers);
		}
		// ���� ������ ��������� �� �������?� ...
		if (messages == null) {
			// ������� ����� ������
			messages = new ArrayList<ChatMessage>(100);
			// ��������� ��� � �������� ��������,
			// ����� ������ �������� ����� �� ���� �������
			getServletContext().setAttribute("messages", messages);
		}
		if(timers == null){
			timers = new HashMap<String, jokesClass>(100);
			getServletContext().setAttribute("timers", timers);
		}
	}
}