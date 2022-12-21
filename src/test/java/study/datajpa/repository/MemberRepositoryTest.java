package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    private Logger log = LoggerFactory.getLogger(MemberRepositoryTest.class);

    @Test
    void memberJoinTest() {
        //given
        Member member = Member.builder().username("usernameA").build();
        //when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).orElseGet(() -> Member.builder().username("noname!").build());
        //then
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(member).isEqualTo(findMember);
    }

    @Test
    void memberRepositoryTest() {
        log.info("memberRepository = " + memberRepository.getClass());
    }

    @Test
    void memberDeleteTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member savedMember = memberRepository.save(memberA);
        //when
        memberRepository.delete(savedMember);
        Member noMember = Member.builder().username("nouser").build();
        Member findMember = memberRepository.findById(savedMember.getId()).orElseGet(() -> noMember);
        //then
        assertThat(findMember).isEqualTo(noMember);
    }

    @Test
    void memberFindAllTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberRepository.save(memberA);
        Member savedMemberB = memberRepository.save(memberB);
        Member savedMemberC = memberRepository.save(memberC);
        Member[] expected = {savedMemberA, savedMemberB, savedMemberC};
        //when
        List<Member> actual = memberRepository.findAll();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void memberFindByIdTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberRepository.save(memberA);
        Member savedMemberB = memberRepository.save(memberB);
        Member savedMemberC = memberRepository.save(memberC);
        //when
        Optional<Member> actual = memberRepository.findById(memberA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(memberA));
    }

    @Test
    void memberFindByIdNotFoundTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberRepository.save(memberA);
        Member savedMemberB = memberRepository.save(memberB);
        Member savedMemberC = memberRepository.save(memberC);
        //when
        Optional<Member> actual = memberRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void memberCountTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberRepository.save(memberA);
        Member savedMemberB = memberRepository.save(memberB);
        Member savedMemberC = memberRepository.save(memberC);
        Long expected = 3L;
        //when
        Long actual = memberRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
