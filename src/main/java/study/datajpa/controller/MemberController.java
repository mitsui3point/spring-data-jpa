package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable(value = "id") Long id) {
        return memberRepository.findById(id)
                .orElseGet(() -> Member.builder()
                        .username("not exists user")
                        .build()
                )
                .getUsername();
    }

    @GetMapping("/members/domain/{id}")
    public String findDomainClassConverterMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> findMembers(
            @Qualifier("member")
            @PageableDefault(size = 12,
                    sort = "username",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(m -> MemberDto.builder()
                        .member(m)
                        .build()
                );
    }

//    @PostConstruct
//    public void init() {
//        for (int i = 0; i < 100; i++) {
//            memberRepository.save(Member.builder().username("username" + i).age(i).build());
//        }
//    }

}
