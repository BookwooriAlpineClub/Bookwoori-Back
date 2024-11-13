package org.bookwoori.core.domain.book.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.book.dto.response.BookDetailResponseDto;
import org.bookwoori.core.domain.book.dto.response.BookResponseDto;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Log4j2
public class BookFacade {

    private static final String ALADIN_API_URL_SEARCH = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String ALADIN_API_URL_LOOKUP = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private final BookService bookService;
    @Autowired
    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;
    @Value("${aladin.TTB_KEY}")
    private String TTB_KEY; // 알라딘 API Key

    public Object getBooksByKeyword(String keyword) {
        String url = ALADIN_API_URL_SEARCH +
            "?ttbkey=" + TTB_KEY +
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
            throw new CustomException(ErrorCode.BOOK_NOT_FOUND);
        }

        return bookList;
    }

    public Object getBookByIsbn(String isbn13) {

        String url = ALADIN_API_URL_LOOKUP + "?ttbkey=" + TTB_KEY +
            "&itemIdType=ISBN13" +
            "&ItemId=" + isbn13 +
            "&output=js" +
            "&Version=20131101";

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode item = root.path("item").get(0);

            if (item != null) {
                return BookDetailResponseDto.from(item);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BOOK_NOT_FOUND);
        }

        return null; // 데이터가 없거나 오류 발생 시 null 반환

    }
}
