import json
import collections

# 读取 JSON 文件
with open("java_questions.json", "r", encoding="utf-8") as json_file:
    data_list = json.load(json_file)

# 创建一个计数器来统计所有标签的出现频率
tag_counts = collections.Counter()

for question in data_list:
    tags = question["Tag"].split()  # 假设标签以空格分隔
    for tag in tags:
        tag_counts[tag] += 1

# 删除 "java" 标签
del tag_counts["java"]

# 找出出现频率最高的10个标签
top_tags = tag_counts.most_common(10)

print("Top 10 Tags (excluding 'java'):")
for tag, count in top_tags:
    print(f"{tag}: {count}")
