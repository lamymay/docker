# 


对Dockerfile里面的内容做一个简单的介绍:
到这里我们对于代码的修改就完成了,下面我们在idea的命令行执行maven命令,或者点击idea的maven插件中的docker:build

  
#1.FROM:指明当前镜像继承的基镜像,编译当前镜像时候会自动下载基镜像
#2.MAINTAINER:当前镜像的作者和邮箱,使用空格隔开
#3.VOLUME:挂载目录
#4.ADD:从当前工作目录复制文件到镜像目录中并重新命名
#5.RUN:在当前镜像上执行Linux命令,这里我执行了2个run指令,第二个run指令是为了解决容器和宿主机时间不一致的问题
#6.EXPOSE:监听的端口号
#7.ENTRYPOINT:让容器像一个可执行程序一样运行


https://download.docker.com/win/stable/Docker%20for%20Windows%20Installer.exe


docker使用

编译方式：dockerfile
Linux镜像：centos7
jdk：jdk1.8.0_144
这里要构建一个基于centos7和jdk1.8的镜像。

```$text

1. 查找centos7镜像
docker search centos

2. 下载centos7镜像
docker pull centos:7

3. 查看本地下载好的centos7镜像
docker images

4. 编译Dockerfile

新建一个文件，这里命名为 jdkdockerfile
很多地方都是使用的Dockerfile这种固定名称，其实创建的时候可以通过 -f 来指定dockerfile
命令：
vi jdkdockerfile

jdkdockerfile 中的内容：

FROM centos:7
MAINTAINER lamymay lamymay@outlook.com
ADD jdk-8u181-linux-x64.tar.gz /usr/local/

ENV JAVA_HOME /usr/local/jdk_8u181_linux_x64
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin

注意：
1、这里使用的镜像是上面下载的centos镜像； 
2、jdk拷贝到dockerfile同级目录，如果在其它目录拷贝的时候可能出现找不到目录错误； 
3、使用ADD指令会直接对 jdk-8u181-linux-x64.tar.gz  进行解压缩，不用再单独的tar解压jdk了


一键清除本地缓存的所有无用的docker镜像命令：
docker images -q --filter "dangling=true" | xargs -t --no-run-if-empty docker rmi
5. 使用Dockerfile创建镜像


docker build -t  centos7_jdk_8u181_linux_x64:20190731 . -f jdkdockerfile
docker build -t  centos7_jdk_8:8 . -f jdkdockerfile
正确：docker save <repository>:<tag> -o <repository>.tar
错误：docker save <IMAGE ID> -o <repository>.tar（会导致载入镜像后名字标签都为<none>）










https://www.cnblogs.com/lsgxeva/p/8746644.html

https://www.jianshu.com/p/59e0a8828b3b
容器是基于镜像创建的，执行镜像生成容器，方可进入容器
启动容器命令: docker run <相关参数> <镜像 ID> <初始命令>


docker run -i -t -v /root/software/:/mnt/software/ 4cbf48630b46 /bin/bash






6. 删除刚刚生成的镜像images，通过image的名称和tag来指定删除谁 
 语法：docker rmi <imagename:tag>
 语法：docker rmi  -f <imagename:tag>
 
命令例子
docker rmi centos7_jdk_8u181_linux_x64:20190731

6.1 注意如果是正在运行的镜像，则：先删除容器后删除仓库中镜像

先查看正在运行中的镜像
docker ps -a
会显示：

[root@localhost data]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                    PORTS               NAMES
8a6b5cc82262        redis               "docker-entrypoint..."   2 months ago        Exited (0) 2 months ago                       tender_euler
d1cb80c01dc4        redis               "docker-entrypoint..."   2 months ago        Exited (1) 2 months ago                       zen_darwin
83a03933c8c0        redis               "docker-entrypoint..."   2 months ago        Exited (1) 2 months ago                       eager_panini
8e5651fe0454        redis               "docker-entrypoint..."   2 months ago        Exited (0) 2 months ago                       agitated_lovelace

在删除: 语法docker rm  <CONTAINER ID >
命令例子：
docker rm  8a6b5cc82262


7. docker创建好的镜像导出,docker镜像保存save、加载load
查看要要保存的镜像的ID
docker images

保存镜像：语法 docker save  <imageName>  -o  <outputFile>

docker save  centos7_jdk_8u181_linux_x64  -o  /data/docker/centos7_jdk_8u181_linux_x64


至此，基础镜像已经完成了，并且怎么拷贝使用都交给你了，下一步就是把我们的app部署到镜像中，运行这个镜像，把我们的服务跑起来
8. 将创建好的Dockerfile文件和我们的程序jar包上传到服务器，放在同一文件夹下,进入这个文件夹,
执行命令：
docker build -t  app .

注意：后面末尾有一个空格和一个“.”，mystory是创建的镜像的名字，“.”表示当前目录

9. 运行
docker run -p 8001:8080  -d  <imgaeName>

-p 指定端口号 第一个端口号8001为容器内部的端口号，第二个8080为外界访问的端口号，将容器内的8001端口号映射到外部的8080端口号
如果想用域名来访问的话，需要把数据库连接改为服务器的ip+数据库端口号，并且命令改为：docker run -d -p 80:80 mystory

-d表示在后台运行


docker run -p 8001:8080  -d app1
docker run -di --name=app1 -p 8001:8080 

```

