package org.bookwoori.core.domain.climbingMember.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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

    public ClimbingMember(Member member, Climbing climbing, ClimbingRole role, boolean hasShared, String memo) {
        this.member = member;
        this.climbing = climbing;
        this.role = role;
        this.hasShared = hasShared;
        this.memo = memo;
    }
}
