package servlet;

import java.io.IOException;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/bipul/*")
public class SecurityFilter implements Filter {
    private static final Set<String> ALLOWED_PATHS = Set.of("/login", "/signup", "/gen-otp", "/match-otp");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String path = req.getServletPath();

        setSecurityHeaders(res);
        setCORSHeaders(req, res);

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (ALLOWED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null) {
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private void setSecurityHeaders(HttpServletResponse res) {
        res.setHeader("X-Frame-Options", "DENY");
        res.setHeader("X-XSS-Protection", "1; mode=block");
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        res.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    }

    private void setCORSHeaders(HttpServletRequest req, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, enctype");
        res.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    public void destroy() {}
}