附：


FROM ：表示使用 Jdk8 环境 为基础镜像，如果镜像不是本地的会从 DockerHub 进行下载
MAINTAINER ：指定维护者的信息
VOLUME ：VOLUME 指向了一个/tmp的目录，由于 Spring Boot 使用内置的Tomcat容器，Tomcat 默认使用/tmp作为工作目录。这个命令的效果是：在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
ADD ：拷贝文件并且重命名(前面是上传jar包的名字，后面是重命名)
RUN ：每条run指令在当前基础镜像执行，并且提交新镜像
ENTRYPOINT ：为了缩短 Tomcat 的启动时间，添加java.security.egd的系统属性指向/dev/urandom作为 ENTRYPOINT



1.搭建docker环境
需要linux系统必须是centOS7以上

准备安装环境，执行一下命令：
yum install epel-release –y
yum clean all
yum list

2.安装
yum install docker-io –y

3.启动
systemctl start docker

4.测试docker是否成功
docker info



vi中的快捷键
x        删除当前光标下的字符
dw       删除光标之后的单词剩余部分。
d$       删除光标之后的该行剩余部分。
dd       删除当前行。
c        功能和d相同，区别在于完成删除操作后进入INSERT MODE
cc       也是删除当前行，然后进入INSERT MODE
删除每行第一个字符    :%s/^.//g



Linux查看文件夹大小
du -sh 查看当前文件夹大小
du -sh * | sort -n 统计当前文件夹(目录)大小，并按文件大小排序
du -sk filename 查看指定文件大小单位是K
du -skh filename 查看指定文件大小, 单位是M


ERROR
# docker run -p 8001:8080  -d app1
WARNING: IPv4 forwarding is disabled. Networking will not work.
647888a6dad3801347a4c29fcd07c1793ffa9c2c47380f53401c968063eeb802

启动docker时映射到宿主机时出现/usr/bin/docker-current: Error response from daemon: oci runtime error: container_linux.go:235: 
starting container process caused "exec: \"java\": executable file not found in $PATH".



-----------
启动docker时映射到宿主机时出现 /usr/bin/docker-current: Error response from daemon: driver failed……的解决方案

https://www.clxz.top/2019/03/31/111040/





-----------


# 下载软件为了在WIN上运作docker
https://download.docker.com/win/stable/Docker%20for%20Windows%20Installer.exe
#尝试使用 Kitematic 可视化管理 Docker
Kitematic 是什么
Kitematic是一个 Docker GUI 工具，它可以更快速、更简单的运行Docker，现在已经支持 Mac 和 Windows。Kitematic 目前在 Github 上开源，而它也早在 2015 年就已经被 Docker 收购。Kitematic 完全自动化了 Docker 安装和设置过程，并提供了一个直观的图形用户接口（GUI）来运行 Docker。通过 GUI 你可以非常容易的创建、运行和管理你的容器，不需要使用命令行或者是在 Docker CLI 和 GUI之间来回切换；同时也可以方便的修改环境变量、查看日志以及配置数据卷等。

state 和 status 的区别：

一、意思不同
state的意思是状态，Status不仅有状态也有身份的意思。

二、使用环境不同
state：比较常用，各种状态都可以用它，但是它更着重于一种心理状态或者物理状态。

status 用在人的身上一般是其身份和地位，状态时指的是政治和商业。



state：比较常用，各种状态都可以用它，但是它更着重于一种心理状态或者物理状态。
Status：用在人的身上一般是其身份和地位，作“状态，情形”讲时，多指政治和商业。
state倾向于condition，是一种延续性的状态。status常用于描述一个过程中的某阶段（phase），类似于C语言中枚举型变量某一个固定的值，这个值属于一个已知的集合。
比如淘宝买家问卖家“我的网购现在是什么状况？”
这个问题的背景是讲话双方都清楚，交易状态有“买家选购”“买家已付款”“卖家已发货”“买家已签收”或者有“买家已
投诉”等等状态。这些状态描述一件事情发展过程中的不同阶段。而且，这些阶段的先后顺序也是双方默许的。
所以在这里可以问“What's the status of my purchase?”，此处用state不太贴切，如果硬用上去从语感上可能听着别扭。
说物态变化用state再恰当不过。如果说一个物质的四种状态，可以说“solid state”，但如果你说“solid status”，第
一，这两个词的组合不像是描述物态，更像是在说“确定的状况（solid产生歧义‘确定的/确凿的’）”；第二，这个说法即
使不被误解，也需要事先约定一组物态变化顺序，比如把这个物质从固态开始加热然后电离，可能先后经历固态、液态、气态、等离子态这四个阶段。类似先定义枚举，然后引用的方式。
扩展：
ajax中readyState，statusText，onreadystatechange，window.status怎么一会state一会是status都晕乎了
state所指的状态，一般都是有限的、可列举的，status则是不可确定的。
比如
readyState -- 就那么四五种值
statusText -- 描述性的文字，可以任意
onreadystatechange -- 那么四五种值之间发生变化
window.status -- 描述性的文字，可以任意
来个形象的比方，你体重多少公斤，属于status，但说你体重属于偏瘦、正常还是偏胖，那就是state.