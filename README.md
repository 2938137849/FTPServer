# FTPServer

- 使用 `maven` + `tomcat` + `jsp` + `vue` 简易实现一个 web 服务器与存储服务器分离的项目
- [项目测试地址](http://122.51.77.15/)

## 配置

1. 在 `FTPServer/src/main/resources/` 文件夹下创建 `config.properties` 文件
2. 在 `config.properties` 文件中按照如下配置填写：

```properties
FTP.ip=FTP服务器地址(ip或网址)
FTP.port=端口
FTP.username=用户名
FTP.password=密码
```

3. 在 `FTPServer` 目录下执行打包命令 `mvn war:war`
4. 发送到服务器应用目录下加载运行

## 功能简介

- 为文件时，前端发送 `file=${fileName}` 请求到后端 jsp 页面，后端 jsp 页面连接 FTP 服务器查找该文件，若找到，则返回下载的文件；若未找到，无返回值；
- 为文件夹时，点击进入下一层文件夹；

## 注意事项

- 注：由于前后端传输时使用的仍然是 `jackson` 所以在 json 与 pojo 互相转换时可能会很耗时；
- 注：由于采用了一次请求新建一次 `FTPClient` 的方式，并未使用任何缓存技术，并发时效率将会极低；
