package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcTransferTypeDao implements TransferTypeDao{
    private JdbcTemplate jdbcTemplate;


    @Override
    public TransferType getTransferTypeById(int transferId) {
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type " +
                "WHERE transfer_type_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        TransferType transferType = null;
        if(results.next()){
            int transferTypeId = results.getInt("transfer_type_id");
            String transferTypeDesc = results.getString("transfer_type_desc");
            transferType = new TransferType(transferTypeId, transferTypeDesc);
        }
        return transferType;
    }

    @Override
    public TransferType getTransferTypeByDesc(String description) {
        String sql =  "SELECT transfer_type_id, transfer_type_desc FROM transfer_type " +
                "WHERE transfer_type_desc = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, description);
        TransferType transferType = null;
        if(results.next()){
            int transferTypeId = results.getInt("transfer_type_id");
            String transferTypeDesc = results.getString("transfer_type_desc");
            transferType = new TransferType(transferTypeId, transferTypeDesc);
        }
        return transferType;
    }
}
