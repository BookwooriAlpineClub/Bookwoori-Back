package org.bookwoori.core.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long memberId;

    @Column(name = "kakao_member_id", updatable = false, unique = true)
    @NotNull
    private Long kakaoMemberId;

    @Column(name = "nickname", unique = true)
    @NotNull
    private String nickname;

    @Column(name = "profile_image", columnDefinition = "TEXT")
    private String profileImg;

    @Column(name = "grade")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_page")
    @NotNull
    private int totalPage;

    @Builder
    public Member(Long kakaoMemberId, String nickname, String profileImg) {
        this.kakaoMemberId = kakaoMemberId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.grade = Grade.Dongsan;
        this.status = Status.ACTIVE;
        this.totalPage = 0;
    }
}