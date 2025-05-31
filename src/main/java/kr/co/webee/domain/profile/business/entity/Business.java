package kr.co.webee.domain.profile.business.entity;

import jakarta.persistence.*;
import kr.co.webee.domain.common.BaseTimeEntity;
import kr.co.webee.domain.profile.crop.entity.Coordinates;
import kr.co.webee.domain.profile.crop.entity.Location;
import kr.co.webee.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Objects;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Business extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String representativeName;

    @Column(nullable = false)
    private LocalDate commencementDate;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private Location businessLocation;

    @Column(nullable = false)
    private String phoneNumber;

    private String onlineStoreUrl;

    private String kakaoChatUrl;

    private String businessCertImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Business(String registrationNumber, String representativeName, LocalDate commencementDate,
                    String companyName, Location businessLocation, User user, String phoneNumber,
                    String onlineStoreUrl, String kakaoChatUrl, String businessCertImageUrl) {
        if (!StringUtils.hasText(registrationNumber)) {
            throw new IllegalArgumentException("registrationNumber는 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(representativeName)) {
            throw new IllegalArgumentException("representativeName은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(companyName)) {
            throw new IllegalArgumentException("companyName은 null이거나 빈 문자열이 될 수 없습니다.");
        }
        if (!StringUtils.hasText(phoneNumber)) {
            throw new IllegalArgumentException("phoneNumber는 null이거나 빈 문자열이 될 수 없습니다.");
        }

        this.registrationNumber = registrationNumber;
        this.representativeName = representativeName;
        this.commencementDate = Objects.requireNonNull(commencementDate, "commencementDate는 null이 될 수 없습니다.");
        this.companyName = companyName;
        this.businessLocation = Objects.requireNonNull(businessLocation, "businessLocation은 null이 될 수 없습니다.");
        this.phoneNumber = phoneNumber;
        this.onlineStoreUrl = onlineStoreUrl;
        this.kakaoChatUrl = kakaoChatUrl;
        this.businessCertImageUrl = businessCertImageUrl;
        this.user = Objects.requireNonNull(user, "user는 null이 될 수 없습니다.");
    }

    public String getBusinessAddress() {
        return businessLocation.getAddress();
    }

    public Coordinates getAddressCoordinates() {
        return businessLocation.getCoordinates();
    }
}
