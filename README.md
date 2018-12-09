
实现步骤
----
**一、开发前奏**

开发工具：eclipse
jar管理：maven
数据库：oracle
架构：SpringMvc + Spring +Mybatis
微信公众号：企业号（个人订阅号和公众测试号无此权限）

**二、开发步骤**

1、开发前请先查看文档[微信扫码支付流程](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_5)，[统一下单接口](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1)和[设置回调接口](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_3)，对整个流程有一些认识。

**2、开发过程中需要的参数如下图所示。**

    ##############################固定参数部分##############################################
    #公众账号appid
    appid=
    #商户号：微信公众平台——微信支付——商户信息——基本信息——商户号
    mch_id=
    #扫码支付回调接口（扫码回调url没有严格规定的，域名和IP都可以，其他非80端口的也是可以的）
    notify_url=
    #交易类型，一般为NATIVE
    trade_type=NATIVE
    #请求的微信支付接口
    ufdoder_url=https://api.mch.weixin.qq.com/pay/unifiedorder
    #api密钥：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
    api_key=
    #用于请求参数的身份验证（不需要发给微信）
    secertKey=
    ##############################动态可添加参数部分##############################################
    ############商品1#######
    #商品名称
    title_test_000=
    #交易金额（单位：分）
    fee_test_000=
    ############商品2#######
    title_test_001=
    fee_test_001=

3、应用运行整体流程图

![在这里插入图片描述](https://img-blog.csdnimg.cn/2018120717252328.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMyNTc0NDM1,size_16,color_FFFFFF,t_70)

4、设置回调接口接收微信通知消息
具体可查看文档[设置回调接口](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_3)

5、支付成功通知给客户端
方式一：前端js定时轮询数据库，看看是否已经支付成功，如果成功发生页面跳转。
方式二：编写windows服务，不断查询数据库，如果支付成功则下发通知短信。可参考我的[制作windows服务](https://blog.csdn.net/qq_32574435/article/details/78963133)


**源代码下载链接：**
https://github.com/flypangzhi/wechat-payinterface
备注：功能是实现了，不过还有很多地方可以优化，优化的任务就交给大家啦。
如果有什么好的意见的话可以跟我反馈：QQ:1484772397


参考链接
[log4j.properties配置详解与实例](https://blog.csdn.net/dr_guo/article/details/50718063)
[Log4j 日志文件存放位置设置](https://hbiao68.iteye.com/blog/1947618)
[JAVA微信扫码支付模式二功能实现完整例子](http://www.demodashi.com/demo/10268.html)





