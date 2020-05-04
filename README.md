
* So I have created a API which will approve the application of user on the basis of "Role". So if the role is "admin" then the application will be Approved or else any other role than the application will be Rejected.


* To Test the Example :

1) POST API : http://localhost:8080/startProcess
--> Request Body :
{
 "name":"Rushikesh Den",
 "role":"admin",
 "date":"04/05/2020"
}


--> Response Body :
Approved



--> Request Body :
{
 "name":"Rushikesh Den",
 "role":"employee",
 "date":"04/05/2020"
}

--> Response Body :
Rejected
