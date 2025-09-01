package com.hdfcbank.neftiladmiproccessor.repository;


import com.hdfcbank.neftiladmiproccessor.model.Admi004Tracker;
import com.hdfcbank.neftiladmiproccessor.model.MsgEventTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NilRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void updateAdmiTracker(Admi004Tracker tracker) {
        String sql = "UPDATE network_il.admi004_tracker SET " +
                "msg_type = :msgType, " +
                "original_req = :originalReq, " +
                "target = :target, " +
                "version = :version, " +
                "batch_creation_date = :batchCreationDate, " +
                "batch_timestamp = :batchTimestamp, " +
                "modified_timestamp = :modifiedTimestamp, " +
                "status = :status " +
                "WHERE msg_id = :msgId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("msgId", tracker.getMsgId());
        params.addValue("msgType", tracker.getMsgType());
        params.addValue("originalReq", tracker.getOriginalReq());
        params.addValue("target", tracker.getTarget());
        params.addValue("version", tracker.getVersion());
        params.addValue("batchCreationDate", tracker.getBatchCreationDate());
        params.addValue("batchTimestamp", tracker.getBatchTimestamp());
        params.addValue("modifiedTimestamp", LocalDateTime.now());
        params.addValue("status", tracker.getStatus());

    }

    public void insertAdmi004Tracker(Admi004Tracker tracker) {
        String sql = "INSERT INTO network_il.admi004_tracker (" +
                "msg_id, msg_type, original_req, target, status, replay_count, " +
                "version, batch_creation_date, batch_timestamp, created_time, modified_timestamp) " +
                "VALUES (:msgId, :msgType, :originalReq, :target, :status, :replayCount, " +
                ":version, :batchCreationDate, :batchTimestamp, :createdTime, :modifiedTimestamp)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("msgId", tracker.getMsgId())
                .addValue("msgType", tracker.getMsgType())
                .addValue("originalReq", tracker.getOriginalReq())
                .addValue("target", tracker.getTarget())
                .addValue("status", tracker.getStatus())
                .addValue("replayCount", tracker.getReplayCount())
                .addValue("version", tracker.getVersion())
                .addValue("batchCreationDate", tracker.getBatchCreationDate())
                .addValue("batchTimestamp", tracker.getBatchTimestamp())
                .addValue("createdTime", LocalDateTime.now())
                .addValue("modifiedTimestamp", LocalDateTime.now());

    }

    public MsgEventTracker findByMsgId(String msgId) {
        String sql = "SELECT * FROM network_il.msg_event_tracker " +
                "WHERE msg_id = :msgId AND status = 'DISPATCHED' AND version = 1.0";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("msgId", msgId);

        List<MsgEventTracker> result = namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            MsgEventTracker tracker = new MsgEventTracker();
            tracker.setMsgId(rs.getString("msg_id"));
            tracker.setSource(rs.getString("source"));
            tracker.setTarget(rs.getString("target"));
            tracker.setFlowType(rs.getString("flow_type"));
            tracker.setMsgType(rs.getString("msg_type"));
            tracker.setOriginalReq(rs.getString("original_req"));
            tracker.setOriginalReqCount(rs.getBigDecimal("original_req_count"));
            tracker.setConsolidateAmt(rs.getBigDecimal("consolidate_amt"));
            tracker.setIntermediateReq(rs.getString("intermediate_req"));
            tracker.setIntemdiateCount(rs.getBigDecimal("intemdiate_count"));
            tracker.setStatus(rs.getString("status"));
            tracker.setCreatedTime(rs.getObject("created_time", LocalDateTime.class));
            tracker.setModifiedTimestamp(rs.getObject("modified_timestamp", LocalDateTime.class));
            return tracker;
        });

        return result.isEmpty() ? null : result.get(0);
    }

    public void saveDuplicateEntry(MsgEventTracker tracker) {
        String selectSql = "SELECT MAX(version) FROM network_il.msg_dedup_tracker " +
                "WHERE msg_id = :msgId";

        MapSqlParameterSource baseParams = new MapSqlParameterSource();
        baseParams.addValue("msgId", tracker.getMsgId());


        BigDecimal currentVersion = namedParameterJdbcTemplate.queryForObject(
                selectSql, baseParams, BigDecimal.class);

        if (currentVersion != null) {
            // Row exists → update version
            BigDecimal nextVersion = currentVersion.add(BigDecimal.ONE);

            String updateSql = "UPDATE network_il.msg_dedup_tracker SET " +
                    "flow_type = :flowType, msg_type = :msgType, original_req =  :originalReq, " +
                    "version = :version, modified_timestamp = CURRENT_TIMESTAMP " +
                    "WHERE msg_id = :msgId AND source = :source AND target = :target";

            MapSqlParameterSource updateParams = new MapSqlParameterSource();
            updateParams.addValue("msgId", tracker.getMsgId());
            updateParams.addValue("source", tracker.getSource());
            updateParams.addValue("target", tracker.getTarget());
            updateParams.addValue("flowType", tracker.getFlowType());
            updateParams.addValue("msgType", tracker.getMsgType());
            updateParams.addValue("originalReq", tracker.getOriginalReq());
            updateParams.addValue("version", nextVersion);

            namedParameterJdbcTemplate.update(updateSql, updateParams);

        } else {
            // Row does not exist → insert with version = 1
            String insertSql = "INSERT INTO network_il.msg_dedup_tracker " +
                    "(msg_id, source, target, flow_type, msg_type, original_req, version, created_time, modified_timestamp) " +
                    "VALUES (:msgId, :source, :target, :flowType, :msgType,  :originalReq, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

            MapSqlParameterSource insertParams = new MapSqlParameterSource();
            insertParams.addValue("msgId", tracker.getMsgId());
            insertParams.addValue("source", tracker.getSource());
            insertParams.addValue("target", tracker.getTarget());
            insertParams.addValue("flowType", tracker.getFlowType());
            insertParams.addValue("msgType", tracker.getMsgType());
            insertParams.addValue("originalReq", tracker.getOriginalReq());

            namedParameterJdbcTemplate.update(insertSql, insertParams);
        }
    }


}
