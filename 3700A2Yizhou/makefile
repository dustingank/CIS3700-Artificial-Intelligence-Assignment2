exp:
	javac *.java
	java ExptoCnf kb1 kb1cnf
	java ExptoCnf per1 per1cnf
	java ExptoCnf nq1 nq1cnf
	java ExptoCnf nq2 nq2cnf
	java ExptoCnf nq3 nq3cnf
	java ExptoCnf nq4 nq4cnf
res:
	javac *.java
	java Resolution kb1cnf nq1cnf per1cnf
	java Resolution kb1cnf nq2cnf per1cnf
	java Resolution kb1cnf nq3cnf per1cnf
	java Resolution kb1cnf nq4cnf per1cnf
clean:
	rm *.class

