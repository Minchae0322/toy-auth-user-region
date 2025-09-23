package com.example.toyauth.app.common.enumuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@NoArgsConstructor
public enum StorageType {

  LOCAL("LOCAL"), // 서버 로컬 디렉토리;

  ;
  private String title;
}
