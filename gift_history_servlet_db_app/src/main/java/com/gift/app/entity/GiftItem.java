package com.gift.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 頂き物の情報を保持するクラス.
 */
@Entity
@Table(name = "giftItems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Size(max = 100, message = "いただいた品物は100文字以内で入力してください")
  @Column(name = "what", nullable = false, columnDefinition = "VARCHAR(100) DEFAULT '未回答'")
  private String what;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "whenis")
  private LocalDate whenis;

  @Size(max = 100, message = "贈り主の名前は100文字以内で入力してください")
  @Column(name = "who", nullable = false, columnDefinition = "VARCHAR(100) DEFAULT '未回答'")
  private String who;

  @Size(max = 100, message = "お祝いの名目は100文字以内で入力してください")
  @Column(name = "why", nullable = false, columnDefinition = "VARCHAR(100) DEFAULT '未回答'")
  private String why;

  @Size(max = 10, message = "おおよその金額は10文字以内で入力してください")
  @Column(name = "howMuch", nullable = false, columnDefinition = "VARCHAR(10) DEFAULT '未回答'")
  private String howMuch;

  @Column(name = "needReturn", nullable = false)
  private String needReturn = "未回答";

  @Column(name = "hasGaveReturn", nullable = false)
  private String hasGaveReturn = "未返礼";
}
