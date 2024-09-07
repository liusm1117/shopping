import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class ShoppingSystemGUI extends JFrame {

    private Admin admin;
    private List<User> users;
    private List<Customer> customers;
    private List<Product> products;
    private List<Product> shoppingCart;

    private JPanel loginPanel;
    private JPanel adminPanel;
    private JPanel userPanel;

    private JTextField adminUsernameField;
    private JPasswordField adminPasswordField;
    private JButton adminLoginButton;

    private JTextField userUsernameField;
    private JPasswordField userPasswordField;
    private JButton userLoginButton;
    private JButton userRegisterButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ShoppingSystemGUI();
            }
        });
    }

    public ShoppingSystemGUI() {
        initializeData(); // 初始化数据

        setTitle("购物系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        createLoginPanel();
        setVisible(true);
    }
    // 初始化数据
    private void initializeData() {
        DatabaseInitializer initializer = new DatabaseInitializer();
        initializer.initializeData1();

        users = new ArrayList<>(initializer.getUsers());
        products = new ArrayList<>(initializer.getProducts());
        shoppingCart = new ArrayList<>();

        admin = initializer.getAdmin();
        customers = new ArrayList<>(initializer.getCustomers());
    }


    // 管理员登录
    private void adminLogin() {
        String username = adminUsernameField.getText();
        String password = new String(adminPasswordField.getPassword());

        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            remove(loginPanel);
            createAdminPanel();
            validate();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误，登录失败。");
        }
    }

    // 创建管理员界面
    private void createAdminPanel() {
        adminPanel = new JPanel();
        adminPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel adminMenuLabel = new JLabel("管理员菜单");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        adminPanel.add(adminMenuLabel, gbc);

        JButton passwordManagementButton = new JButton("密码管理");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        adminPanel.add(passwordManagementButton, gbc);

        JButton customerManagementButton = new JButton("客户管理");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        adminPanel.add(customerManagementButton, gbc);

        JButton productManagementButton = new JButton("商品管理");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        adminPanel.add(productManagementButton, gbc);

        JButton logoutButton = new JButton("退出登录");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        adminPanel.add(logoutButton, gbc);

        passwordManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createPasswordManagementPanel(true);
            }
        });

        customerManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCustomerManagementPanel();
            }
        });

        productManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createProductManagementPanel();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(adminPanel);
                createLoginPanel();
                validate();
            }
        });

        getContentPane().add(adminPanel);
    }

    // 创建密码管理界面
    private void createPasswordManagementPanel(boolean isAdmin) {
        JPanel passwordManagementPanel = new JPanel();
        passwordManagementPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel passwordManagementLabel = new JLabel("密码管理");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        passwordManagementPanel.add(passwordManagementLabel, gbc);

        JButton changePasswordButton = new JButton("修改管理员密码");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        passwordManagementPanel.add(changePasswordButton, gbc);

        if (isAdmin) {
            JButton resetPasswordButton = new JButton("重置用户密码");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            passwordManagementPanel.add(resetPasswordButton, gbc);

            resetPasswordButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resetUserPassword();
                }
            });
        }

        JButton backButton = new JButton("返回");
        gbc.gridx = 0;
        gbc.gridy = isAdmin ? 3 : 2;
        gbc.gridwidth = 2;
        passwordManagementPanel.add(backButton, gbc);

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePassword(isAdmin);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(passwordManagementPanel);
                if (isAdmin) {
                    createAdminPanel();
                } else {
                    createUserPanel();
                }
                validate();
            }
        });

        getContentPane().remove(adminPanel);
        if (isAdmin) {
            getContentPane().add(passwordManagementPanel);
        } else {
            getContentPane().add(passwordManagementPanel);
        }
        validate();
    }

    // 修改密码
    private void changePassword(boolean isAdmin) {
        String username = isAdmin ? admin.getUsername() : users.get(0).getUsername();
        String password = isAdmin ? admin.getPassword() : users.get(0).getPassword();

        String inputPassword = JOptionPane.showInputDialog(this, "请输入原密码：", "修改密码", JOptionPane.PLAIN_MESSAGE);
        if (inputPassword != null && inputPassword.equals(password)) {
            String newPassword = JOptionPane.showInputDialog(this, "请输入新密码：", "修改密码", JOptionPane.PLAIN_MESSAGE);
            if (newPassword != null && !newPassword.isEmpty()) {
                DatabaseInitializer initializer = null;
                if (isAdmin) {
                    admin.setPassword(newPassword);
                    // 更新密码到数据库
                    initializer = new DatabaseInitializer();
                    initializer.updatePassword(admin.getUsername(), newPassword, true);
                } else {
                    users.get(0).setPassword(newPassword);
                    // 更新密码到数据库
                    initializer.updatePassword(users.get(0).getUsername(), newPassword, false);
                }
                JOptionPane.showMessageDialog(this, "密码修改成功。");
            }
        } else {
            JOptionPane.showMessageDialog(this, "原密码错误，请重新输入。");
        }
    }

    // 重置用户密码
    private void resetUserPassword() {
        String adminPassword = new String(adminPasswordField.getPassword());

        if (admin.getPassword().equals(adminPassword)) {
            for (User user : users) {
                user.setPassword("000000");
            }
            // 更新密码到数据库
            DatabaseInitializer initializer = new DatabaseInitializer();
            initializer.resetUserPasswords(users);
            JOptionPane.showMessageDialog(this, "用户密码重置成功。");
        } else {
            JOptionPane.showMessageDialog(this, "管理员密码错误，无法重置密码。");
        }
    }

    // 创建客户管理界面
    private void createCustomerManagementPanel() {
        JPanel customerManagementPanel = new JPanel();
        customerManagementPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel customerManagementLabel = new JLabel("客户管理");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        customerManagementPanel.add(customerManagementLabel, gbc);

        JButton listAllCustomersButton = new JButton("列出所有客户信息");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        customerManagementPanel.add(listAllCustomersButton, gbc);

        JButton deleteCustomerButton = new JButton("删除客户信息");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        customerManagementPanel.add(deleteCustomerButton, gbc);

        JButton searchCustomerButton = new JButton("查询客户信息");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        customerManagementPanel.add(searchCustomerButton, gbc);

        JButton backButton = new JButton("返回");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        customerManagementPanel.add(backButton, gbc);

        listAllCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listAllCustomers();
            }
        });

        deleteCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });

        searchCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchCustomer();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(customerManagementPanel);
                createAdminPanel();
                validate();
            }
        });

        getContentPane().remove(adminPanel);
        getContentPane().add(customerManagementPanel);
        validate();
    }

    // 列出所有客户信息
    private void listAllCustomers() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有客户信息。");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Customer customer : customers) {
                sb.append(customer.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "所有客户信息", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // 删除客户信息
    private void deleteCustomer() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有客户信息。");
        } else {
            String[] customerNames = new String[customers.size()];
            for (int i = 0; i < customers.size(); i++) {
                customerNames[i] = customers.get(i).getName();
            }

            String selectedCustomer = (String) JOptionPane.showInputDialog(this, "请选择要删除的客户：",
                    "删除客户信息", JOptionPane.QUESTION_MESSAGE, null, customerNames, customerNames[0]);

            if (selectedCustomer != null) {
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getName().equals(selectedCustomer)) {
                        customers.remove(i);
                        // 从数据库中删除客户信息
                        DatabaseInitializer initializer = new DatabaseInitializer();
                        initializer.deleteCustomer(selectedCustomer);
                        JOptionPane.showMessageDialog(this, "客户信息删除成功。");
                        return;
                    }
                }
            }
        }
    }

    // 查询客户信息
    private void searchCustomer() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有客户信息。");
        } else {
            String[] searchOptions = {"姓名", "电话"};
            String selectedOption = (String) JOptionPane.showInputDialog(this, "请选择查询方式：",
                    "查询客户信息", JOptionPane.QUESTION_MESSAGE, null, searchOptions, searchOptions[0]);

            if (selectedOption != null) {
                String input = JOptionPane.showInputDialog(this, "请输入查询关键字：", "查询客户信息", JOptionPane.PLAIN_MESSAGE);
                if (input != null && !input.isEmpty()) {
                    List<Customer> foundCustomers = new ArrayList<>();
                    switch (selectedOption) {
                        case "姓名":
                            for (Customer customer : customers) {
                                if (customer.getName().equals(input)) {
                                    foundCustomers.add(customer);
                                }
                            }
                            break;

                        case "电话":
                            for (Customer customer : customers) {
                                if (customer.getPhoneNumber().equals(input)) {
                                    foundCustomers.add(customer);
                                }
                            }
                            break;
                    }

                    if (foundCustomers.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "未找到该客户信息，请检查查询关键字是否正确。");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (Customer customer : foundCustomers) {
                            sb.append(customer.toString()).append("\n");
                        }
                        JOptionPane.showMessageDialog(this, sb.toString(), "查询结果", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        }
    }

    // 创建商品管理界面
    private void createProductManagementPanel() {
        JPanel productManagementPanel = new JPanel();
        productManagementPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel productManagementLabel = new JLabel("商品管理");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        productManagementPanel.add(productManagementLabel, gbc);

        JButton listAllProductsButton = new JButton("列出所有商品信息");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        productManagementPanel.add(listAllProductsButton, gbc);

        JButton addProductButton = new JButton("添加商品信息");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        productManagementPanel.add(addProductButton, gbc);

        JButton modifyProductButton = new JButton("修改商品信息");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        productManagementPanel.add(modifyProductButton, gbc);

        JButton deleteProductButton = new JButton("删除商品信息");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        productManagementPanel.add(deleteProductButton, gbc);

        JButton searchProductButton = new JButton("查询商品信息");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        productManagementPanel.add(searchProductButton, gbc);

        JButton backButton = new JButton("返回");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        productManagementPanel.add(backButton, gbc);

        listAllProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listAllProducts();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        modifyProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyProduct();
            }
        });

        deleteProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        searchProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchProduct();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(productManagementPanel);
                createAdminPanel();
                validate();
            }
        });

        getContentPane().remove(adminPanel);
        getContentPane().add(productManagementPanel);
        validate();
    }

    // 列出所有商品信息
    private void listAllProducts() {
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有商品信息。");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Product product : products) {
                sb.append(product.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "所有商品信息", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // 添加商品信息
    private void addProduct() {
        JPanel addProductPanel = new JPanel();
        addProductPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel productCodeLabel = new JLabel("商品编号：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        addProductPanel.add(productCodeLabel, gbc);

        JTextField productCodeField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        addProductPanel.add(productCodeField, gbc);

        JLabel nameLabel = new JLabel("名称：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addProductPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        addProductPanel.add(nameField, gbc);

        JLabel manufacturerLabel = new JLabel("生产厂家：");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        addProductPanel.add(manufacturerLabel, gbc);

        JTextField manufacturerField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        addProductPanel.add(manufacturerField, gbc);

        JLabel productionDateLabel = new JLabel("生产日期：");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        addProductPanel.add(productionDateLabel, gbc);

        JTextField productionDateField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        addProductPanel.add(productionDateField, gbc);

        JLabel modelLabel = new JLabel("型号：");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        addProductPanel.add(modelLabel, gbc);

        JTextField modelField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        addProductPanel.add(modelField, gbc);

        JLabel purchasePriceLabel = new JLabel("进货价：");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        addProductPanel.add(purchasePriceLabel, gbc);

        JTextField purchasePriceField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        addProductPanel.add(purchasePriceField, gbc);

        JLabel priceLabel = new JLabel("零售价格：");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        addProductPanel.add(priceLabel, gbc);

        JTextField priceField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        addProductPanel.add(priceField, gbc);

        JLabel quantityLabel = new JLabel("数量：");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        addProductPanel.add(quantityLabel, gbc);

        JTextField quantityField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        addProductPanel.add(quantityField, gbc);

        int result = JOptionPane.showConfirmDialog(this, addProductPanel, "添加商品信息",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String productCodeStr = productCodeField.getText();
            String name = nameField.getText();
            String manufacturer = manufacturerField.getText();
            String productionDate = productionDateField.getText();
            String model = modelField.getText();
            String purchasePriceStr = purchasePriceField.getText();
            String priceStr = priceField.getText();
            String quantityStr = quantityField.getText();

            if (productCodeStr.isEmpty() || name.isEmpty() || manufacturer.isEmpty() || productionDate.isEmpty() ||
                    model.isEmpty() || purchasePriceStr.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整商品信息。");
            } else {
                int productCode;
                double purchasePrice;
                double price;
                int quantity;
                try {
                    productCode = Integer.parseInt(productCodeStr);
                    purchasePrice = Double.parseDouble(purchasePriceStr);
                    price = Double.parseDouble(priceStr);
                    quantity = Integer.parseInt(quantityStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "输入格式错误，请输入数值。");
                    return;
                }

                Product newProduct = new Product(productCode, name, manufacturer, productionDate,
                        model, purchasePrice, price, quantity);
                products.add(newProduct);
                // 插入新商品信息到数据库
                DatabaseInitializer initializer = new DatabaseInitializer();
                initializer.insertProduct(newProduct);
                JOptionPane.showMessageDialog(this, "商品添加成功。");
            }
        }
    }

    // 修改商品信息
    private void modifyProduct() {
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有商品信息。");
        } else {
            String[] productNames = new String[products.size()];
            for (int i = 0; i < products.size(); i++) {
                productNames[i] = products.get(i).getName();
            }

            String selectedProduct = (String) JOptionPane.showInputDialog(this, "请选择要修改的商品：",
                    "修改商品信息", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

            if (selectedProduct != null) {
                for (Product product : products) {
                    if (product.getName().equals(selectedProduct)) {
                        JPanel modifyProductPanel = new JPanel();
                        modifyProductPanel.setLayout(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.fill = GridBagConstraints.HORIZONTAL;
                        gbc.insets = new Insets(5, 5, 5, 5);

                        JLabel nameLabel = new JLabel("名称：");
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.gridwidth = 1;
                        modifyProductPanel.add(nameLabel, gbc);

                        JTextField nameField = new JTextField(product.getName(), 20);
                        gbc.gridx = 1;
                        gbc.gridy = 0;
                        gbc.gridwidth = 1;
                        modifyProductPanel.add(nameField, gbc);

                        JLabel priceLabel = new JLabel("价格：");
                        gbc.gridx = 0;
                        gbc.gridy = 1;
                        gbc.gridwidth = 1;
                        modifyProductPanel.add(priceLabel, gbc);

                        JTextField priceField = new JTextField(String.valueOf(product.getPrice()), 20);
                        gbc.gridx = 1;
                        gbc.gridy = 1;
                        gbc.gridwidth = 1;
                        modifyProductPanel.add(priceField, gbc);

                        JLabel quantityLabel = new JLabel("数量：");
                        gbc.gridx = 0;
                        gbc.gridy = 2;
                        gbc.gridwidth = 1;
                        modifyProductPanel.add(quantityLabel, gbc);

                        JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()), 20);
                        gbc.gridx = 1;
                        gbc.gridy = 2;
                        gbc.gridwidth = 1;
                        modifyProductPanel.add(quantityField, gbc);

                        int result = JOptionPane.showConfirmDialog(this, modifyProductPanel, "修改商品信息",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.OK_OPTION) {
                            String name = nameField.getText();
                            String priceStr = priceField.getText();
                            String quantityStr = quantityField.getText();

                            if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "请填写完整商品信息。");
                            } else {
                                double price;
                                int quantity;
                                try {
                                    price = Double.parseDouble(priceStr);
                                    quantity = Integer.parseInt(quantityStr);
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(this, "输入格式错误，请输入数值。");
                                    return;
                                }

                                product.setName(name);
                                product.setPrice(price);
                                product.setQuantity(quantity);

                                // 更新商品信息到数据库
                                DatabaseInitializer initializer = new DatabaseInitializer() ;
                                initializer.modifyProductInDatabase(product);

                                JOptionPane.showMessageDialog(this, "商品信息修改成功。");
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    // 删除商品信息
    private void deleteProduct() {
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有商品信息。");
        } else {
            String[] productNames = new String[products.size()];
            for (int i = 0; i < products.size(); i++) {
                productNames[i] = products.get(i).getName();
            }

            String selectedProduct = (String) JOptionPane.showInputDialog(this, "请选择要删除的商品：",
                    "删除商品信息", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

            if (selectedProduct != null) {
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getName().equals(selectedProduct)) {
                        products.remove(i);
                        // 从数据库中删除商品信息
                        DatabaseInitializer initializer = new DatabaseInitializer();
                        initializer.deleteProductFromDatabase(selectedProduct);
                        JOptionPane.showMessageDialog(this, "商品信息删除成功。");
                        return;
                    }
                }
            }
        }
    }

    // 查询商品信息
    private void searchProduct() {
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有商品信息。");
        } else {
            String input = JOptionPane.showInputDialog(this, "请输入要查询的商品名称：",
                    "查询商品信息", JOptionPane.PLAIN_MESSAGE);

            if (input != null && !input.isEmpty()) {
                List<Product> foundProducts = new ArrayList<>();
                for (Product product : products) {
                    if (product.getName().equals(input)) {
                        foundProducts.add(product);
                    }
                }

                if (foundProducts.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "未找到该商品信息，请检查商品名称是否正确。");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Product product : foundProducts) {
                        sb.append(product.toString()).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, sb.toString(), "查询结果", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    // 创建登录界面
    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        JLabel adminLabel = new JLabel("管理员登录");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        loginPanel.add(adminLabel, gbc);

        JLabel adminUsernameLabel = new JLabel("用户名：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(adminUsernameLabel, gbc);

        adminUsernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(adminUsernameField, gbc);

        JLabel adminPasswordLabel = new JLabel("密码：");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginPanel.add(adminPasswordLabel, gbc);

        adminPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginPanel.add(adminPasswordField, gbc);

        adminLoginButton = new JButton("登录");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(adminLoginButton, gbc);

        adminLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adminLogin();
            }
        });

        JLabel userLabel = new JLabel("用户登录");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(userLabel, gbc);

        JLabel userUsernameLabel = new JLabel("用户名：");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        loginPanel.add(userUsernameLabel, gbc);

        userUsernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        loginPanel.add(userUsernameField, gbc);

        JLabel userPasswordLabel = new JLabel("密码：");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        loginPanel.add(userPasswordLabel, gbc);

        userPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        loginPanel.add(userPasswordField, gbc);

        userLoginButton = new JButton("登录");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        loginPanel.add(userLoginButton, gbc);

        userLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userLogin();
            }
        });

        userRegisterButton = new JButton("用户注册");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        loginPanel.add(userRegisterButton, gbc);

        userRegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userRegister();
            }
        });

        getContentPane().add(loginPanel);
        validate();
    }

    // 用户登录
    private void userLogin() {
        String username = userUsernameField.getText();
        String password = new String(userPasswordField.getPassword());

        boolean userFound = false;
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                userFound = true;
                break;
            }
        }

        if (userFound) {
            remove(loginPanel);
            createUserPanel();
            validate();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误，登录失败。");
        }
    }

    // 用户注册
    private void userRegister() {
        JPanel userRegisterPanel = new JPanel();
        userRegisterPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("用户名：");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        userRegisterPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        userRegisterPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("密码：");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        userRegisterPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        userRegisterPanel.add(passwordField, gbc);

        int result = JOptionPane.showConfirmDialog(this, userRegisterPanel, "用户注册",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整注册信息。");
            } else {
                // 将用户注册的账号和密码写入数据库
                DatabaseInitializer initializer = new DatabaseInitializer();
                initializer.insertUser(username, password);

                // 将新注册的用户添加到列表中（如果需要）
                users.add(new User(username, password));
                JOptionPane.showMessageDialog(this, "用户注册成功。");
            }
        }
    }

    // 创建用户界面
    private void createUserPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userMenuLabel = new JLabel("用户菜单");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        userPanel.add(userMenuLabel, gbc);

        JButton listProductsButton = new JButton("列出所有商品信息");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        userPanel.add(listProductsButton, gbc);

        JButton searchProductButton = new JButton("查询商品信息");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        userPanel.add(searchProductButton, gbc);

        JButton addToCartButton = new JButton("添加商品到购物车");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        userPanel.add(addToCartButton, gbc);

        JButton viewCartButton = new JButton("查看购物车");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        userPanel.add(viewCartButton, gbc);

        JButton checkoutButton = new JButton("结账");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        userPanel.add(checkoutButton, gbc);

        JButton logoutButton = new JButton("退出登录");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        userPanel.add(logoutButton, gbc);

        listProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listAllProducts();
            }
        });

        searchProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchProduct();
            }
        });

        addToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        viewCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkout();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove(userPanel);
                createLoginPanel();
                validate();
            }
        });

        getContentPane().remove(loginPanel);
        getContentPane().add(userPanel);
        validate();
    }

    // 添加商品到购物车
    private void addToCart() {
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有商品信息。");
        } else {
            String[] productNames = new String[products.size()];
            for (int i = 0; i < products.size(); i++) {
                productNames[i] = products.get(i).getName();
            }

            String selectedProduct = (String) JOptionPane.showInputDialog(this, "请选择要添加的商品：",
                    "添加商品到购物车", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

            if (selectedProduct != null) {
                for (Product product : products) {
                    if (product.getName().equals(selectedProduct)) {
                        int quantity = 1;
                        try {
                            quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "请输入商品数量：",
                                    "添加商品到购物车", JOptionPane.PLAIN_MESSAGE));
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "数量输入格式错误，请输入整数。");
                            return;
                        }

                        if (quantity <= 0) {
                            JOptionPane.showMessageDialog(this, "数量必须大于0。");
                            return;
                        }

                        CartItem cartItem = new CartItem(product, quantity);
                        cart.addCartItem(cartItem);

                        JOptionPane.showMessageDialog(this, "商品添加到购物车成功。");
                        return;
                    }
                }
            }
        }
    }
    private Cart cart = new Cart();
    // 查看购物车
    private void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "购物车为空。");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("购物车中的商品：\n");
            for (CartItem item : cart.getCartItems()) {
                sb.append(item.toString()).append("\n");
            }
            sb.append("总计：").append(cart.getTotalPrice());
            sb.append("\n\n请选择要移出购物车的商品：");

            // 创建商品选择复选框
            JCheckBox[] checkBoxes = new JCheckBox[cart.getCartItems().size()];
            for (int i = 0; i < cart.getCartItems().size(); i++) {
                CartItem item = cart.getCartItems().get(i);
                checkBoxes[i] = new JCheckBox(item.toString());
            }

            JPanel panel = new JPanel(new GridLayout(checkBoxes.length + 1, 1));
            panel.add(new JLabel(sb.toString()));

            for (JCheckBox checkBox : checkBoxes) {
                panel.add(checkBox);
            }

            int result = JOptionPane.showConfirmDialog(this, panel, "购物车", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                boolean removed = false; // 标记是否有移除的商品
                // 移出选中的商品
                for (int i = checkBoxes.length - 1; i >= 0; i--) {
                    if (checkBoxes[i].isSelected()) {
                        cart.removeCartItem(i);
                        removed = true;
                    }
                }

                if (removed) {
                    JOptionPane.showMessageDialog(this, "商品已移出购物车。");
                }
            }
        }
    }


    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "购物车为空。");
        } else {
            double totalPrice = cart.getTotalPrice();

            StringBuilder sb = new StringBuilder();
            sb.append("购物车中的商品：\n");
            for (CartItem item : cart.getCartItems()) {
                sb.append(item.toString()).append("\n");
            }
            sb.append("总金额：").append(totalPrice);

            JPanel panel = new JPanel();
            panel.add(new JLabel(sb.toString()));

            JButton payButton = new JButton("结账");
            panel.add(payButton);

            payButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // 减少仓库中对应商品的数量
                    for (CartItem item : cart.getCartItems()) {
                        Product product = item.getProduct();
                        int quantity = item.getQuantity();
                        product.setQuantity(product.getQuantity() - quantity);

                        // 更新商品数量到数据库
                        DatabaseInitializer initializer = new DatabaseInitializer();
                        initializer.updateProduct(product);
                    }

                    // 清空购物车
                    cart.clearCart();

                    JOptionPane.showMessageDialog(ShoppingSystemGUI.this, "付款成功！");
                }
            });

            JOptionPane.showOptionDialog(this, panel, "结账",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
        }
    }
}
