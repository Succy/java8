package cn.succy.java8.stream;

import cn.succy.java8.lambda.Employee;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * StreamApi测试类
 *
 * @author Succy
 * @date 2017-04-21 22:47
 **/

public class StreamApiTest {
    private List<Employee> employees = Arrays.asList(
            new Employee("张三", 35, 5000.55, Employee.Status.BUSY),
            new Employee("李四", 23, 6600.55, Employee.Status.IDLE),
            new Employee("大南瓜", 25, 5600.55, Employee.Status.BUSY),
            new Employee("王五", 50, 3211.23, Employee.Status.VOCATION),
            new Employee("王五", 50, 3211.23, Employee.Status.VOCATION),
            new Employee("大屌丝", 55, 9211.23, Employee.Status.IDLE),
            new Employee("赵六", 38, 5854.55, Employee.Status.BUSY),
            new Employee("朱八", 23, 9000, Employee.Status.IDLE),
            new Employee("朱八", 23, 9000, Employee.Status.IDLE)
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


    /**
     * Stream 终止操作
     * allMatch 当流中所有元素都匹配时返回true，与&&操作类似
     * anyMatch 只要流中有一个元素与之匹配，返回true，与||操作类似
     * noneMatch 流中所有元素不匹配返回true
     * findFirst 从流中查找第一个
     * findAny 从流中查找任意一个，是随意的
     * count 取出流中元素总数
     * max 取出流中按照某方式比较的最大值
     * min 取出流中以某方式比价的最小值
     */
    @Test
    public void testMatch() {
        boolean b = employees.stream().allMatch(e -> e.getStatus() == Employee.Status.BUSY);
        System.out.println(b);
        boolean b1 = employees.stream().anyMatch(e -> e.getStatus() == Employee.Status.BUSY);
        System.out.println(b1);
        boolean b2 = employees.stream().noneMatch(e -> e.getStatus() == Employee.Status.BUSY);
        System.out.println(b2);
    }

    @Test
    public void testFind() {
        // 找出工资大于5000的第一个
        Optional<Employee> emp = employees.stream().filter(e -> e.getSalary() > 5000).findFirst();
        System.out.println(emp);
        System.out.println("==========================================");
        // 找出工资大于5000的任意一个
        Optional<Employee> emp1 = employees.stream().filter(e -> e.getSalary() > 5000).findAny();
        System.out.println(emp1);
    }

    @Test
    public void testCountMaxMin() {
        // 找出工资最大一个
        Optional<Employee> max = employees.stream().max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(max.get());
        System.out.println("=============================================");
        // 找出工资最少的一个
        Optional<Employee> min = employees.stream().min((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(min.get());
        System.out.println("=============================================");
        // 取出最少的工资
        Optional<Double> min1 = employees.stream().map(Employee::getSalary).min(Double::compare);
        System.out.println(min1.get());
    }

    /**
     * 归约和收集
     * reduce 归约，将流中的元素反复结合在一起，得到一个值
     * collect 收集
     */

    @Test
    public void testReduce() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // 把list里面元素相加,有起始值的
        Integer sum = list.stream().reduce(0, (x, y) -> x + y);
        System.out.println(sum);
        // 把list里面元素相加,没有起始值的
        System.out.println("------------------------------------");
        Optional<Integer> sum1 = list.stream().reduce(Integer::sum);
        System.out.println(sum1.get());
        System.out.println("------------------------------------");
        // 拿到所有员工的工资之和
        Optional<Double> salarys = employees.stream().map(Employee::getSalary).reduce(Double::sum);
        System.out.println(salarys.get());
    }

    /**
     * 将流中元素收集到一个集合
     */
    @Test
    public void testCollect1() {
        // toList
        List<String> namesToList = employees.stream().map(Employee::getName).collect(Collectors.toList());
        System.out.println(namesToList);
        System.out.println("------------------------------------------");
        // toSet
        Set<String> namesToSet = employees.stream().map(Employee::getName).collect(Collectors.toSet());
        System.out.println(namesToSet);
        System.out.println("------------------------------------------");
        // 求和1
        Double collectSum = employees.stream().collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(collectSum);
        // 求和2,使用概要统计，会得到和，平均值，最大，最小值等
        // 相当于sql中的组函数
        DoubleSummaryStatistics dss = employees.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println("dss sum: " + dss.getSum());
        System.out.println("dss avg: " + dss.getAverage());
        System.out.println("dss count: " + dss.getCount());
        System.out.println("dss max: " + dss.getMax());
        System.out.println("dss min: " + dss.getMin());
        System.out.println("------------------------------------------");
        // 分组 按照status分组
        Map<Employee.Status, List<Employee>> groupMap = employees.stream()
                .distinct()
                .collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(groupMap);
        System.out.println("------------------------------------------");
        // 多重分组，先按照status进行分组，接着按照年龄进行分组
        Map<Employee.Status, Map<String, List<Employee>>> groupMap1 = employees.stream()
                .distinct()
                .collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((Employee e) -> {
                    if (e.getAge() <= 30) {
                        return "青年";
                    } else if (e.getAge() > 30 && e.getAge() <= 50) {
                        return "中年";
                    } else {
                        return "老年";
                    }
                })));
        System.out.println(groupMap1);

    }
}
