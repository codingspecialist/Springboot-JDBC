package shop.mtcoding.jdbcstudy.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class CustomerRepository {
    private DataSource dataSource;

    public CustomerRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "INSERT INTO customer (name, tel) VALUES (?, ?)";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getTel());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void update(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE customer SET name = ?, tel = ? WHERE id = ?";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getTel());
            statement.setLong(3, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void delete(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM customer WHERE id = ?";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    public Customer findById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM customer WHERE id = ?";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                return mapper(rs);
            } else {
                throw new RuntimeException("DB warning : 해당 id의 고객이 없습니다");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            }catch (SQLException e){
                log.error(e.getMessage());
            }
        }
    }

    public List<Customer> findAll(int page) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            final int row = 2;
            String sql = "SELECT * FROM customer limit ?, ?";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, page * row);
            statement.setInt(2, row);
            rs = statement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer c = mapper(rs);
                customers.add(c);
            }
            return customers;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            }catch (SQLException e){
                log.error(e.getMessage());
            }
        }
    }

    // Object Relational Mapping
    public Customer mapper(ResultSet rs) throws SQLException {
        System.out.println("mapper 실행");
        return new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("tel"));
    }
}
