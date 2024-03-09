# 启动
1.  图片上传nginx配置
图片存储地址："D:/nginx-1.22.1/html/"
nginx配置：
```
http {

    server {
        listen       80;
        server_name  localhost;
        location / {
            root   html;
            index  index.html index.htm;
        }
    }
}
```
2. 支付宝回调地址内网穿透
使用[natapp](https://natapp.cn/)进行内网穿透
[NATAPP使用教程](https://blog.csdn.net/weixin_42601136/article/details/108836388?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522168560864816800227447787%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=168560864816800227447787&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~hot_rank-3-108836388-null-null.142%5Ev88%5Econtrol_2,239%5Ev2%5Einsert_chatgpt&utm_term=natapp&spm=1018.2226.3001.4187)

> 注意每次启动都会获得新的公网地址，需要更改application.yml中`alipay.notifyUrl`
>