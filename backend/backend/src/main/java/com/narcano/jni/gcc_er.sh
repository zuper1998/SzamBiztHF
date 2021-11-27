FILES="file1
/path/to/file2
/etc/resolv.conf"
for f in *.cpp
do
	echo "gcc -c $f -fPIC -o $f "
done