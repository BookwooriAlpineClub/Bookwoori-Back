package org.bookwoori.core.domain.climbingMember.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;

@Entity
@Table(name = "climbing_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ClimbingRole role;

    public ClimbingMember(Member member, Climbing climbing, ClimbingRole role) {
        this.member = member;
        this.climbing = climbing;
        this.role = role;
        this.hasShared = false;
        this.memo = null;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateRole(ClimbingRole climbingRole) {
        this.role = climbingRole;
    }

    public void updateShared(boolean hasShared) {
        this.hasShared = hasShared;
    }
}
