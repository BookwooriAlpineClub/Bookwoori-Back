package org.bookwoori.core.domain.channel.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Channel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", updatable = false)
    private Long channelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "type", updatable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "before_channel_id")
    private Channel beforeNode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "next_channel_id")
    private Channel nextNode;

    public void setBeforeNode(Channel channel) {
        this.beforeNode = channel;
        if (!Objects.isNull(channel)) {
            channel.nextNode = this;
        }
    }

    public void modifyName(String name) {
        this.name = name;
    }

}
