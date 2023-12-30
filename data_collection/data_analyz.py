import json
import pandas as pd
import matplotlib.pyplot as plt

# 读取JSON文件
with open("top_tags_questions.json", "r", encoding="utf-8") as json_file:
    data_list = json.load(json_file)

# 将数据转换为DataFrame
df = pd.DataFrame(data_list)

# 计算每个标签的平均回答数量、平均查看次数和平均得分
tag_metrics = df.groupby("Tag").agg({
    "Answer Count": "mean",
    "View Count": "mean",
    "Score": "mean"
}).reset_index()

# 输出计算结果
print(tag_metrics)

# 创建一个柱状图来可视化结果
plt.figure(figsize=(12, 6))

plt.subplot(1, 3, 1)
plt.bar(tag_metrics["Tag"], tag_metrics["Answer Count"])
plt.title("Average Answer Count")
plt.xticks(rotation=90)

plt.subplot(1, 3, 2)
plt.bar(tag_metrics["Tag"], tag_metrics["View Count"])
plt.title("Average View Count")
plt.xticks(rotation=90)

plt.subplot(1, 3, 3)
plt.bar(tag_metrics["Tag"], tag_metrics["Score"])
plt.title("Average Score")
plt.xticks(rotation=90)

plt.tight_layout()
plt.show()
