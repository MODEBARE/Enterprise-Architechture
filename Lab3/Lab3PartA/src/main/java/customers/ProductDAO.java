package customers;

import java.util.List;

public interface ProductDAO {
    void save(Product product);
    Product findByProductNumber(int productNumber);
    List<Product> getAllProducts();
    List<Product> findByProductName(String name);
    void removeProduct(int productNumber);
}
