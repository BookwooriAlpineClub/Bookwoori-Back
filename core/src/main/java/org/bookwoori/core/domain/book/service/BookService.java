package org.bookwoori.core.domain.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.dto.response.BookDetailResponseDto;
import org.bookwoori.core.domain.book.dto.response.BookResponseDto;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.repository.BookRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private static final String ALADIN_API_URL_SEARCH = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String ALADIN_API_URL_LOOKUP = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private final BookRepository bookRepository;
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;
    @Value("${aladin.TTB_KEY}")
    private String TTB_KEY;

    public List<BookResponseDto> getBooksByKeyword(String keyword) { // 도서 검색
        String url = ALADIN_API_URL_SEARCH + "?ttbkey=" + TTB_KEY +
            "&Query=" + keyword +
            "&QueryType=Keyword" +
            "&MaxResults=100" +
            "&start=1" +
            "&SearchTarget=Book" +
            "&output=js" +
            "&Version=20131101";

        String jsonResponse = restTemplate.getForObject(url, String.class);

        List<BookResponseDto> bookList = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    bookList.add(BookResponseDto.from(item));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }


    public BookDetailResponseDto getBookByIsbn(String isbn13) { // 상세정보 조회
        String url = ALADIN_API_URL_LOOKUP + "?ttbkey=" + TTB_KEY +
            "&itemIdType=ISBN13" +
            "&ItemId=" + isbn13 +
            "&output=js" +
            "&Version=20131101";

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode item = root.path("item").get(0);

            if (item != null) {
                return BookDetailResponseDto.from(item);
            } else {
                throw new CustomException(ErrorCode.BOOK_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.BOOK_NOT_FOUND);
        }
    }


    public Book getOrCreateBookByIsbn(String isbn) {
        Optional<Book> existingBook = bookRepository.findByIsbn13(isbn);
        if (existingBook.isPresent()) {
            return existingBook.get();
        }
        Book newBook = getBookByIsbn(isbn).toEntity();
        return bookRepository.save(newBook);
    }


    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
    }

}
