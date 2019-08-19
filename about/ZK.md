#docker中搭建zookeeper
拉取镜像
docker pull zookeeper

 

启动容器并添加映射
docker run --privileged=true -d --name zookeeper --publish 2181:2181 -d zookeeper:latest
查看是否启动
docker ps

[搭建docker 的ZK](https://www.cnblogs.com/kingkoo/p/8732448.html)
--------------
docker run --name my_zookeeper -d zookeeper:latest
这个命令会在后台运行一个 zookeeper 容器, 名字是 my_zookeeper, 并且它默认会导出 2181 端口.
接着我们使用:

1
docker logs -f my_zookeeper
