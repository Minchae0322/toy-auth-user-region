package com.example.toyauth.app.file.domain;

import com.example.toyauth.app.base.BaseEntity;
import com.example.toyauth.app.common.enumuration.FileCode;
import com.example.toyauth.app.common.enumuration.StorageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_attch_file")
public class AttachmentFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attch_file_id", unique = true, nullable = false)
    @Comment("첨부 파일 아이디")
    private Long id;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    @Comment("파일 코드")
    private FileCode fileCode;

    @Column(name = "org_file_nm")
    @Comment("원본 파일 명")
    private String orgFileNm;

    @Column(name = "store_file_nm")
    @Comment("저장 파일 명")
    private String storeFileNm;

    @Column(length = 2000)
    @Comment("파일 url")
    private String fileUrl;

    @Comment("파일 사이즈")
    private Long fileSize;

    @Comment("파일 확장자")
    private String fileExt;

    @Column(name = "file_explain", length = 2000)
    @Comment("파일 설명")
    private String fileExplain;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type", length = 20)
    @Comment("저장소 타입 (LOCAL, S3 등)")
    private StorageType storageType;
}