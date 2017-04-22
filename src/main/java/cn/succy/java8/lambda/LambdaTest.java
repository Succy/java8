package cn.succy.java8.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * lambda表达式的测试类
 *
 * @author Succy
 * @date 2017-03-26 10:18
 **/

public class LambdaTest {
    // 需求：找出公司中所有员工年龄大于35岁的

    // 构造员工
    private List<Employee> employees = Arrays.asList(
            new Employee("张三", 35, 5000.55),
            new Employee("李四", 23, 6600.55),
            new Employee("王五", 50, 3211.23),
            new Employee("赵六", 38, 5854.55),
            new Employee("朱八", 25, 9000)
    );

    // 传统方式，直接遍历集合，找到符合条件的元素，组成新集合
    @Test
    public void test1() {
        List<Employee> emps = filterEmployees(this.employees);
        for (Employee employee : emps) {
            System.out.println(employee);
        }
    }

    private List<Employee> filterEmployees(List<Employee> list) {
        List<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if (emp.getAge() >= 35) {
                emps.add(emp);
            }
        }
        return emps;
    }

    // 使用策略模式
    @Test
    public void test2() {
        // 找出工资大于5000的员工
        List<Employee> emps = filterEmployee(this.employees, new MyPredicate<Employee>() {
            @Override
            public boolean handle(Employee employee) {
                return employee.getSalary() >= 5000;
            }
        });
        System.out.println("---------工资大于5000的员工集合-------");
        for (Employee emp : emps) {
            System.out.println(emp);
        }
        System.out.println("-------------------------------------");
        // 找出年龄大于358的员工
        emps = filterEmployee(this.employees, new MyPredicate<Employee>() {
            @Override
            public boolean handle(Employee employee) {
                return employee.getAge() >= 35;
            }
        });
        System.out.println("---------年龄大于35的员工集合-------");
        for (Employee emp : emps) {
            System.out.println(emp);
        }
    }


    // 传统方式只要需求改动，就要新增方法，并不是很好，这个时候可以使用策略模式
    // 当需求改变的时候，只需传递不同的策略实现类进去即可
    private List<Employee> filterEmployee(List<Employee> list, MyPredicate<Employee> mp) {
        List<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if (mp.handle(emp)) {
                emps.add(emp);
            }
        }
        return emps;
    }

    // 使用策略模式加匿名内部类的方式的确可以减少一定的编码量，并且写起来也很清晰但是实际上冗余代码还是不少
    // 这个时候可以使用java8的lambda表达式来简化操作
    @Test
    public void test3() {
        List<Employee> emps = filterEmployee(this.employees, e -> e.getAge() >= 35);
        emps.forEach(System.out::println);
        System.out.println("-------------------------------------");
        emps = filterEmployee(this.employees, e -> e.getSalary() >= 5000);
        emps.forEach(System.out::println);

        // 的确简洁不少，能不能再简洁一点，可以的，使用Stream API
    }

    // 接下来，加入连策略接口都没有，能不能再简洁点实现上述需求呢？肯定可以，Stream API是很强大的
    @Test
    public void test4() {
        System.out.println("---------年龄大于35的员工集合-------");
        employees.stream().filter(employee -> employee.getAge() >= 35).forEach(System.out::println);
        System.out.println("---------工资大于5000的员工集合-------");
        employees.stream().filter(employee -> employee.getSalary() >= 5000).forEach(System.out::println);
    }


}
