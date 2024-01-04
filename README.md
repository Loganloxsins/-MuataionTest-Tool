

# MutationTest-Tool项目文档
![目录](/toc.jpg)
## 项目整体介绍

变异测试是一种重要的软件测试方法，它通过预先制定好的规则产生人工缺陷，利用这些人工缺陷评估测试套件的缺陷检测能力。本项目根据课上介绍的变异测试的内容实现搭建了一个完整的、可交互的基于Javaparaser源码变换的变异测试工具。本项目实现了添加新机制：更多样的变异算子，变异选择策略以及优雅的代码实现：高可读性、高可拓展性、合理运用设计模式。

**项目结构：**

```text
├── compile-mutants.sh # 批量编译变异体
├── pom.xml
├── pool  # Mutant Pool，变异生成的变异体
├── src     
│   ├── main # Demo变异引擎与变异执行实现
│   └── test # 一些Javaparser使用样例
└── testsuite # 独立出来的测试套件类
```

## 变异算子设计思路

### 突变算子ABS

ABS -（ Absolute Value Insertion）涉及abs() negAbs() failOnZero的替换。

在这一部分，我们使用程序ABSMutator生成变异体到pool池中。算法思路如下：每一个算数表达式或者子表达式都可被abs() negAbs() failOnZero替换。

然后，我们编写 SortingTestSuite 来测试这个突变算子。得到了 18/19=94.74%的杀伤率，成功验证了我们测试套件和变异算子（变异体）的有效性和良好的结合效果。

### 突变算子AOR

AOR (Arithmetic Operator Replacement) 突变算子涉及到算术运算符（+，-，*，/,%）。在这一部分，我们使用程序AORMutator生成算术运算符的变异体到pool池中。算法思路如下：

1. 当遇到算术运算符时，将其替换为其他算术运算符。
2. 每个程序只做一处改动，因此我们生成了 |程序中算术运算符数量| * (|替换运算符数量| - 1 ) 个变异体。

然后，我们编写 SortingTestSuite 来测试这个突变算子。得到了 118/153=77.12%的杀伤率，成功验证了我们测试套件和变异算子（变异体）的有效性和良好的结合效果。

### 突变算子LCR

LCR (Logical Connector Replacement) 突变算子涉及到逻辑连接词（& | ^ && ||）。在这一部分，我们使用程序LCRMutator生成逻辑连接词的变异体到pool池中。算法思路如下：

1. 当遇到逻辑连接词时，将其替换为其他逻辑连接词，如 `&&` 替换为 `||`等。
2. 每个程序只做一处改动，因此我们生成了 |程序中逻辑连接词数量| * (|替换运算符数量| - 1 ) 个变异体。

接着，我们编写SortingTestSuite 用于测试这个突变算子。得到了 184/219=84.02%的杀伤率，成功验证了我们测试套件和变异算子（变异体）的有效性和良好的结合效果。

### 突变算子ROR

ROR(Relational Operator Replacement)，包括运算符 (< <= > >= = !=)。

在这一部分，我们首先用程序RORMutator生成变异体到pool池中。该程序思路是：

当遇到属于集合{< <= > >= = !=}的运算符时，即将其替换成其他关系运算符或者falseOp或trueOp。每个程序只有一处改动，即我们生成了|程序中RO数量|*(|RO类型数|-1+2)个变异体。

接着，我们编写testsuite用来测试。我们首先编写了单一的SortingTestSuite用于初步的简单测试，得到了294/376=78.19%的杀伤率，成功验证了我们测试套件和变异算子（变异体）的有效性和良好的结合效果。

### 突变算子UOI

UOI（Unary Operator Insertion）算子涉及一元运算符的插入，这些运算符包括（+, -, !, ~等）。

在这一部分，我们首先用程序UOIMutator生成变异体到pool池中。该程序思路是：

将单运算符（+ - ! ~ ++ --)等，加在每一个正确类型的表达式前。每个程序只有一处改动，即我们生成了|程序中UO数量|*(|正确类型的表达式数量)个变异体。

接着，我们编写testsuite用来测试。我们编写 SortingTestSuite 来测试这个突变算子。得到了9/10=90%的杀伤率，成功验证了我们测试套件和变异算子（变异体）的有效性和良好的结合效果。

### 突变算子UOR

