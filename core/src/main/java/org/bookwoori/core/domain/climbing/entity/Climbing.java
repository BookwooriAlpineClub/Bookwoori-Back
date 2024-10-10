package org.bookwoori.core.domain.climbing.entity;

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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "climbing")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Climbing extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "climbing_id", updatable = false)
    private Long climbingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", updatable = false)
    private Server server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", updatable = false)
    private Book book;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ClimbingStatus status;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;
}
