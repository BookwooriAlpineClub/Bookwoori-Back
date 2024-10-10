package org.bookwoori.core.domain.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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

    @Column(name = "isbn", updatable = false)
    @NotNull
    private String isbn;

    @Column(name = "cover_image")
    private String coverImg;

    @Column(name = "description")
    private String description;
}
