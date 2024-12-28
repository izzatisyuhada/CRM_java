package main.java.com.crm.dao;

import com.crm.model.ContactHistory;
import com.crm.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactHistoryDAO {
    public void addContactHistory(ContactHistory contact) throws SQLException {
        String sql = "INSERT INTO contact_history (customer_id, contact_date, contact_type, notes, created_by) " +
                    "VALUES (?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, contact.getCustomerId());
            pstmt.setTimestamp(2, Timestamp.valueOf(contact.getContactDate()));
            pstmt.setString(3, contact.getContactType());
            pstmt.setString(4, contact.getNotes());
            pstmt.setLong(5, contact.getCreatedBy());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getLong(1));
                }
            }
        }
    }
    
    public List<ContactHistory> getContactHistoryForCustomer(Long customerId) throws SQLException {
        List<ContactHistory> history = new ArrayList<>();
        String sql = "SELECT * FROM contact_history WHERE customer_id = ? ORDER BY contact_date DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ContactHistory contact = new ContactHistory(
                    rs.getLong("customer_id"),
                    rs.getString("contact_type"),
                    rs.getString("notes"),
                    rs.getLong("created_by")
                );
                contact.setId(rs.getLong("id"));
                contact.setContactDate(rs.getTimestamp("contact_date").toLocalDateTime());
                contact.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                history.add(contact);
            }
        }
        return history;
    }
}