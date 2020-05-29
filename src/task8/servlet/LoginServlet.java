package task8.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import task8.entity.ChatUser;
import task8.entity.jokesClass;

public class LoginServlet extends ChatServlet {
	private static final long serialVersionUID = 1L;
	// ������������ ������, � ��������
	private int sessionTimeout = 10*60;
	private int waiting_time = 20000;
	
	public void init() throws ServletException {
		super.init();
		// ��������� �� ������������ �������� ��������� SESSION_TIMEOUT
		String value = getServletConfig().getInitParameter("SESSION_TIMEOUT");
		// ���� �� �����, �������������� ������������ ������ �� ���������
		if (value!=null) {
			sessionTimeout = Integer.parseInt(value);
		}
	}
	
	// ����� ����� ������ ��� ��������� � �������� HTTP-������� GET
	// �.�. ����� ������������ ������ ��������� ����� � ��������
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���������, ���� �� ��� � ������ �������� ��� ������������?
		String name = (String)request.getSession().getAttribute("name");
		// ������� �� ������ �������� � ���������� ������ (���������)
		String errorMessage = (String)request.getSession().getAttribute("error");
		// ������������� ���������� ������ ���������� ����
		String previousSessionId = null;
		// ���� � ������ ��� �� ���������, �� ����������
		// ������������ ��� ����� cookie
		if (name==null && request.getCookies()!=null) {
			// ����� cookie � ������ sessionId
			for (Cookie aCookie: request.getCookies()) {
				if (aCookie.getName().equals("sessionId")) {
					// ��������� �������� ����� cookie �
					// ��� ������ ������������� ������
					previousSessionId = aCookie.getValue();
					break;
				}
			}
			if (previousSessionId!=null) {
				// �� ����� session cookie
				// ���������� ����� ������������ � ����� sessionId
				for (ChatUser aUser: activeUsers.values()) {
					if(aUser.getSessionId().equals(previousSessionId)) {
						// �� ����� ������, �.�. ������������ ���
						name = aUser.getName();
						aUser.setSessionId(request.getSession().getId());//aUser ?
					}
				}
			}
		}
		// ���� � ������ ������� �� ������ ��� ������������, ��...
		if (name!=null && !"".equals(name)) {
			errorMessage = processLogonAttempt(name, request, response);
		}
		// ������������ ���������� ������ ���. �������� �����
		// ������ ��������� HTTP-������
		response.setCharacterEncoding("utf8");
		// �������� ����� ������ ��� HTTP-������
		PrintWriter pw = response.getWriter();
		pw.println("<html><head><title>Mega chat</title><meta httpequiv='Content-Type' content='text/html; charset=utf-8'/></head>");
		// ���� �������� ������ - �������� � ���
		if (errorMessage!=null) {
			pw.println("<p><font color='red'>" + errorMessage +	"</font></p>");
		}
		// ������� �����
		pw.println("<form action='/chat/' method='post'>Write name:<input type='text' name='name' value=''><input type='submit' value='Enter'>");
		pw.println("</form></body></html>");
		// �������� ��������� �� ������ � ������
		request.getSession().setAttribute("error", null);
	}
	
	// ����� ����� ������ ��� ��������� � �������� HTTP-������� POST
	// �.�. ����� ������������ ���������� �������� ������
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ������ ��������� HTTP-������� - ����� �����!
		// ����� ������ �������� ����� �����������
		request.setCharacterEncoding("UTF-8");
		// ������� �� HTTP-������� �������� ��������� 'name'
		String name = (String)request.getParameter("name");
		// ��������, ��� ���������� ������ ���
		String errorMessage = null;
		if (name==null || "".equals(name)) {
			// ������ ��� ����������� - �������� �� ������
			errorMessage = "��� ������������ �� ����� ���� ������!";
		} 
		else {
			// ���� �� �� ������, �� ���������� ���������� ������
			errorMessage = processLogonAttempt(name, request, response);
		}
		if (errorMessage!=null) {
			// �������� ��� ������������ � ������
			request.getSession().setAttribute("name", null);
			// ��������� � ������ ��������� �� ������
			request.getSession().setAttribute("error", errorMessage);
			// �������������� ������� �� �������� �������� � ������
			
			response.sendRedirect(response.encodeRedirectURL("/chat/"));
		}
	}
		// ���������� ��������� �������� ��������� ������ ��� null

	String processLogonAttempt(String name, HttpServletRequest request,	HttpServletResponse response) throws IOException{
		// ���������� ������������� Java-������ ������������
		String sessionId = request.getSession().getId();
		// ������� �� ������ ������, ��������� � ���� ������
		ChatUser aUser = activeUsers.get(name);
		if (aUser==null) {
			// ���� ��� ��������, �� ��������
			// ������ ������������ � ������ ��������
			aUser = new ChatUser(name,Calendar.getInstance().getTimeInMillis(), sessionId);
			// ��� ��� ������������ ����������� �������
			// �� ��������� �������������
			// �� ���������� ������������� �� �������
			String time = getServletConfig().getInitParameter("JOKE_TIMEOUT");
			if(time!=null){
				waiting_time = Integer.parseInt(time);
			
			}
			jokesClass timer = new jokesClass(aUser, messages, waiting_time);
			
			synchronized (activeUsers) {
				activeUsers.put(aUser.getName(), aUser);
				timers.put(aUser.getName(), timer);
				timers.get(aUser.getName()).start();
			}
		}
		if (aUser.getSessionId().equals(sessionId) || aUser.getLastInteractionTime()<(Calendar.getInstance().getTimeInMillis()-sessionTimeout*1000)) {
			// ���� ��������� ��� ����������� �������� ������������,
			// ���� ��� ������������ ����-�� �������, �� ������ �������,
			// �� �������� ������ ������������ �� ��� ���
			// �������� ��� ������������ � ������
			request.getSession().setAttribute("name", name);
			// �������� ����� �������������� ������������ � ��������
			aUser.setLastInteractionTime(Calendar.getInstance().getTimeInMillis());
			// �������� ������������� ������ ������������ � cookies
			Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
			// ���������� ���� �������� cookie 1 ���
			sessionIdCookie.setMaxAge(60*60*24*365);
			// �������� cookie � HTTP-�����
			response.addCookie(sessionIdCookie);
			// ������� � �������� ���� ����
			
			response.sendRedirect(response.encodeRedirectURL("/chat/view.htm"));
			// ������� null, �.�. ��������� �� ������� ���
			return null;
		} 
		else {
			
			// ������?���� � ������ ��� ��� ���������� �� ���-�� ������.
			// ����������, �������� � ��������� ������ ������ ���
			return "��������, �� ��� <strong>" + name + "</strong> ��� ���-�� ������. ���������� �������� ������ ���!";
		}
	}
}