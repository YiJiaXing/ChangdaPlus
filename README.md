# 《长大plus》微信小程序  后端

做这款微信小程序的初衷是为了方便学生查询成绩、图书、学校新闻等信息。

## 一、主要的思路是：

### 1.模拟登陆教务系统

模拟登陆教务系统，使用的技术是**Jsoup组件**。

首先请求教务系统的登录页面，获得**cookie** ，并且获取主要的登录要素，比如：**用户名、密码**等。

然后携带第一次请求的cookie和登录要素值，请求登录成功后的首页。模拟登陆成功之后，将cookie存入到redis非关系型数据库中，并且设置过期时间（过期时间是，通过登录教务系统后多长时间不操作cookie失效来设置）

### 2.爬取学生的成绩、课程、考试安排、课表等信息

爬取这类信息的时候，采用了**webmagic框架**。技术文档：http://webmagic.io/docs/zh

**WebMagic框架**代码分为核心和扩展两部分。核心部分(**webmagic-core**)是一个精简的、模块化的爬虫实现，而扩展部分则包括一些便利的、实用性的功能。WebMagic的架构设计参照了**Scrapy**，目标是尽量的模块化，并体现爬虫的功能特点。

这部分提供非常简单、灵活的API，在基本不改变开发模式的情况下，编写一个爬虫。

扩展部分(**webmagic-extension**)提供一些便捷的功能，例如注解模式编写爬虫等。同时内置了一些常用的组件，便于爬虫开发。

爬取学生这些信息的时候，每次都需要携带cookie进行访问页面。

### 3.学校新闻信息的爬取

技术点同上，但是不需要携带cookie。

### 4.图书信息的爬取

图书信息的爬取相对来说比较复杂。

首先是携带书名请求学校图书馆的页面，然后爬取页面图书的名称、出版社、出版日期、作者、isbn号以及书的编号no。

然后查看书籍的详细信息的时候分为三个部分：

#### 1.馆藏信息的获取

通过书的编号no，请求馆藏页面然后获取基本的信息

#### 2.书籍信息

通过no请求书籍信息页面获取书籍的基本信息

#### 3.书籍信息（豆瓣图书）

学校的书籍信息有的不是特别全面，因此我们想通过书的isbn号去豆瓣图书上获取书籍信息。

这里需要解决豆瓣图书内部编号的获取，这里需要进行编码的解密。我们参考了一篇博客，通过执行js的方式获得书的内部编号。

首先是通过isbn号获取密码的密文，然后将密文传送到js里面进行解密从而获得该书籍在豆瓣图书内部的编号，最后通过该编号请求书籍详细页面获取书籍的信息。

豆瓣图书的信息获取中，速度比较慢。（原因是执行外部js）

## 二、项目主要的框架

**mybaits框架**

**springboot框架**

**webmagic框架**

本项目主要使用了以上框架进行开发。除此之外还设置了邮件服务，只要程序出现异常就会通过邮件发送给开发人员。

## 三、项目页面展示

![图片1](C:\Users\YJX\Desktop\图片1.png)