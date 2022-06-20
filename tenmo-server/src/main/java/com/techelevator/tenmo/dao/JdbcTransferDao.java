package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public List<TransferHistory> getTransfersByUserId(int userId) {
        String sql = "SELECT tr.transfer_id\n" +
                "\t , acFrom.user_id FromUserId\n" +
                "\t , acTo.user_id ToUserId\n" +
                "\t , touFrom.username FromUsername\n" +
                "\t , touTo.username ToUsername\n" +
                "\t , tr.amount FROM tenmo_user tou1\n" +
                "INNER JOIN account ac1\n" +
                "\ton ac1.user_id = tou1.user_id\n" +
                "INNER JOIN transfer tr\n" +
                "\ton tr.account_from = ac1.account_id OR tr.account_to = ac1.account_id\n" +
                "INNER JOIN account acFrom\n" +
                "\tON acFrom.account_id = tr.account_from\n" +
                "INNER JOIN account acTo\n" +
                "\tON acTo.account_id = tr.account_to\n" +
                "INNER JOIN tenmo_user touFrom\n" +
                "\tON touFrom.user_id = acFrom.user_id\n" +
                "INNER JOIN tenmo_user touTo\n" +
                "\tON touTo.user_id = acTo.user_id\n" +
                "WHERE tou1.user_id = ?";
        SqlRowSet results  = jdbcTemplate.queryForRowSet(sql, userId);
        List<TransferHistory> transfers = new ArrayList<TransferHistory>();
        while(results.next()) {
            transfers.add(mapRowToTransferHistory(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
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
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount" +
                "FROM transfer";
        SqlRowSet results  = jdbcTemplate.queryForRowSet(sql);
        List<Transfer> transfers = new ArrayList<>();
        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer " +
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

    private TransferHistory mapRowToTransferHistory(SqlRowSet results){
        TransferHistory t = new TransferHistory();
        t.setTransferId(results.getInt("transfer_id"));
        t.setUserFrom(results.getInt("FromUserId"));
        t.setUserTo(results.getInt("ToUserId"));
        t.setFromUsername(results.getString("FromUsername"));
        t.setToUsername(results.getString("ToUsername"));
        t.setAmount(results.getBigDecimal("amount"));
        return t;
    }
}
