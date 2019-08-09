#!/usr/bin/expect

set user smartadmin
set passwd Smartautotech@123
set host 192.168.200.122
set port 22
set src_dir ./statistics/target/
set tag_dir /data/server/statistics/userapps/
set name statistics-0.0.1-SNAPSHOT.jar
set tmp_dir /tmp

spawn sh -c " scp -P $port -r $src_dir$name $user@$host:$tag_dir$tmp_dir"
expect "password:"
send "${passwd}\n"
set timeout 30000
expect "$ "

spawn ssh $user@$host -p $port
expect "password:"
send "${passwd}\n"
expect "]$ "
send "cd $tag_dir\n"
expect "]$ "
send "rm $name\n"
expect "]$ "
send "mv $tag_dir$tmp_dir/$name .\n"
expect "]$ "

send "cd ../bin/\n"
expect "]$ "

send "./stop.sh\n"
expect "]$ "
send "./start.sh\n"
expect "]$ "