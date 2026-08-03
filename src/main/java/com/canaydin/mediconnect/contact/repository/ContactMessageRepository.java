package com.canaydin.mediconnect.contact.repository;

import com.canaydin.mediconnect.contact.entity.ContactMessage;
import com.canaydin.mediconnect.contact.enums.ContactMessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ContactMessageRepository
        extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findByStatus(ContactMessageStatus status);

    List<ContactMessage> findByStatus(
            ContactMessageStatus status,
            Sort sort
    );

    Page<ContactMessage> findByStatus(
            ContactMessageStatus status,
            Pageable pageable
    );

    @Modifying
    @Query("""
            update ContactMessage c
            set c.status = :status,
                c.updatedAt = :updatedAt,
                c.updatedBy = :updatedBy
            where c.id = :id
            """)
    int updateStatusById(
            @Param("id") Long id,
            @Param("status") ContactMessageStatus status,
            @Param("updatedAt") Instant updatedAt,
            @Param("updatedBy") String updatedBy
    );
}