// 客户类
import java.util.Date;
class Customer {
    private String name;
    private int age;
    private String phone;
    private String userLevel;
    private Date registrationTime;
    private double totalSpent;
    private String email;

    public Customer(String name, int age, String phone, String userLevel, Date registrationTime, double totalSpent, String email) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.userLevel = userLevel;
        this.registrationTime = registrationTime;
        this.totalSpent = totalSpent;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "姓名: " + name +
                ", 年龄: " + age +
                ", 电话: " + phone +
                ", 用户级别: " + userLevel +
                ", 注册时间: " + registrationTime +
                ", 累计消费总金额: " + totalSpent +
                ", 邮箱: " + email;
    }
}
