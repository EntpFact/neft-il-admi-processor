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
    void testFindByMsgId_found() throws Exception {
        // Mock ResultSet to simulate DB row
        java.sql.ResultSet rs = mock(java.sql.ResultSet.class);
        when(rs.getString("msg_id")).thenReturn("id");
        when(rs.getString("source")).thenReturn("SRC");
        when(rs.getString("target")).thenReturn("TGT");
        when(rs.getString("flow_type")).thenReturn("FLOW");
        when(rs.getString("msg_type")).thenReturn("TYPE");
        when(rs.getString("original_req")).thenReturn("REQ");
        when(rs.getBigDecimal("original_req_count")).thenReturn(new java.math.BigDecimal("2"));
        when(rs.getBigDecimal("consolidate_amt")).thenReturn(new java.math.BigDecimal("100.50"));
        when(rs.getString("intermediate_req")).thenReturn("INTREQ");
        when(rs.getBigDecimal("intemdiate_count")).thenReturn(new java.math.BigDecimal("3"));
        when(rs.getString("status")).thenReturn("DISPATCHED");
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        when(rs.getObject(eq("created_time"), eq(java.time.LocalDateTime.class))).thenReturn(now);
        when(rs.getObject(eq("modified_timestamp"), eq(java.time.LocalDateTime.class))).thenReturn(now);

        // Capture the RowMapper and invoke it manually
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(org.springframework.jdbc.core.RowMapper.class)))
                .thenAnswer(invocation -> {
                    org.springframework.jdbc.core.RowMapper<MsgEventTracker> rowMapper = invocation.getArgument(2);
                    MsgEventTracker tracker = rowMapper.mapRow(rs, 0);
                    return List.of(tracker);
                });

        MsgEventTracker result = repo.findByMsgId("id");
        assertNotNull(result);
        assertEquals("id", result.getMsgId());
        assertEquals("SRC", result.getSource());
        assertEquals("TGT", result.getTarget());
        assertEquals("FLOW", result.getFlowType());
        assertEquals("TYPE", result.getMsgType());
        assertEquals("REQ", result.getOriginalReq());
        assertEquals(new java.math.BigDecimal("2"), result.getOriginalReqCount());
        assertEquals(new java.math.BigDecimal("100.50"), result.getConsolidateAmt());
        assertEquals("INTREQ", result.getIntermediateReq());
        assertEquals(new java.math.BigDecimal("3"), result.getIntemdiateCount());
        assertEquals("DISPATCHED", result.getStatus());
        assertEquals(now, result.getCreatedTime());
        assertEquals(now, result.getModifiedTimestamp());
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
