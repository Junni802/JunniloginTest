package hello.login.web.filter;

import hello.login.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestURI = servletRequest.getRequestURI();
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            if(isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행{}", requestURI);
                HttpSession session = servletRequest.getSession(false);
                if(session == null ||
                    session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);

                    servletResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; // 여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝난다
                }
            }
            chain.doFilter(request, response);
        }catch (Exception e) {
            throw e;
        }finally {
            log.info("인증 체크필터 종료 {}" + requestURI);
        }
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

}
