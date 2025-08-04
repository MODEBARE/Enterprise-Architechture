package customers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
    private Connection conn;

    public ProductDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Product product) {
        try {
            // Save supplier first
            String supplierSql = "INSERT INTO supplier (name, phone) VALUES (?, ?)";
            PreparedStatement supplierStmt = conn.prepareStatement(supplierSql, Statement.RETURN_GENERATED_KEYS);
            supplierStmt.setString(1, product.getSupplier().getName());
            supplierStmt.setString(2, product.getSupplier().getPhone());
            supplierStmt.executeUpdate();

            ResultSet generatedKeys = supplierStmt.getGeneratedKeys();
            int supplierId = -1;
            if (generatedKeys.next()) {
                supplierId = generatedKeys.getInt(1);
            }

            // Save product with supplierId
            String sql = "INSERT INTO product (productNumber, name, price, supplierId) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, product.getProductNumber());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, supplierId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product findByProductNumber(int productNumber) {
        String sql = "SELECT p.*, s.supplierId, s.name AS sname, s.phone AS sphone " +
                "FROM product p JOIN supplier s ON p.supplierId = s.supplierId " +
                "WHERE p.productNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplierId"),
                        rs.getString("sname"),
                        rs.getString("sphone")
                );

                Product product = new Product(
                        rs.getInt("productNumber"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
                product.setSupplier(supplier);
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, s.supplierId, s.name AS sname, s.phone AS sphone " +
                "FROM product p JOIN supplier s ON p.supplierId = s.supplierId";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplierId"),
                        rs.getString("sname"),
                        rs.getString("sphone")
                );

                Product product = new Product(
                        rs.getInt("productNumber"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
                product.setSupplier(supplier);
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> findByProductName(String name) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, s.supplierId, s.name AS sname, s.phone AS sphone " +
                "FROM product p JOIN supplier s ON p.supplierId = s.supplierId " +
                "WHERE p.name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplierId"),
                        rs.getString("sname"),
                        rs.getString("sphone")
                );

                Product product = new Product(
                        rs.getInt("productNumber"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
                product.setSupplier(supplier);
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void removeProduct(int productNumber) {
        String sql = "DELETE FROM product WHERE productNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
