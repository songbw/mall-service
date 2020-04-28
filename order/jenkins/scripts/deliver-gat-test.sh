#!/usr/bin/expect

set user smartadmin
set passwd Smartautotech@123
set host 117.78.8.30
set port 22120
set src_dir ./order/target/
set tag_dir /data/server/order/userapps
set name order-0.0.1-SNAPSHOT.jar
set tmp_dir /tmp

##拷贝jar文件到目标机器
spawn sh -c " scp -P $port -r $src_dir$name $user@$host:$tag_dir"
expect "password:"
send "${passwd}\n"
set timeout 30000
expect "$ "

##登录目标机器
spawn ssh $user@$host -p $port
expect "password:"
send "${passwd}\n"
expect "]$ "
send "cd $tag_dir\n"
expect "]$ "

##
send "rm $name\n"
expect "]$ "
send "mv $tmp_dir/$name .\n"
expect "]$ "


## 重启
send "cd ../bin/\n"
expect "]$ "
send "./stop.sh\n"
expect "]$ "
send "./start.sh\n"
expect "]$ "