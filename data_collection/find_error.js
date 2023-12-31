const fs = require("fs");

// 读取JSON文件
fs.readFile("top_tags_questions.json", "utf8", (err, data) => {
  if (err) {
    console.error("读取文件出错：", err);
    return;
  }

  try {
    // 解析JSON数据
    const jsonData = JSON.parse(data);

    // 创建一个对象来存储异常和错误类型的计数
    const errorExceptionCount = {};

    // 遍历JSON数据
    for (const item of jsonData) {
      const title = item["Title"];

      // 使用正则表达式匹配标题中的异常或错误相关词汇
      const matches = title.match(/(exception|error|problem|issue|bug)[\s:]*([\w\s]+)?/gi);

      // 如果匹配到了关键词，则增加计数
      if (matches) {
        for (const match of matches) {
          errorExceptionCount[match] = (errorExceptionCount[match] || 0) + 1;
        }
      }
    }

    // 将计数结果按出现次数降序排序
    const sortedCounts = Object.entries(errorExceptionCount)
      .sort(([, countA], [, countB]) => countB - countA);

    // 获取前十个出现次数最多的异常或错误类型
    const top10Types = sortedCounts.slice(0, 10);

    // 打印结果
    console.log("出现最频繁的异常或错误类型（前十个）：");
    for (const [type, count] of top10Types) {
      console.log(type, "，出现次数：", count);
    }
  } catch (error) {
    console.error("解析JSON数据出错：", error);
  }
});
