package study.datajpa.repository.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.criteria.*;

public class MemberSpec {
    public static Specification<Member> username(String username) {
        return (Specification<Member>) (root, query, builder) -> {
            return builder.equal(root.get("username"), username);
        };
    }

    public static Specification<Member> teamName(String teamName) {
        return (root, query, builder) -> {
            if (StringUtils.hasText(teamName)) {
                return null;
            }
            Join<Member, Team> t = root.join("team", JoinType.INNER);//회원과 조인
            return builder.equal(t.get("name"), teamName);
        };
    }
}