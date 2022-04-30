package com.example.eyagi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter// get 함수를 자동 생성합니다.
@MappedSuperclass // 멤버 변수가 컬럼이 되도록 합니다.
@EntityListeners(AuditingEntityListener.class) // 변경되었을 때 자동으로 기록합니다.
public abstract class Timestamped {

    @CreatedDate // 생성일자임을 나타냅니다.
    @JsonFormat(pattern = "yyyy.MM.dd")
    private String createdAt;

    @LastModifiedDate // 마지막 수정일자임을 나타냅니다.
    @JsonFormat(pattern = "yyyy.MM.dd")
    private String modifiedAt;

}
