import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/lenovo/Desktop/shopping_system_test_9/database.db";

    private Admin admin;
    private List<User> users;
    private List<Customer> customers;
    private List<Product> products;
    public void ClearSQLiteDatabase(){
            // SQLite数据库连接URL
            String url = "jdbc:sqlite:C:/Users/lenovo/Desktop/shopping_system_test_9/database.db";

            try {
                // 连接到数据库
                Connection connection = DriverManager.getConnection(url);

                // 创建Statement对象
                Statement statement = connection.createStatement();

                // 删除所有表格的数据
                String deleteDataQuery = "DELETE FROM admin;"; // 将 "tablename" 替换为您的表格名称
                statement.executeUpdate(deleteDataQuery);

                // 关闭Statement和连接
                statement.close();
                connection.close();

                System.out.println("数据库已清空。");

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void initializeData1() {
        //ClearSQLiteDatabase();
        createTables(); // 创建缺少的表

        if (isDatabaseEmpty()) {
            // 数据库为空，写入初始信息
            insertData();
        }
        readData();
    }

    private boolean isDatabaseEmpty() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // 查询admin表格的记录数
            String sql = "SELECT COUNT(*) FROM admin;";
            ResultSet resultSet = stmt.executeQuery(sql);
            int count = resultSet.getInt(1);
            return count == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // 创建admin表格
            String createAdminTableSql = "CREATE TABLE IF NOT EXISTS admin (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ");";
            stmt.execute(createAdminTableSql);

            // 创建users表格
            String createUsersTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ");";
            stmt.execute(createUsersTableSql);

            // 创建customers表格
            String createCustomersTableSql = "CREATE TABLE IF NOT EXISTS customers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "phone TEXT NOT NULL," +
                    "user_level TEXT NOT NULL," +     // 用户级别
                    "registration_time TIMESTAMP NOT NULL," +  // 注册时间
                    "total_spent REAL NOT NULL," +     // 累计消费总金额
                    "email TEXT NOT NULL" +            // 用户邮箱
                    ");";
            stmt.execute(createCustomersTableSql);


            // 创建products表格
            String createProductsTableSql = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "productCode INTEGER NOT NULL," + // 商品编号
                    "name TEXT NOT NULL," +
                    "manufacturer TEXT NOT NULL," +
                    "productionDate TIMESTAMP NOT NULL," +
                    "model TEXT NOT NULL," +
                    "purchasePrice REAL NOT NULL," +
                    "price REAL NOT NULL," +          // 零售价格
                    "quantity INTEGER NOT NULL" +
                    ");";
            stmt.execute(createProductsTableSql);


            // 创建cart表格
            String createCartTableSql = "CREATE TABLE IF NOT EXISTS cart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "product_name TEXT NOT NULL," +
                    "quantity INTEGER NOT NULL" +
                    ");";
            stmt.execute(createCartTableSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // 插入admin数据
            String sql = "INSERT INTO admin (username, password) VALUES ('admin', 'ynuinfo#777');";
            stmt.executeUpdate(sql);

            // 插入用户数据
            sql = "INSERT INTO users (username, password) VALUES " +
                    "('user1', 'password1'), " +
                    "('user2', 'password2'), " +
                    "('user3', 'password3');";
            stmt.executeUpdate(sql);

            // 插入客户数据
            sql = "INSERT INTO customers (name, age, phone, user_level, registration_time, total_spent, email) VALUES " +
                    "('张三', 25, '12345678901', 'Gold', '2023-08-18 10:00:00', 1500.00, 'zhangsan@example.com'), " +
                    "('李四', 30, '23456789012', 'Silver', '2023-08-18 11:30:00', 800.00, 'lisi@example.com'), " +
                    "('王五', 35, '34567890123', 'Bronze', '2023-08-18 13:45:00', 300.00, 'wangwu@example.com');";
            stmt.executeUpdate(sql);


            //插入商品数据
            sql = "INSERT INTO products (productCode, name, manufacturer, productionDate, model, purchasePrice, price, quantity) VALUES " +
                    "(1, '商品1', '制造商1', '2023-08-18', '型号1', 8.0, 10.0, 20), " +
                    "(2, '商品2', '制造商2', '2023-08-19', '型号2', 18.0, 20.0, 15), " +
                    "(3, '商品3', '制造商3', '2023-08-20', '型号3', 28.0, 30.0, 25);";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        users = new ArrayList<>();
        customers = new ArrayList<>();
        products = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // 读取admin数据
            String sql = "SELECT * FROM admin;";
            ResultSet resultSet = stmt.executeQuery(sql);
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                admin = new Admin(username, password);
            }

            // 读取用户数据
            sql = "SELECT * FROM users;";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                users.add(new User(username, password));
            }

            // 读取客户数据
            sql = "SELECT * FROM customers;";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String userLevel = resultSet.getString("user_level");  // 获取用户级别字段
                Timestamp registrationTime = resultSet.getTimestamp("registration_time");  // 获取注册时间字段
                double totalSpent = resultSet.getDouble("total_spent");  // 获取累计消费总金额字段
                String email = resultSet.getString("email");  // 获取用户邮箱字段

                // 创建 Customer 对象并添加到列表中
                customers.add(new Customer(name, age, phone, userLevel, registrationTime, totalSpent, email));
            }


            // 读取产品数据
            sql = "SELECT * FROM products;";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                int productCode = resultSet.getInt("productCode");     // 获取商品编号字段
                String name = resultSet.getString("name");
                String manufacturer = resultSet.getString("manufacturer");  // 获取生产厂家字段
                String productionDate = resultSet.getString("productionDate");  // 获取生产日期字段
                String model = resultSet.getString("model");             // 获取型号字段
                double purchasePrice = resultSet.getDouble("purchasePrice");  // 获取进货价字段
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                // 创建 Product 对象并添加到列表中
                Product product = new Product(productCode, name, manufacturer, productionDate, model, purchasePrice, price, quantity);
                products.add(product);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getter方法用于获取读取到的数据
    public Admin getAdmin() {
        return admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void insertUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "');";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE products SET " +
                    "price = " + product.getPrice() + ", " +
                    "quantity = " + product.getQuantity() + ", " +
                    "manufacturer = '" + product.getManufacturer() + "', " +  // 更新生产厂家
                    "productionDate = '" + product.getProductionDate() + "', " +  // 更新生产日期
                    "model = '" + product.getModel() + "', " +  // 更新型号
                    "purchasePrice = " + product.getPurchasePrice() + " " +
                    "WHERE productCode = " + product.getProductCode() + ";";  // 使用商品编号作为更新条件
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updatePassword(String username, String newPassword, boolean isAdmin) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String tableName = isAdmin ? "admin" : "users";
            String sql = "UPDATE " + tableName + " SET password = '" + newPassword + "' WHERE username = '" + username + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetUserPasswords(List<User> users) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            for (User user : users) {
                String sql = "UPDATE users SET password = '000000' WHERE username = '" + user.getUsername() + "';";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(String customerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM customers WHERE name = '" + customerName + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduct(Product product) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO products (productCode, name, manufacturer, productionDate, model, purchasePrice, price, quantity) VALUES (" +
                    product.getProductCode() + ", '" +
                    product.getName() + "', '" +
                    product.getManufacturer() + "', '" +
                    product.getProductionDate() + "', '" +
                    product.getModel() + "', " +
                    product.getPurchasePrice() + ", " +
                    product.getPrice() + ", " +
                    product.getQuantity() + ");";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void modifyProductInDatabase(Product product) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE products SET " +
                    "productCode = " + product.getProductCode() + ", " +  // 修改商品编号
                    "name = '" + product.getName() + "', " +
                    "manufacturer = '" + product.getManufacturer() + "', " +  // 修改生产厂家
                    "productionDate = '" + product.getProductionDate() + "', " +  // 修改生产日期
                    "model = '" + product.getModel() + "', " +  // 修改型号
                    "purchasePrice = " + product.getPurchasePrice() + ", " +
                    "price = " + product.getPrice() + ", " +
                    "quantity = " + product.getQuantity() +
                    " WHERE productCode = " + product.getProductCode() + ";";  // 使用商品编号作为修改条件
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteProductFromDatabase(String productName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM products WHERE name = '" + productName + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCartItem(String username, String productName, int quantity) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO cart (username, product_name, quantity) VALUES ('" + username + "', '" + productName + "', " + quantity + ");";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCartItem(String username, String productName, int quantity) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "UPDATE cart SET quantity = " + quantity + " WHERE username = '" + username + "' AND product_name = '" + productName + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCartItem(String username, String productName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM cart WHERE username = '" + username + "' AND product_name = '" + productName + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