UOR（Unary Operator Replacement）包括前缀与后缀“++”，“--”的替换。在这一部分，我们首先用程序UORMutator生成变异体到pool池中。该程序思路是：

1. 当遇到“++”或“--”时，将其替换为其他形式（前缀与后缀互换）。
2. 每个程序只做一处改动，因此我们生成了 |对应符号数量| * (|程序UO数量| - 1 ) 个变异体。

然后，我们编写 SortingTestSuite 来测试这个突变算子。得到了  10/21=47.62%的杀伤率，成功验证了我们测试套件和变异算子（变异体）的有效性和良好的结合效果。



## 设计方案

### 架构：

本项目为符合BS架构的前后端，其中后端为核心。

本项目的前端可以实现为Web的形式，提供项目上传、变异执行、变异体查看、变异结果查看、测试用例管理等一系列管理功能；也可以实现为封装良好的命令行接口（CLI，Command Line Interface），能够按照用户给定的参数和配置文件完成一系列变异测试活动。

后端应实现变异测试的所有基础环节，包括变异（算子）选择、变异生成、变异执行。其中，变异选择要求变异测试中使用的变异算子是可配置可选择的；变异生成部分要求实现了一些基础变异算子与额外的变异算子，变异测试所用的测试套件采用自行编写。

### 流程：

**包括变异（算子）选择、变异生成、变异执行，测试生成共四个流程。**

#### 变异选择：

选择要应用的变异算子（mutation operator）：

通过结合DemoSrcMutationEngine`与MutatorFactory中进行具体实现：

MutatorFactory实现如下,生成对应的变异算子：

```java
`public class MutatorFactory {`
    `public static Mutator createMutator(String opt, CompilationUnit cu) {`
        `switch (opt) {`
            `case "BIN":`
                `return new BinaryMutator(cu);`
            `case "ABS":`
                `return null;`
            `case "AOR":`
                `return null;`
            `case "LCR":`
                `return null;`
            `case "ROR":`
                `return new RORMutator(cu);`
            `case "UOI":`
                `return new UOIMutator(cu);`
            `case "AIR":`
                `return null;`
            `default:`
                `throw new IllegalArgumentException("This mutator is not supported.");`
        `}`
    `}`
`}`
```

DemoSrcMutationEngine实现对应部分如下:

```java
    String opt = args.length == 3 ? args[2] : "BIN";
    System.out.println("[LOG] Source file: " + srcFile.getAbsolutePath());
    System.out.println("[LOG] Output dir: " + outDir.getAbsolutePath());
    System.out.println("[LOG] Chosen Mutator: " + opt);
    // Initialize mutator(s).
    CompilationUnit cu = StaticJavaParser.parse(srcFile);
    Mutator mutator = MutatorFactory.createMutator(opt, cu);
```



#### 变异生成：

在DemoSrcMutationEngine利用选定的变异算子生成变异体，包括变异点定位和实施变异两个步骤。

**定位变异点：**

调用 `mutator.locateMutationPoints()` 定位变异点，这可能涉及到分析 AST（抽象语法树）结构。

```java
mutator.locateMutationPoints();
```

**实施变异：**

调用 `mutator.mutate()` 获取变异后的 `CompilationUnit` 列表。然后**保存变异体到本地：**创建输出目录，并将变异体写入相应的目录结构中。使用 `preserveToLocal` 方法实现这个逻辑。

```java
mutator.locateMutationPoints();
// Fire off mutation! Mutants can be wrapped.
List<CompilationUnit> mutCUs=mutator.mutate();
System.out.printf("[LOG] Generate %d mutants.\n", mutCUs.size());
// Preserve to local.
preserveToLocal(outDir, srcFile, cu, mutCUs);
```

#### 变异执行：

1.`compile-mutants.sh`: 将生成的变异体（.java文件）批量编译成.class文件。

2.`DemoMutantExecution`: 利用给定的测试套件执行变异体，计算变异得分

3.类与测试套件在example目录中

#### 测试生成：

手动编写测试用例

### 类层次设计：

在设计阶段对系统中的类进行组织和层次化:

**工厂模式**：提高代码的可维护性和可扩展性。

```java
public class MutatorFactory {
    public static Mutator createMutator(String opt, CompilationUnit cu) {
        switch (opt) {
            case "BIN":
                return new BinaryMutator(cu);
            case "ABS":
                return null;
            case "AOR":
                return null;
            case "LCR":
                return null;
            case "ROR":
                return new RORMutator(cu);
            case "UOI":
                return new UOIMutator(cu);
            case "AIR":
                return null;
            default:
                throw new IllegalArgumentException("This mutator is not supported.");
        }
    }
}
```

**抽象：** 类层次设计中常常通过抽象类或接口来定义共享的属性和方法，以实现代码的高度抽象。抽象可以隐藏实现细节，使得系统更容易理解和维护。

**变异算子的抽象与接口实现：**

```java
abstract public class AbstractMutator implements Mutator {

