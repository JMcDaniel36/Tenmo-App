package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, transfer.getTransferId(), transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        String sql = "SELECT transfer(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "FROM transfers " +
                "JOIN accounts ON accounts.account_id = transfer.account_from " +
                "WHERE user_id = ?";
        SqlRowSet results  = jdbcTemplate.queryForRowSet(sql, userId);
        List<Transfer> transfers = new ArrayList<>();
        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT transfer(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "FROM transfers " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = null;
        if(results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        String sql = "SELECT transfer(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "FROM transfers";
        SqlRowSet results  = jdbcTemplate.queryForRowSet(sql);
        List<Transfer> transfers = new ArrayList<>();
        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfers " +
                "SET transfer_status_id = ?" +
                "WHERE transfer_id = ?";
        jdbcTemplate.update(sql);
    }

    private Transfer mapRowToTransfer(SqlRowSet results){
        Transfer t = new Transfer();
        t.setTransferId(results.getInt("transfer_id"));
        t.setTransferTypeId(results.getInt("transfer_type_id"));
        t.setTransferStatusId(results.getInt("transfer_status_id"));
        t.setAccountFrom(results.getInt("account_from"));
        t.setAccountTo(results.getInt("account_to"));
        t.setAmount(results.getBigDecimal("amount"));
        return t;
    }
}
