

# MutationTest-Tool项目文档

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

本项目的使用分为四个步骤： 

0. 准备待测程序和测试套件；
1. 使用`DemoSrcMutationEngine`生成源代码变异体；
2. 使用`compile-mutants.sh`将源代码变异体编译为`.class`文件；
3. 使用`DemoMutantExecution`执行变异体。

可能的命令行执行过程如下：

```shell
# 变异生成（Mutant Creation）
java edu.nju.mutest.DemoSrcMutationEngine <src_file> <pool_dir>
bash compile-mutants.sh <pool_dir>
# 变异执行（Mutation Execution）
java edu.nju.mutest.DemoMutantExecution <testsuite_dir> <pool_dir>
```



