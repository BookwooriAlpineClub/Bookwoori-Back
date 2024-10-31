package org.bookwoori.core.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.repository.BookRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private String TTB_KEY; // 알라딘 API Key

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

        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode item = root.path("item").get(0);

            if (item != null) {
                return BookDetailResponseDto.from(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // 데이터가 없거나 오류 발생 시 null 반환
    }


}
    private final BookRepository bookRepository;

    public Book getOrCreateBookByIsbn(String isbn) {
        return bookRepository.findByIsbn13(isbn)
            .orElseGet(() -> {
                Book newBook = Book.builder()
                    .title("테스트 제목")
                    .author("테스트 저자")
                    .publisher("테스트 출판사")
                    .pubDate(LocalDate.now())
                    .itemPage(1)
                    .isbn13(isbn)
                    .description("기본 설명")
                    .coverImg("default.jpg")
                    .build();
                return bookRepository.save(newBook);
            });
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
    }

}
