package hello.login.web.login;

import hello.login.SessionConst;
import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionManagement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final SessionManagement sessionManagement;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm, HttpServletResponse response) {
        Member login = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(login == null) {
            log.info("로그인 실패");
            return "login/loginForm";
        }

        Cookie idCookie = new Cookie("memberId", String.valueOf(login.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV1(@ModelAttribute("loginForm") LoginForm loginForm, HttpServletResponse response) {
        Member login = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(login == null) {
            log.info("로그인 실패");
            return "login/loginForm";
        }

        sessionManagement.createSession(login, response);

        return "redirect:/";
    }


    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member login = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(login == null) {
            log.info("로그인 실패");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, login);



        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }

//        sessionManager.expire(request);
        return "redirect:/";
    }

}
