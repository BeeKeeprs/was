package kr.co.webee.domain.bee.diagnosis;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BeeDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String treatment;

    @Builder
    public BeeDisease(String name, String description, String treatment) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("descriptione은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(treatment)) {
            throw new IllegalArgumentException("treatment는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.name = name;
        this.description = description;
        this.treatment = treatment;
    }
}
