/*
 * package nirmalya.aathithya.webmodule.common.security;
 * 
 * import java.io.IOException;
 * 
 * import javax.servlet.Filter; import javax.servlet.FilterChain; import
 * javax.servlet.ServletException; import javax.servlet.ServletRequest; import
 * javax.servlet.ServletResponse; import javax.servlet.http.HttpServletRequest;
 * import javax.servlet.http.HttpServletResponse; import
 * javax.servlet.http.HttpSession;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Component;
 * 
 * @Component public class AccessFilter implements Filter {
 * 
 * @Autowired HttpSession session;
 * 
 * @Override public void doFilter(ServletRequest request, ServletResponse
 * response, FilterChain chain) throws IOException, ServletException { // TODO
 * Auto-generated method stub //
 * if(request.getRequestedSessionId().equals(anObject)) HttpServletRequest req =
 * (HttpServletRequest) request; HttpServletResponse res = (HttpServletResponse)
 * response;
 * 
 * 
 * String SESSION_HOST = (String) session.getAttribute("SESSION_HOST"); String
 * SESSION_AGENT = (String) session.getAttribute("SESSION_AGENT"); String
 * SESSION_ID = (String) session.getAttribute("SESSION_ID"); String USER_ID =
 * (String) session.getAttribute("USER_ID"); Integer PORT =
 * (Integer)session.getAttribute("SESSION_PORT");
 * 
 * // System.out.println("remote port: "+
 * request.getRemotePort()+"\t port : "+PORT); //
 * System.out.println("remote addr: "+ request.getRemoteAddr()); //
 * System.out.println("remote addr: "+ request.getRemoteHost()); //
 * System.out.println("SESSION_ID : "); // System.out.println(SESSION_ID); //
 * System.out.println("Remote Id"); //
 * /System.out.println(req.getSession().getId());
 * 
 * if(SESSION_ID != null) if(!SESSION_ID.equals(req.getSession().getId())){
 * System.out.println("Remote terminated\n\n\n\n\n\n\n"); session.invalidate();
 * }
 * 
 * try { if (USER_ID != null && SESSION_HOST != null && SESSION_AGENT != null) {
 * if(!req.getHeader("User-Agent").equals(SESSION_AGENT) ||
 * !req.getRemoteHost().equals(SESSION_HOST)){ // session.invalidate();
 * res.sendRedirect("/login?logout"); }
 * 
 * } } catch (Exception e) { // TODO Auto-generated catch block //
 * e.printStackTrace(); }
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * chain.doFilter(request, response); }
 * 
 * }
 */