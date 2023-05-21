package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    MemberRepository memberRepository = new MemberRepository();

    public Member login(String loginId, String password) {
        Optional<Member> member = memberRepository.findById(loginId);
        return member.filter(member1 -> member1.getPassword().equals(password))
                .orElse(null);
    }

}
