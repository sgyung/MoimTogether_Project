package com.project.moimtogether.repository;

import com.project.moimtogether.domain.meeting.Meeting;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Override
    Optional<Meeting> findById(Long meetingId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 쓰기 락
    @Query("SELECT m FROM Meeting m WHERE m.meetingId = :meetingId")
    Optional<Meeting> findByIdWithLock(@Param("meetingId") Long meetingId);

    @Override
    List<Meeting> findAll();
}
