# ITemplate Java
_Fill in text templates with variable content_

(a.k.a. org.gjt.itemplate)

This  library  was created  by  Itamar  Carvalho  and  was inspired by Perl Text::Template module (see http://www.cpan.org).

This project was once hosted in the Giant Java Tree site (http://www.gjt.org).

For more information, see the [README](https://github.com/itamarc/itemplate/blob/master/doc/README) file in "doc" folder.

## :gear: Maven

This library is on Maven Central Repository (through Sonatype.org).
To use this library in a Maven project, you can use this code in your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.itamarc</groupId>
    <artifactId>itemplate</artifactId>
    <version>1.2</version>
</dependency>
```

## :white_check_mark: Unit Tests

To generate a report of code coverage of the unit tests it's possible to use JaCoCo (https://github.com/jacoco).

It's already properly configured in ITemplate's `pom.xml` and you can generate the report using a command like this:

    mvn clean jacoco:prepare-agent test jacoco:report
