import psycopg2
import json

# 读取 JSON 文件
with open('tag_data.json', 'r', encoding='utf-8') as file:
    data = json.load(file)

try:
    # 建立数据库连接
    connection = psycopg2.connect(
        dbname="stackoverflow",
        user="postgres",  
        password="123456",
        host="localhost",
        port="5432"
    )

    # 创建游标对象
    cursor = connection.cursor()

    # 创建名为 top_tags_question 的表格
    create_table_query = '''CREATE TABLE IF NOT EXISTS relevant_tag_data (
                                Tags TEXT
                            );'''
    cursor.execute(create_table_query)
    connection.commit()

    # 插入数据到数据库表格中
    for item in data:
        insert_query = '''INSERT INTO relevant_tag_data (Tags)
                          VALUES (%s);'''
        cursor.execute(insert_query, (
            ','.join(item['Tags']),
        ))
        connection.commit()

    print("数据成功插入到数据库中!")

except (Exception, psycopg2.Error) as error:
    print("发生错误:", error)

finally:
    # 关闭游标和数据库连接
    if connection:
        cursor.close()
        connection.close()
