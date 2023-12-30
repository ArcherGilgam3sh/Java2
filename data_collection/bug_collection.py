import requests
import json
import csv

# 设置 API Key
api_key = "3KuCjhqiJIDILRsikOerKQ(("

# 设置搜索关键字
search_keywords = "error OR exception"

# 初始化数据列表
data_list = []

# 初始化页数
page = 1

while len(data_list) < 600:  # 收集至少600个问题
    api_url = "https://api.stackexchange.com/2.2/search"
    params = {
        "intitle": search_keywords,  # 标题中包含指定关键字
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
        data_list.append({
            "Tag": ";".join(item["tags"]),
            "Question ID": item["question_id"],
            "Title": item["title"],
            "Creation Date": item["creation_date"],
            "Answer Count": item["answer_count"],
            "View Count": item["view_count"],
            "Score": item["score"]
        })

    # 判断是否还有更多页面的数据
    if not data["has_more"]:
        break

    # 增加页数以获取下一页
    page += 1

print("Data collection completed.")

# 将数据保存为 JSON 文件
with open("error_exception_questions.json", "w", encoding="utf-8") as json_file:
    json.dump(data_list, json_file, indent=4, ensure_ascii=False)

# 将数据保存为 CSV 文件
with open("error_exception_questions.csv", "w", newline="", encoding="utf-8") as csv_file:
    csv_writer = csv.writer(csv_file)

    # 写入 CSV 头部
    csv_writer.writerow(["Tag", "Question ID", "Title", "Creation Date", "Answer Count", "View Count", "Score"])

    # 写入每个问题的数据
    for question in data_list:
        csv_writer.writerow([question["Tag"], question["Question ID"], question["Title"], question["Creation Date"],
                             question["Answer Count"], question["View Count"], question["Score"]])
