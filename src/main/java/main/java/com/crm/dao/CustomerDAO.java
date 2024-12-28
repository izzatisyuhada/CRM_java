package main.java.com.crm.dao;

import com.crm.model.Customer;
import com.crm.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, company, last_contact, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getCompany());
            pstmt.setTimestamp(6, Timestamp.valueOf(customer.getLastContact()));
            pstmt.setString(7, customer.getStatus());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getLong(1));
                }
            }
        }
    }
    
    public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone=?, " +
                    "company=?, last_contact=?, status=?, updated_at=NOW() WHERE id=?";
                    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getCompany());
            pstmt.setTimestamp(6, Timestamp.valueOf(customer.getLastContact()));
            pstmt.setString(7, customer.getStatus());
            pstmt.setLong(8, customer.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteCustomer(Long id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public Customer getCustomerById(Long id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id=?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createCustomerFromResultSet(rs);
            }
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY last_name, first_name";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(createCustomerFromResultSet(rs));
            }
        }
        return customers;
    }
    
    public List<Customer> searchCustomers(String searchTerm) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE " +
                    "LOWER(first_name) LIKE ? OR " +
                    "LOWER(last_name) LIKE ? OR " +
                    "LOWER(email) LIKE ? OR " +
                    "LOWER(company) LIKE ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String term = "%" + searchTerm.toLowerCase() + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);
            pstmt.setString(3, term);
            pstmt.setString(4, term);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                customers.add(createCustomerFromResultSet(rs));
            }
        }
        return customers;
    }
    
    private Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("company")
        );
        customer.setId(rs.getLong("id"));
        customer.setLastContact(rs.getTimestamp("last_contact").toLocalDateTime());
        customer.setStatus(rs.getString("status"));
        return customer;
    }
}