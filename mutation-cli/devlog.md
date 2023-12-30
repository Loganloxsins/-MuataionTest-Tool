## cli

使用[commander.js](https://github.com/tj/commander.js)开发

### 需求

步骤为：

1. 变异算子选择
2. 生成变异体
3. 选择测试用例
4. 测试变异体

cli应该具备

- `mutation-cli mutate`生成变异体
- `mutation-cli test`测试变异体
- `mutation-cli run`生成并测试变异体

需要的参数有：

- `-m --mutator <mutator>`选择算子
- `-p --pool <pool-dir>`生成变异体位置
- `-t --testsuite <testsuite>`选择测试用例

其他：

- Java版本错误要能报错

## 12.30