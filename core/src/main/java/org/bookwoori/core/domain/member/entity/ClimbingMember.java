package org.bookwoori.core.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;

@Entity
@Table(name = "climbing_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClimbingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "climbing_member_id", updatable = false)
    private Long climbingMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "climbing_id", updatable = false)
    @NotNull
    private Climbing climbing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @NotNull
    private Member member;

    @Column(name = "has_shared")
    @NotNull
    private boolean hasShared;

    @Column(name = "memo")
    private String memo;
}
