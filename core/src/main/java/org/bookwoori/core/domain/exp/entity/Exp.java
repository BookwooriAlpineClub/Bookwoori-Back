package org.bookwoori.core.domain.exp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;

@Entity
@Table(name = "exp")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Exp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "expLog_id", updatable = false)
  private Long expLogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", updatable = false)
  @NotNull
  @JsonIgnore
  private Member member;

  @Column(name = "height")
  private double height;

  @Column(name = "amount")
  private double amount;

  @Column(name = "type")
  private ExpType expType;

}
