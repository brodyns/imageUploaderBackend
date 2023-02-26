## 后端流程
* uploading image to a static folder in the server
* downloading image from server with the link
* display list of image

1. 在服务器端创建图片文件夹
2. 接收到的文件保存到图片文件夹
3. 返回图片链接

## 后端实现
* 各种图片操作接口   添加、查询、删除
* 静态资源路径配置 - 访问权限设置
* Docker容器化部署

## 待实现
* 图片操作接口 - 删除、 按label查询
* Docker容器化部署
  
## 正在做
* 图片操作接口   添加

## Java实现
  资源：https://www.bezkoder.com/spring-boot-image-upload-thymeleaf/
  文件处理类：MultipartFile

Service类实现思路：
* 设置接收文件路径
* 创建文件路径
* 保存文件

图片对象：Image
属性： label、URL。

## 遇到的问题
* 问题1：
>WARN 17996 --- [nio-8080-exec-6] s.w.m.s.StandardServletMultipartResolver : 
>Failed to perform cleanup of multipart items

