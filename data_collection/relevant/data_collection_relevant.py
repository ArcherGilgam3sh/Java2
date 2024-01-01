import requests
import json
import csv

# 设置 API Key
api_key = "3KuCjhqiJIDILRsikOerKQ(("

# 定义要搜索的标签列表
tags_to_search = ["spring-boot", "android", "maven", "hibernate", "gradle", "kotlin", "jpa", "docker", "json", "javafx"]

# 初始化数据列表
data_list = []
total_collected = 0  # 记录已经收集到的数据数量

# 初始化页数
page = 1

while total_collected < 2000:
    api_url = "https://api.stackexchange.com/2.2/questions"
    params = {
        "tagged": "java",  # 以分号分隔的标签字符串
        "site": "stackoverflow",
        "key": api_key,
        "pagesize": 100,  # 每页的问题数量
        "page": page
    }

    response = requests.get(api_url, params=params)
    if response.status_code != 200:
        print(f"Error: Unable to fetch data from API (Page {page})")
        break

    # 解析 JSON 数据
    data = response.json()

    # 处理每个搜索结果
    for item in data["items"]:
        # 获取问题的所有标签
        question_tags = item["tags"]

        # 检查是否有标签在 tags_to_search 中
        relevant_tags = [tag for tag in question_tags if tag in tags_to_search]

        # 如果没有符合条件的标签，跳过这个问题
        if not relevant_tags:
            continue

        # 存储问题的符合条件的标签列表
        data_list.append({
            "Tags": question_tags
        })
        total_collected += 1

        # 判断是否还有更多页面的数据
        if not data["has_more"]:
            break

    # 如果已经收集到2000个问题，退出循环
    if total_collected >= 2000:
        break

    print(total_collected)

    # 增加页数以获取下一页
    page += 1

print("Data collection completed.")

# 将数据保存为 JSON 文件
with open("tag_data.json", "w", encoding="utf-8") as json_file:
    json.dump(data_list, json_file, indent=4, ensure_ascii=False)
