package org.bookwoori.core.domain.server.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "server")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Server extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id", updatable = false)
    private Long serverId;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "server_image", columnDefinition = "TEXT")
    private String serverImg;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

}
