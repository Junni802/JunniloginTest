package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionManagement {

    private static final String SESSION_COOKIE_NAME = "mySessionId";

    // ConcurrentHashMap -> 동시성 이슈가 있을경우 가 있으므로 ConcurrentHashMap사용
    private final Map<String, Object> sessionStroe = new ConcurrentHashMap<>();

    // 세션생성

    /**
     *
     * @param value = Member 객체를 주입하기위한 매개변수
     * @param response = Cookie를 보내기위한 매개변수
     */
    public void createSession(Object value, HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();

        sessionStroe.put(uuid, value);

        log.info("Member[{}]", value);
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, uuid);
        response.addCookie(cookie);
    }


    // 세션조회
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null) {
            return null;
        }
        return sessionStroe.get(sessionCookie);
    }

    // 세션 만료
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie != null) {
            sessionStroe.remove(sessionCookie.getValue());
        }
    }


    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                .findAny()
                .orElse(null);
    }

}
