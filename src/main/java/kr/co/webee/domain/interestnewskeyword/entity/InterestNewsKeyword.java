package kr.co.webee.domain.interestnewskeyword.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class InterestNewsKeyword extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String keyword;

    @Builder
    public InterestNewsKeyword(User user, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new IllegalArgumentException("keyword는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
        this.keyword = keyword;
    }
}

