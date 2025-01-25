package org.bookwoori.core.domain.category.infrastructure;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.channel.infrastructure.ChannelEntity;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", updatable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    @Column(name = "name")
    @NotNull
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "before_category_id")
    private CategoryEntity beforeNode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "next_category_id")
    private CategoryEntity nextNode;

    @Column(name = "is_default")
    @NotNull
    private boolean isDefault;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ChannelEntity> channels = new ArrayList<>();

    public void setBeforeNode(CategoryEntity category) {
        this.beforeNode = category;
        if (!Objects.isNull(category)) {
            category.setNextNode(this);
        }
    }

    public void setNextNode(CategoryEntity category) {
        this.nextNode = category;
    }

    public void connectBeforeAndAfterNodes() {
        CategoryEntity beforeCategory = this.beforeNode;
        CategoryEntity nextCategory = this.nextNode;

        if (nextCategory != null) {
            nextCategory.setBeforeNode(beforeCategory);
        } else {
            if (beforeCategory != null) {
                beforeCategory.setNextNode(null);
            }
        }

        this.beforeNode = null;
        this.nextNode = null;
    }

    public void disconnect() {
        if (this.nextNode != null) {
            this.nextNode.setBeforeNode(null);
        }
        this.nextNode = null;
    }

    public void modifyName(String name) {
        this.name = name;
    }
}
