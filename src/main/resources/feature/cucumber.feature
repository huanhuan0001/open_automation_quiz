Feature: 提交企业复工申请资料自动化测试
    # 测试功能点1
  Scenario: 填写第一页内容并截图
    Given 用户打开网页 "https://templates.jinshuju.net/detail/Dv9JPD"
    When 用户在第一页选择选项组 "请选择贵单位情况" 并选择选项 "连续生产/开工类企事业单位"
    And 用户截图保存为 "第一页填写内容.png"
    And 用户点击下一页按钮
    Then 用户在第二页填写申请日期为当年元旦日期、申请人为自动化、联系方式为1388888888
    And 用户截图保存为 "第二页填写内容.png"

    # 测试功能点2
  Scenario: 填写第三页内容并截图
    Given 用户在第三页填写以下内容 报备单位 "测试公司"
    And 用户在第三页填写以下内容 在岗人数为"99"
    And 用户在第三页填写以下内容 报备日期为当前日期
    And 用户在第三页填写以下内容 单位负责人为 "您的姓名"
    And 用户在第三页填写以下内容 联系方式为 "13888888888"
    And 用户在第三页填写以下内容 疫情防控方案为 "测试内容"
    And 我点击了提交按钮
    Then 我应该能够在提交结果页看到提交成功的提示
    And 我应该能够成功截图并保存
    And 我应该能够生成HTML格式的测试报告