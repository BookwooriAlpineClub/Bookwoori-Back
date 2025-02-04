package org.bookwoori.core.domain.record.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;

@Entity
@Table(name = "record")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", updatable = false)
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", updatable = false)
    @NotNull
    @JsonIgnore
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @NotNull
    @JsonIgnore
    private Member member;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ReadingStatus status;

    @Column(name = "star_review")
    private int starReview;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "current_page")
    private int currentPage;

    @Column(name = "max_page")
    private int maxPage;


    public void updateRecord(Record record) {
        this.status = record.status;
        this.starReview = record.starReview;
        this.startDate = record.startDate;
        this.currentPage = record.currentPage;
    }

}
