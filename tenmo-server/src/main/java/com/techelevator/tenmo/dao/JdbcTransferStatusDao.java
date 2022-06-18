package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcTransferStatusDao implements TransferStatusDao {
    private JdbcTemplate jdbcTemplate;



    @Override
    public TransferStatus getTransferStatusById(int transferStatusId) {
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status " +
                "WHERE transfer_status_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        TransferStatus transferStatus = null;
        if(result.next()) {
            int transferStatusNewId = result.getInt("transfer_status_id");
            String transferStatusNewDesc = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(transferStatusNewId, transferStatusNewDesc);

        }
        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusByDesc(String description) {
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status " +
                "WHERE transfer_status_desc = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);
        TransferStatus transferStatus = null;
        if (result.next()) {
            int transferStatusIDAgain = result.getInt("transfer_status_id");
            String transferStatusDescAgain = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(transferStatusIDAgain, transferStatusDescAgain);

        }

        return transferStatus;
    }
}
