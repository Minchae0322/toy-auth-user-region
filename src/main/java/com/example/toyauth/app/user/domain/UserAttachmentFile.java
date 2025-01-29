package com.example.toyauth.app.user.domain;

import com.example.toyauth.app.file.AttachmentFile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserAttachmentFile.UserAttachmentFilePK.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_user_attch_file")
public class UserAttachmentFile {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attc_file_id")
    private AttachmentFile attachmentFile;

    @Column(name = "ord", nullable = false)
    private Integer ord;

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAttachmentFilePK implements Serializable {
        private Long user;
        private Long attachmentFile;
    }

}
