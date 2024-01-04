import os
import re
import matplotlib.pyplot as plt

# 初始化一个字典存储各个算子及其对应的变异得分
mutation_scores = {
    'ABS': None,
    'AOR': None,
    'LCR': None,
    'ROR': None,
    'UOI': None,
    'UOR': None
}

# 定义文件名列表
file_names = ['ABS_log.txt', 'AOR_log.txt', 'LCR_log.txt', 'ROR_log.txt', 'UOI_log.txt', 'UOR_log.txt']

# 遍历文件列表
for file_name in file_names:
    # 检查文件是否存在
    if os.path.isfile(file_name):
        # 打开文件并读取最后一行
        with open(file_name, 'r') as file:
            last_line = file.readlines()[-1]

        # 提取算子名称和变异得分
        match = re.search(r'(?:\[LOG\] Stats: \d+/\d+\(#killed/#total\), score=)(\d+\.\d+)', last_line)
        if match:
            score = float(match.group(1))
            operator = re.findall(r'([A-Z]+)_log\.txt', file_name)[0]
            mutation_scores[operator] = score
        else:
            print(f"无法从文件 {file_name} 中提取变异得分.")
    else:
        print(f"文件 {file_name} 不存在.")

# 将字典转换为列表以绘图（按字典顺序）
scores_list = [mutation_scores[key] for key in mutation_scores.keys()]


# 绘制条形图
bar_handles = plt.bar(mutation_scores.keys(), scores_list)

# 在每条条形上显示具体得分
for bar, score in zip(bar_handles, scores_list):
    plt.text(bar.get_x() + bar.get_width() / 2, bar.get_height(), f'{score:.2f}', ha='center', va='bottom')

    
plt.xlabel('Mutation Operators')
plt.ylabel('Mutation Score')
plt.title('Mutation Operators & Mutation Score')
plt.xticks(rotation=45)  # 倾斜x轴标签以便更好地展示
plt.show()