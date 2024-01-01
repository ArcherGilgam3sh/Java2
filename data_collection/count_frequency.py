# import re
# import json

# # 读取 JSON 文件
# with open('error_exception_questions.json', 'r', encoding='utf-8') as file:
#     data = json.load(file)

# # 获取所有标题
# titles = [item['Title'] for item in data]

# # 要匹配的关键词
# keyword = 'Error'  # 你想要匹配的词汇

# # 匹配标题中包含特定连续词汇的标签
# matches = []
# for title in titles:
#     found = re.findall(r'\b\w*{}+\w*\b'.format(re.escape(keyword)), title, flags=re.IGNORECASE)
#     matches.extend(found)

# new_matches = list(set(matches))
# print("匹配项列表的长度:", len(matches))
# print("去重之后的长度:", len(new_matches))
# # 输出匹配项
# for match in new_matches:
#     print(match)

import re
import json

# 读取 JSON 文件
with open('error_exception_questions.json', 'r', encoding='utf-8') as file:
    data = json.load(file)

# 要匹配的关键词列表
keywords = ['Exception', 'Error']  # 你想要匹配的词汇

# 匹配标题中包含特定连续词汇的标签并添加到原始对象中
for item in data:
    for keyword in keywords:
        matches = re.findall(r'\b\w*{}+\w*\b'.format(re.escape(keyword)), item['Title'], flags=re.IGNORECASE)
        # 将列表转换为以空格分隔的字符串
        matches_string = ' '.join(matches)
        # 将属性名修改为对应的Matches，值为字符串
        if keyword == 'Exception':
            item['Exception Matches'] = matches_string
        elif keyword == 'Error':
            item['Error Matches'] = matches_string

# 写入更新后的数据到新文件中
new_file_name = 'updated_error_exception_question.json'

with open(new_file_name, 'w', encoding='utf-8') as new_file:
    json.dump(data, new_file, indent=4, ensure_ascii=False)

print(f"已将更新后的数据写入到 {new_file_name} 文件中")




