*** STEPS TO BUILD AND RUN PROJECT ***
--------------------------------------------------------
1) Clone Project using command "git clone https://ajithnair08@bitbucket.org/ajithnair08/cs441homework1.git"
2) Navigate to folder "Ajith_Nair_hw1" from command line
3) Execute command "gradle clean build"
4) Execute command "gradle test"
     The report of test cases execution is stored at path "~\build\reports\tests\test\index.html"
5) The application has 5 simulatiuons. "Booter.java" has been set as the main class in gradle. To run the simulations, please enter the below mentioned commands

	gradle run --args '0'			-> To execute simulation 0
	gradle run --args '1'			-> To execute simulation 1
	gradle run --args '2'			-> To execute simulation 2
	gradle run --args '3'			-> To execute simulation 3
	gradle run --args '4'			-> To execute simulation 4
	gradle run --args '5'			-> To execute simulation 5
	gradle run --args '6'			-> To execute simulation 6
	gradle run --args '7'			-> To execute simulation 7

6) Please refer to the document "ProjectReport.docx" for implementation details.