package org.bookwoori.core.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "member")
@Getter
@Log4j2
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long memberId;

    @Column(name = "kakao_id", updatable = false, unique = true)
    @NotNull
    private Long kakaoId;

    @Column(name = "nickname", unique = true)
    @NotNull
    private String nickname;

    @Column(name = "profile_image", columnDefinition = "TEXT")
    private String profileImg;

    @Column(name = "background_image", columnDefinition = "TEXT")
    private String backgroundImg;

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

    @Column(name = "total_height")
    @NotNull
    private double totalHeight;

    @Builder
    public Member(Long kakaoId, String nickname, String profileImg) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.backgroundImg = null;
        this.grade = Grade.Dongsan;
        this.status = Status.ACTIVE;
        this.totalPage = 0;
        this.totalHeight = 0;
    }

    public void deleteMember() {
        this.nickname = "(알 수 없음)";
        this.profileImg = null; // 추후 수정
        this.backgroundImg = null;
        this.status = Status.INACTIVE;
    }

    public double updateHeight(double xp) {
        this.totalHeight += xp;
        Grade newGrade = Grade.findGradeByHeight(this.totalHeight);
        if (!this.grade.equals(newGrade)) {
            this.grade = newGrade;
        }
        return this.totalHeight;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImg(String url) {
        this.profileImg = url;
    }

    public void updateBackgrounImg(String url) {
        this.backgroundImg = url;
    }
}
