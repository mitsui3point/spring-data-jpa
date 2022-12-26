package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 두가지 필드만 필요한 경우가 있다.
 * 이를 위해 BaseTimeEntity, BaseEntity class 분리함.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)//이벤트 기반으로 동작한다.
public class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
