import requests
import json
import csv
import collections
import matplotlib.pyplot as plt

# 设置 API Key
api_key = "3KuCjhqiJIDILRsikOerKQ(("

# 初始化数据列表
data_list = []

# 发起 API 请求以获取数据
api_url = "https://api.stackexchange.com/2.2/questions"
params = {
    "site": "stackoverflow",
    "key": api_key,
    "pagesize": 100,  # 每页的问题数量
}

page = 1
total_collected = 0  # 记录已经收集到的数据数量

while total_collected < 600:
    params["page"] = page
    response = requests.get(api_url, params=params)
    if response.status_code != 200:
        print(f"Error: Unable to fetch data from API (Page {page})")
        break
    
    # 解析 JSON 数据
    data = response.json()
    
    # 处理每个问题
    for question in data["items"]:
        tags = question["tags"]
        for tag in tags:
            data_list.append({
                "Tag": tag,
                "Question ID": question["question_id"],
                "Title": question["title"],
                "Creation Date": question["creation_date"],
                "Answer Count": question["answer_count"]
            })
        total_collected += 1
    
    # 判断是否还有更多页面的数据
    if not data["has_more"]:
        break
    
    # 增加页数以获取下一页
    page += 1

    # 如果已经收集到600条数据，退出循环
    if total_collected >= 600:
        break

# 将数据保存为 JSON 文件
with open("java_questions.json", "w", encoding="utf-8") as json_file:
    json.dump(data_list, json_file, indent=4, ensure_ascii=False)

# 将数据保存为 CSV 文件
with open("java_questions.csv", "w", newline="", encoding="utf-8") as csv_file:
    csv_writer = csv.writer(csv_file)
    
    # 写入 CSV 头部
    csv_writer.writerow(["Tag", "Question ID", "Title", "Creation Date", "Answer Count"])
    
    # 写入每个问题的数据
    for question in data_list:
        csv_writer.writerow([question["Tag"], question["Question ID"], question["Title"], question["Creation Date"], question["Answer Count"]])

print("Data collection completed.")

# 对数据进行分类和分析，根据标签归类
tag_counts = collections.Counter([question["Tag"] for question in data_list])

# 以柱状图形式呈现标签的数量
tags, counts = zip(*tag_counts.items())
plt.bar(tags, counts)
plt.xlabel("Tags")
plt.ylabel("Count")
plt.title("Tag Popularity")
plt.xticks(rotation=90)
plt.show()
