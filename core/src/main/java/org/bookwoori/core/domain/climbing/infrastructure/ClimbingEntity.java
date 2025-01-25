package org.bookwoori.core.domain.climbing.infrastructure;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberEntity;
import org.bookwoori.core.domain.reviewEmoji.infrastructure.ReviewEmojiEntity;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "climbing")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClimbingEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "climbing_id", updatable = false)
    private Long climbingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", updatable = false)
    private ServerEntity server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", updatable = false)
    private BookEntity book;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ClimbingStatus status;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @OneToMany(mappedBy = "climbing", cascade = CascadeType.ALL)
    private List<ClimbingMemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "climbing", cascade = CascadeType.ALL)
    private List<ReviewEmojiEntity> emojis = new ArrayList<>();

    @Builder
    public ClimbingEntity(Long climbingId, ServerEntity server, BookEntity book, String name,
        String description,
        LocalDate startDate, LocalDate endDate, ClimbingStatus status) {
        this.climbingId = climbingId;
        this.server = server;
        this.book = book;
        this.status = status;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateClimbing(String name, String description, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.endDate = endDate;
    }

    public void updateStatus(ClimbingStatus climbingStatus) {
        this.status = climbingStatus;
    }
}
