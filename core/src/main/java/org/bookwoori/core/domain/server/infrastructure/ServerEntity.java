package org.bookwoori.core.domain.server.infrastructure;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.serverMember.infrastructure.ServerMemberEntity;
import org.bookwoori.core.global.BaseTimeEntity;

@Entity
@Table(name = "server")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ServerEntity extends BaseTimeEntity {

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
    private List<CategoryEntity> categories = new ArrayList<>();

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL)
    private List<ClimbingEntity> climbingChannels = new ArrayList<>();

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL)
    private List<ServerMemberEntity> members = new ArrayList<>();

    public void updateInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateServerImg(String url) {
        this.serverImg = url;
    }
}
