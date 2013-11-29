memoizer
========

To compile and execute:

mvn clean compile assembly:single - it produces executable jar with dependencies inside
or
mvn clean package - produces executable jar and folder lib with dependencies

Compilation requires memoizer-plugins project as dependency, which can be found here:
https://github.com/Ziemin/memoizer-plugins

To install a plugin simply copy compiled jar to plugins (create it in case it does not exists) directory in execution path of memoizer.

Some plugins implementation are here:
https://github.com/sokar92/memoizer-plugins-Komixxy
https://github.com/sokar92/memoizer-plugins-Kwejk
https://github.com/sokar92/memoizer-plugins-Demoty