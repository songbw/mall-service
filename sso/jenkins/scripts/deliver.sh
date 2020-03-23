#!/usr/bin/expect

set user smartadmin
set passwd Smartautotech@123
set host 121.36.52.130
set port 22051
set src_dir ./sso/target/
set tag_dir /data/server/sso/userapps
set name sso-0.0.1-SNAPSHOT.jar
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
##send "rm $name\n"
##expect "]$ "
##send "mv $tag_dir$tmp_dir/$name .\n"
##expect "]$ "


## 重启
send "cd ../bin/\n"
expect "]$ "
send "./stop.sh\n"
expect "]$ "
send "./start.sh\n"
expect "]$ "