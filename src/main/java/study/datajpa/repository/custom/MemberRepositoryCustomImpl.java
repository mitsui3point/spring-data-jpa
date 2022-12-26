package study.datajpa.repository.custom;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 사용자 정의 리포지토리 구현 최신 방식
 *  네이밍 룰:
 *      MemberRepositoryCustom + Impl(추가된 방식, 기존방식 사용가능)
 *  규칙:
 *      사용자 정의 구현 클래스에 리포지토리 인터페이스 이름 + Impl
 *      현재 구현체가 구현하고 있는 리포지토리 인터페이스 의 이름
 *  스프링 데이터 2.x 부터는 사용자 정의 구현 클래스에 리포지토리 인터페이스 이름 + Impl 을 적용하는 대신에 사용자 정의 인터페이스 명 + Impl 방식도 지원한다.
 *
 * 사용자 정의 구현 클래스 기존 방식
 *  네이밍 룰:
 *      MemberRepository + Impl
 *  규칙:
 *      리포지토리 인터페이스 이름 + Impl
 *      실제 스프링 DATA JPA(JpaRepository) 를 상속받은 인터페이스 의 이름
 *  스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMembersCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
