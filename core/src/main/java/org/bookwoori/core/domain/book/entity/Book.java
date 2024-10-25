package org.bookwoori.core.domain.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", updatable = false)
    private Long bookId;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "writer")
    @NotNull
    private String writer;

    @Column(name = "publisher")
    @NotNull
    private String publisher;

    @Column(name = "page_count")
    @NotNull
    private int pageCount;

    @Column(name = "isbn", updatable = false, unique = true)
    @NotNull
    private String isbn;

    @Column(name = "cover_image", columnDefinition = "TEXT")
    private String coverImg;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder
    public Book(String title, String writer, String publisher, int pageCount, String isbn, String coverImg, String description) {
        this.title = title;
        this.writer = writer;
        this.publisher = publisher;
        this.pageCount = pageCount;
        this.isbn = isbn;
        this.coverImg = coverImg;
        this.description = description;
    }
}
