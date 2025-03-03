all: compileall

clean:
	@rm -rf $(DIR)/*.class

cleanall:
	@find . -type f -name "*.class" -delete

compile:
	@javac $(DIR)/*.java

compileall:
	@find . -type f -name "*.java" -exec javac {} +
