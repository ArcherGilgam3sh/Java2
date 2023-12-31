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

while total_collected < 600:
    api_url = "https://api.stackexchange.com/2.2/search"
    params = {
        "tagged": ";".join(tags_to_search),  # 以分号分隔的标签字符串
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

        # 选取第一个符合条件的标签作为主要标签
        main_tag = relevant_tags[0]

        data_list.append({
            "Tags": relevant_tags,  # 存储问题的符合条件的标签列表
            "Tag": main_tag,
            "Question ID": item["question_id"],
            "Title": item["title"],
            "Creation Date": item["creation_date"],
            "Answer Count": item["answer_count"],
            "View Count": item["view_count"],
            "Score": item["score"]
        })
        total_collected += 1

        # 判断是否还有更多页面的数据
        if not data["has_more"]:
            break

    # 如果已经收集到600个问题，退出循环
    if total_collected >= 600:
        break

    # 增加页数以获取下一页
    page += 1

print("Data collection completed.")

# 将数据保存为 JSON 文件
with open("top_tags_questions.json", "w", encoding="utf-8") as json_file:
    json.dump(data_list, json_file, indent=4, ensure_ascii=False)

# 将数据保存为 CSV 文件
with open("top_tags_questions.csv", "w", newline="", encoding="utf-8") as csv_file:
    csv_writer = csv.writer(csv_file)

    # 写入 CSV 头部
    csv_writer.writerow(["Tags", "Tag", "Question ID", "Title", "Creation Date", "Answer Count", "View Count", "Score"])

    # 写入每个问题的数据
    for question in data_list:
        csv_writer.writerow([",".join(question["Tags"]), question["Tag"], question["Question ID"], question["Title"],
                             question["Creation Date"], question["Answer Count"], question["View Count"], question["Score"]])
