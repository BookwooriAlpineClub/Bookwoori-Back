package org.bookwoori.core.domain.channel.infrastructure;

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
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.channel.entity.ChannelType;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChannelEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", updatable = false)
    private Long channelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "type", updatable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "before_channel_id")
    private ChannelEntity beforeNode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "next_channel_id")
    private ChannelEntity nextNode;

    public void setBeforeNode(ChannelEntity channel) {
        this.beforeNode = channel;
        if (!Objects.isNull(channel)) {
            channel.setNextNode(this);
        }
    }

    public void setNextNode(ChannelEntity channel) {
        this.nextNode = channel;
    }

    public void connectBeforeAndNextNodes() {

        ChannelEntity beforeChannel = this.beforeNode;
        ChannelEntity nextChannel = this.nextNode;

        if (nextChannel != null) {
            nextChannel.setBeforeNode(beforeChannel);
        } else {
            if (beforeChannel != null) {
                beforeChannel.setNextNode(null);
            }
        }

        this.beforeNode = null;
        this.nextNode = null;
    }

    public void modifyName(String name) {
        this.name = name;
    }

    public void modifyCategory(CategoryEntity category) {
        if (category != null) {
            category.getChannels().remove(this);
        }
        this.category = category;
    }

}
