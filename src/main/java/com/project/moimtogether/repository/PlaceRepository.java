package com.project.moimtogether.repository;

import com.project.moimtogether.domain.place.Place;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByPlaceAddress(String address);

    Optional<Place> findById(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Place p where p.id = :id")
    Optional<Place> findForLockById(@Param("id") Long id);
}
