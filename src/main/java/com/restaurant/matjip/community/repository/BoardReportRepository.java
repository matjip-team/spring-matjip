package com.restaurant.matjip.community.repository;

import com.restaurant.matjip.community.domain.BoardReport;
import com.restaurant.matjip.community.domain.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {

    boolean existsByReporterIdAndBoardIdAndCommentIsNull(Long reporterId, Long boardId);

    boolean existsByReporterIdAndCommentId(Long reporterId, Long commentId);

    Page<BoardReport> findByStatusOrderByIdDesc(ReportStatus status, Pageable pageable);

    Page<BoardReport> findAllByOrderByIdDesc(Pageable pageable);
}
