package cn.succy.java8.stream;

import cn.succy.java8.lambda.Employee;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * StreamApi测试类
 *
 * @author Succy
 * @date 2017-04-21 22:47
 **/

public class StreamApiTest {
    private List<Employee> employees = Arrays.asList(
            new Employee("张三", 35, 5000.55),
            new Employee("李四", 23, 6600.55),
            new Employee("王五", 50, 3211.23),
            new Employee("赵六", 38, 5854.55),
            new Employee("朱八", 23, 9000),
            new Employee("朱八", 23, 9000)

    );

    /**
     * 创建Stream的方式
     */
    @Test
    public void testCreateStream() {
        // 1.使用集合中的stream()
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();
        // 2、使用Arrays.stream()
        String[] arr = new String[10];
        Stream<String> stream1 = Arrays.stream(arr);
        // 3、使用Stream.of()
        Stream<String> stream2 = Stream.of("aaa", "bbb", "ccc");
        // 4、无限流
        // 4.1、迭代方式生成无限流，seed种子，作为无限流的起点 一元表达式:按照该函数生成无限流
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2);
        // 取无限流中前十个输出
        stream3.limit(10).forEach(System.out::println);

        // 4.2、生成方式产生无限流 参数是一个生成流的提供者
        Stream<Double> stream4 = Stream.generate(Math::random);
        // 同样取出10个流中数据打印
        stream4.limit(10).forEach(System.out::println);

    }

    /**
     * Stream Api的中间操作
     * 筛选和切片，测试用例中，用到的有:
     * filter 过滤流，过滤出符合策略的数据
     * limit(n) 截断流, 截取流中前n个数据
     * skip(n) 跳过流，跳过流中前n个数据， 与limit互补
     * distinct 去除重复的数据，要重新hashCode和equals方法
     */
    @Test
    public void testFilter() {
        // 中间操作：过滤出工资大于6000的员工信息
        employees.stream().filter(e -> e.getSalary() > 6000).forEach(System.out::println);
    }

    @Test
    public void testLimit() {
        // 中间操作：过滤出工资大于6000的前两个员工信息
        employees.stream()
                .filter(e -> e.getSalary() > 6000)
                .limit(2)
                .forEach(System.out::println);
    }

    @Test
    public void testSkip() {
        // 中间操作，过滤出工资大于6000的员工，并且跳过前面两个
        employees.stream()
                .filter(e -> e.getSalary() > 6000)
                .skip(2)
                .forEach(System.out::println);
    }

    @Test
    public void testDistinct() {
        // 中间操作，过滤出工资大于6000的员工，并且去重
        employees.stream()
                .filter(e -> e.getSalary() > 6000)
                .distinct()
                .forEach(System.out::println);
    }

    /**
     * 中间操作之映射
     * map: 接受一个函数作为参数，将函数应用到流中每一个元素
     * flatMap：接受一个函数，将流中每一个元素都换成流，接着合并成一个新的流
     */
    @Test
    public void testMap() {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee", "fff");
        // 把list中的元素都变成大写
        list.stream().map(str -> str.toUpperCase()).forEach(System.out::println);

        // 提取employee的姓名
        employees.stream().map(Employee::getName).forEach(System.out::println);
        System.out.println("------------------------------------------");
        // 假设使用map去将list中的所有元素变成单个字符输出
        // 那么先的到的是一个Stream<Stream<Character>>类型的数据，接着遍历stream 在遍历character
        list.stream().map(StreamApiTest::filterCharacter).forEach(s -> s.forEach(System.out::println));

        System.out.println("------------------------------------------");
        // 上述需求，如果使用flatMap的话，就会把每个元数经过filterCharacter产生的流里边的元素合并成一些新的流
        // 这个流是Stream<Character>类型的，一步到位
        list.stream().flatMap(StreamApiTest::filterCharacter).forEach(System.out::println);
        System.out.println("可以看到，使用lambda和Stream可以使得对数据操作简洁很多");
    }

    private static Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }

    /**
     * 中间操作之排序
     * sorted() 自然排序，流中的元素实现了Comparable接口，自己可以排序，如String Integer
     * sorted(Comparator c) 定制排序，流中的元素没实现Comparable接口，要自定义排序规则
     */
    @Test
    public void testSorted() {
        List<String> list = Arrays.asList("aaa", "eee", "ccc", "ddd", "bbb", "fff");
        list.stream().sorted().forEach(System.out::println);
        System.out.println("--------------------------------------------------");
        employees.stream().sorted((e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            }
            return e1.getAge() - e2.getAge();
        }).forEach(System.out::println);
    }
}
