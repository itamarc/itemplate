JDK_HOME?=/usr/local/jdk1.3.1
JAVA_HOME?=$(JDK_HOME)
BIN_JAVA_PATH=$(JAVA_HOME)/bin

all:
	${BIN_JAVA_PATH}/javac -d . *java
	${BIN_JAVA_PATH}/jar cvf ITemplate.jar org
install:
	mkdir -p ${DESTDIR}/usr/share/java/
	cp -v ITemplate.jar ${DESTDIR}/usr/share/java/
	@echo "######################################################################"
	@echo "# Include in your CLASSPATH: ${DESTDIR}/usr/share/java/ITemplate.jar"
	@echo "######################################################################"
clean:
	#rm *~
	rm *.class
javadoc:
	mkdir -p ${DESTDIR}/usr/share/doc/libitemplate-java
	ln -sf ${DESTDIR}/usr/share/doc/libitemplate-java ${DESTDIR}/usr/doc/libitemplate-java
	${BIN_JAVA_PATH}/javadoc -d ${DESTDIR}/usr/share/doc/libitemplate-java -author -version -doctitle "ITemplate" -windowtitle "ITemplate" *java

