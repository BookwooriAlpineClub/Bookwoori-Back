package org.bookwoori.core.domain.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.dto.response.BookDetailResponseDto;
import org.bookwoori.core.domain.book.dto.response.BookResponseDto;
import org.bookwoori.core.domain.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final String ALADIN_API_URL_SEARCH = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String ALADIN_API_URL_LOOKUP = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private static final String TTB_KEY = "ttboesnimnos1216001"; // 알라딘 API Key
    private final BookRepository bookRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

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
                    String title = item.path("title").asText();
                    String author = item.path("author").asText();
                    String publisher = item.path("publisher").asText();
                    String pubYear = item.path("pubDate").asText().split("-")[0];
                    String isbn13 = item.path("isbn13").asText();
                    String cover = item.path("cover").asText();
                    BookResponseDto bookResponseDto = new BookResponseDto(title, author, publisher,
                        pubYear,
                        isbn13, cover);
                    bookList.add(bookResponseDto);
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

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode item = root.path("item").get(0);

            if (item != null) {
                String title = item.path("title").asText();
                String author = item.path("author").asText();
                String publisher = item.path("publisher").asText();
                String pubDate = item.path("pubDate").asText();
                Long itemPage = item.path("subInfo").path("itemPage").asLong();
                String description = item.path("description").asText();
                String cover = item.path("cover").asText();

                return new BookDetailResponseDto(title, author, publisher, pubDate, itemPage,
                    description,
                    isbn13, cover);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // 데이터가 없거나 오류 발생 시 null 반환
    }


}