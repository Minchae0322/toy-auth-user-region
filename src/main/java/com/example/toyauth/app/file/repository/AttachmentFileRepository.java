package com.example.toyauth.app.file.repository;

import com.example.toyauth.app.file.domain.AttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentFileRepository extends JpaRepository<AttachmentFile, Long> {

}
