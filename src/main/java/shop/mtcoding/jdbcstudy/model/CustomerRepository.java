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
    private Connection connection;

    public CustomerRepository(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        }
    }

    public void save(Customer customer) {
        String sql = "INSERT INTO customer (name, tel) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getTel());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        }
    }

    public void update(Customer customer) {
        String sql = "UPDATE customer SET name = ?, tel = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getTel());
            statement.setLong(3, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        }
    }

    public Customer findById(Long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return mapper(rs);
            }else{
                throw new RuntimeException("DB warning : 해당 id의 고객이 없습니다");
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        }
    }

    public List<Customer> findAll(int page) {
        final int row = 2;
        String sql = "SELECT * FROM customer limit ?, ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, page*row);
            statement.setInt(2, row);
            ResultSet rs = statement.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer c = mapper(rs);
                customers.add(c);
            }
            return customers;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getSQLState());
        }
    }

    // Object Relational Mapping
    public Customer mapper(ResultSet rs) throws SQLException{
        System.out.println("mapper 실행");
        return new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("tel"));
    }


}
