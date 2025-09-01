package com.hdfcbank.neftiladmiproccessor.repository;

import com.hdfcbank.neftiladmiproccessor.model.Admi004Tracker;
import com.hdfcbank.neftiladmiproccessor.model.MsgEventTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NilRepositoryTest {
    @Mock
    NamedParameterJdbcTemplate jdbc;
    @InjectMocks
    NilRepository repo;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void testUpdateAdmiTracker() {
        Admi004Tracker tracker = Admi004Tracker.builder()
                .msgId("id").msgType("type").originalReq("req").target("tgt")
                .version(BigDecimal.ONE).batchCreationDate(LocalDate.now())
                .batchTimestamp(LocalDateTime.now()).status("status").build();
        repo.updateAdmiTracker(tracker);
        // No update call in code, so just ensure no exception
    }

    @Test
    void testInsertAdmi004Tracker() {
        Admi004Tracker tracker = Admi004Tracker.builder()
                .msgId("id").msgType("type").originalReq("req").target("tgt")
                .status("status").replayCount(1).version(BigDecimal.ONE)
                .batchCreationDate(LocalDate.now()).batchTimestamp(LocalDateTime.now())
                .build();
        repo.insertAdmi004Tracker(tracker);
        // No update call in code, so just ensure no exception
    }

    @Test
    void testFindByMsgId_found() {
        MsgEventTracker tracker = new MsgEventTracker();
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(org.springframework.jdbc.core.RowMapper.class)))
                .thenReturn(List.of(tracker));
        MsgEventTracker result = repo.findByMsgId("id");
        assertNotNull(result);
    }

    @Test
    void testFindByMsgId_notFound() {
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(org.springframework.jdbc.core.RowMapper.class)))
                .thenReturn(Collections.emptyList());
        MsgEventTracker result = repo.findByMsgId("id");
        assertNull(result);
    }

    @Test
    void testSaveDuplicateEntry_update() {
        MsgEventTracker tracker = new MsgEventTracker();
        tracker.setMsgId("id");
        tracker.setSource("src");
        tracker.setTarget("tgt");
        tracker.setFlowType("flow");
        tracker.setMsgType("type");
        tracker.setOriginalReq("req");
        when(jdbc.queryForObject(anyString(), any(MapSqlParameterSource.class), eq(BigDecimal.class)))
                .thenReturn(BigDecimal.ONE);
        when(jdbc.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);
        repo.saveDuplicateEntry(tracker);
        verify(jdbc).update(anyString(), any(MapSqlParameterSource.class));
    }

    @Test
    void testSaveDuplicateEntry_insert() {
        MsgEventTracker tracker = new MsgEventTracker();
        tracker.setMsgId("id");
        tracker.setSource("src");
        tracker.setTarget("tgt");
        tracker.setFlowType("flow");
        tracker.setMsgType("type");
        tracker.setOriginalReq("req");
        when(jdbc.queryForObject(anyString(), any(MapSqlParameterSource.class), eq(BigDecimal.class)))
                .thenReturn(null);
        when(jdbc.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);
        repo.saveDuplicateEntry(tracker);
        verify(jdbc).update(anyString(), any(MapSqlParameterSource.class));
    }
}

