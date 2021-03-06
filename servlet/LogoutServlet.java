package task8.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import task8.entity.ChatUser;
import task8.entity.jokesClass;

public class LogoutServlet extends ChatServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = (String) request.getSession().getAttribute("name");
		// ���� � ������ ������� ��� ������������...
		if (name!=null) {
			// �������� ������, ����������� ������������ � ����� ������
			ChatUser aUser = activeUsers.get(name);
			//jokesClass timer = timers.get(name);
			// ���� ������������� ������ ������������, ��������� ���
			// ���� ������, ��������� � ��������������� ������
			// ������������, ����������� ����� �� ����
			// (�.�. ������� ��� ��, ��� � ������)
			if (aUser.getSessionId().equals((String)request.getSession().getId())) {
				// ������� ������������ �� ������ ��������
				// �.�. ������� �������������� ������������,
				// ����� �������������
				synchronized (activeUsers) {
					activeUsers.remove(name);
					timers.get(aUser.getName()).stop();
					timers.remove(name);
				}
				// �������� ��� ������������ � ������
				request.getSession().setAttribute("name", null);
				// �������� ID ������ � cookie
				response.addCookie(new Cookie("sessionId", null));
				// ������������� �� ������� ��������
				response.sendRedirect(response.encodeRedirectURL("/chat/"));
			} 
			else {
				// ������������ �������� ������������ ����� ������ �
				// �� ������ ������
				response.sendRedirect(response.encodeRedirectURL("/chat/view.htm"));
			}
		} 
		else {
			// ������������� ������������ �� ������� ���� ����
			response.sendRedirect(response.encodeRedirectURL("/chat/view.htm"));
		}
	}
}