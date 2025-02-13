package org.bookwoori.notification.domain.repository;


import org.bookwoori.notification.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findBymemberId(Long memberId);

    @Query(nativeQuery = true, value = "SELECT * FROM Device as d WHERE d.memberId IN (:memberIds)")
    List<Device> findByMemberIdList(@Param("memberIds") List<Long> memberIds);
}
