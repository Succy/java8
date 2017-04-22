package cn.succy.java8.lambda.exec;

import cn.succy.java8.lambda.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Succy
 * @date 2017-03-26 12:59
 **/

public class LambdaExec {
    private List<Employee> employees = Arrays.asList(
            new Employee("张三", 35, 5000.55),
            new Employee("李四", 23, 6600.55),
            new Employee("王五", 50, 3211.23),
            new Employee("赵六", 38, 5854.55),
            new Employee("朱八", 23, 9000)
    );

    @Test
    public void test5() {
        Collections.sort(employees, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        });

        employees.forEach(System.out::println);
    }
}
