package hello.login.web;

import hello.login.SessionConst;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberRepository memberRepository;

//    @GetMapping("/")
//    public String home() {
//        return "redirect:/items";
//    }


//    @GetMapping("/")
    public String home() {
        return "home";
    }


//    @GetMapping("/")
    public String homeV1(@CookieValue(value = "memberId", required = false) Long memberId, Model model) {
        if(memberId == null) {
            return "home";
        }

        // 로그인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Long memberId, Model model) {

        HttpSession session = request.getSession(false);
        if(session == null) {
            return "home";
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션 관리자에 저장된 회원정보 조회
//        Member member = (Member)sessionManager.getSession(request);

        // 로그인
        if(member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginHome";
    }

}