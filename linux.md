
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