    protected CompilationUnit origCU;

    public AbstractMutator(CompilationUnit cu) {
        this.origCU = cu;
    }

    public void setOrigCU(CompilationUnit origCU) {
        this.origCU = origCU;
    }

}
```

```java
public interface Mutator {

    /**
     * Find the positions that could be mutated by this mutator.
     */
    void locateMutationPoints();

    /**
     * Mutate the original program, which is represented with {@link CompilationUnit},
     * and get a list of mutated programs.
     *
     * @return a list of compilation units mutated from the original one.
     */
    List<CompilationUnit> mutate();

}
```

**继承**

具体的变异算子继承了变异算子抽象类

```java
public class AORMutator extends AbstractMutator
```

**封装：** 

将类的内部实现细节隐藏起来

```java
public class UOIExample {
    private UOIExample() {}

    public static int bitWise(int a) {
        int b = 3;
        a = ~a + b;  // Unary operator applied: BITWISE_COMPLEMENT
        return a;
    }

    public static int minus(int a) {
        int b = 9;
        a = -a + b;  // Unary operator applied: MINUS
        return a;
    }

    public static boolean equal(int a) {
        int b = 10;
        return a == b;  // Binary operator applied: EQUAL
    }
}
```

## 使用方法：

在根目录下：

1. 安装cli程序依赖

   ```bash
   npm install
   ```

2. 编译并打包jar包

   ```bash
   mvn clean package
   ```

3. 运行指定命令

   ```bash
   node mutation-cli run src/main/java/edu/nju/mutest/example/Calculator.java A edu.nju.mutest.example.CalculatorTestSuite testsuite
   ```

### cli命令说明

```bash
node mutation-cli -h
```

- 获取命令说明

```bash
node mutation-cli run <src_file> <mutator> <test_class> [test_dir]
```

- 使用变异算子`<mutator>`给`<src_file>`产生变异体，并使用`[test_dir]`下的测试类`<test_class>`来测试这些变异体。变异体生成在`pool`目录下。

- 参数表

  | 参数            | 是否必须 | 说明                                         |
  | --------------- | -------- | -------------------------------------------- |
  | <src_file>      | 是       | 源文件                                       |
  | &lt;mutator&gt; | 是       | 变异算子                                     |
  | <test_class>    | 是       | 测试类 必须是编译后的.class文件              |
  | [test_dir]      | 否       | 测试类的位置 若不指定，则使用默认值testsuite |

- 变异算子列表

  - ABS
  - AOR
  - LCR
  - ROR
  - UOI
  - UOR

```bash
node mutation-cli mutate <src_file> <mutator> 
```

- 使用变异算子`<mutator>`给`<src_file>`产生变异体。变异体生成在`pool`目录下。

- 参数表

  | 参数            | 是否必须 | 说明     |
  | --------------- | -------- | -------- |
  | <src_file>      | 是       | 源文件   |
  | &lt;mutator&gt; | 是       | 变异算子 |

- 变异算子列表

  - ABS
  - AOR
  - LCR
  - ROR
  - UOI
  - UOR

```bash
node mutation-cli test <test_class> [test_dir]
```

- 使用`[test_dir]`下的测试类`<test_class>`来测试`pool`目录下的变异体。

- 参数表

  | 参数         | 是否必须 | 说明                                         |
  | ------------ | -------- | -------------------------------------------- |
  | <test_class> | 是       | 测试类 必须是编译后的.class文件              |
  | [test_dir]   | 否       | 测试类的位置 若不指定，则使用默认值testsuite |

- 变异算子列表

  - ABS
  - AOR
  - LCR
  - ROR
  - UOI
  - UOR
