package study.datajpa.repository.custom;

import study.datajpa.entity.Member;

import java.util.List;

//이름 상관없음
public interface MemberRepositoryCustom {
    List<Member> findMembersCustom();
}
