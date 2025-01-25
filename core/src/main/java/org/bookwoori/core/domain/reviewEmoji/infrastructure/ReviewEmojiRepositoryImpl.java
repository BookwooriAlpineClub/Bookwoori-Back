package org.bookwoori.core.domain.reviewEmoji.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.reviewEmoji.repository.ReviewEmojiRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewEmojiRepositoryImpl implements ReviewEmojiRepository {

    private final ReviewEmojiJpaRepository reviewEmojiJpaRepository;
}
