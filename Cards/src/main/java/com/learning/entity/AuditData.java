package com.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditData {

  @CreatedBy
  @Column(name = "created_by",updatable = false)
  String createdBy;

  @CreatedDate
  @Column(updatable = false)
  LocalDateTime createdAt;

  @Column(insertable = false)
  @LastModifiedBy
  String updatedBy;

  @Column(insertable = false)
  @LastModifiedDate
  LocalDateTime updatedAt;
}
