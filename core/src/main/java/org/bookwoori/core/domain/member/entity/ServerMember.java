package org.bookwoori.core.domain.member.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.server.entity.Server;

@Entity
@Table(name = "server_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_member_id", updatable = false)
    private Long serverMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    @NotNull
    private Server server;

    @Column(name = "role")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ServerRole role;

}
