package com.gift.app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報を保持するクラス.
 */
@Entity
@Table(name = "gift_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  /** ユーザーID. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  /** ユーザー名. */
  @NotBlank
  @Size(max = 50)
  @Column(name = "name", nullable = false)
  private String name;

  /** パスワード. */
  @NotBlank
  @Size(max = 100)
  @Column(name = "password", nullable = false)
  private String password;
}
