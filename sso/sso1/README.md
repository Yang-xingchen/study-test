# 单点登录1
- page: 页面，需要登录的页面
- user-center: 用户中心，处理登录请求

# 流程
1. 打开页面，后端将cookie发送到user-center判断登录状态是否过期，若未过期，则结束。若过期，跳转登录页面[index.html](page1/src/main/resources/templates/index.html)。
2. 输入账号密码发送到后端，后端发送到user-center判断是否合法，若不合法，则结束。若合法，跳转至登录成功页面[home.html](page1/src/main/resources/templates/home.html)。
3. 登录成功页面获取当前cookie及所有页面列表后，打开iframe窗口，将cookie设置到所有页面上。