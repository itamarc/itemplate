BIN_JAVA_PATH=/usr/local/jdk1.3.1/bin

all:
	${BIN_JAVA_PATH}/javac *java
	${BIN_JAVA_PATH}/jar cvf ITemplate.jar *class
install:
	mkdir -p ${DESTDIR}/usr/share/java/
	cp -v ITemplate.jar ${DESTDIR}/usr/share/java/
	@echo "Include in your CLASSPATH: ${DESTDIR}/usr/share/java/ITemplate.jar"
clean:
	rm *~
javadoc:
	mkdir -p ${DESTDIR}/usr/share/doc/libitemplate-java
	ln -sf ${DESTDIR}/usr/share/doc/libitemplate-java ${DESTDIR}/usr/doc/libitemplate-java
	${BIN_JAVA_PATH}/javadoc -d ${DESTDIR}/usr/share/doc/libitemplate-java -author -version -doctitle "ITemplate" -windowtitle "ITemplate" *java

