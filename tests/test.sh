# 1
echo "Test get user by login"
curl --header "content-type: text/xml" -d @request1.xml localhost:8080/ws/ > response1.xml 

# 2
echo "Test get user by id"
curl --header "content-type: text/xml" -d @request2.xml localhost:8080/ws/ > response2.xml 

# 3
echo "Test get all users"
curl --header "content-type: text/xml" -d @request3.xml localhost:8080/ws/ > response3.xml 

# 4
echo "Test add user"
curl --header "content-type: text/xml" -d @request4.xml localhost:8080/ws/ > response4.xml 

# 5
echo "Test modify user"
curl --header "content-type: text/xml" -d @request5.xml localhost:8080/ws/ > response5.xml 

# 6
echo "Test delete user by id" 
curl --header "content-type: text/xml" -d @request6.xml localhost:8080/ws/ > response6.xml

# 7
echo "Test delete user by login"
curl --header "content-type: text/xml" -d @request7.xml localhost:8080/ws/ > response7.xml


# Проверяем все операции
echo "Check all results"
curl --header "content-type: text/xml" -d @request3.xml localhost:8080/ws/ > result.xml 
