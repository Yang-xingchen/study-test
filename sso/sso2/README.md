# 单点登录2
- page: 页面，需要登录的页面
- user-center: 用户中心，处理登录请求

# 流程
1. 打开页面，后端将cookie发送到user-center判断登录状态是否过期，若未过期，则结束。若过期，重定向到user-center登录页面[login.html](userCenter%2Fsrc%2Fmain%2Fresources%2Ftemplates%2Flogin.html)，携带回调地址。
2. 输入账号密码发送到后端，后端判断是否合法，若不合法，则结束。若合法，则打开iframe窗口，将cookie设置到所有页面上。
3. 一段时间后，重定向到传递过来的回调地址。