package uz.pdp.oauthexample.entity.template;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 19:49
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbsAudit {

    //qachon yaratildi
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    //qachon oxirgigi marta o'zgardi
    @UpdateTimestamp
    private Timestamp updatedAt;

    //kim yaratdi
    @CreatedBy
    @Column(updatable = false)
    private Long createdById;

    //kim oxirgi marta o'zgartirdi
    @LastModifiedBy
    private Long updatedById;

}
