memoizer
========

To compile and execute:

`mvn clean compile assembly:single` - it produces executable jar with dependencies inside<br/>
or<br/>
`mvn clean package` - produces executable jar and folder lib with dependencies

Compilation requires memoizer-plugins project as dependency, which can be found here:<br/>
https://github.com/io-project/memoizer-plugins

To install a plugin simply copy compiled jar to plugins (create it in case it does not exists) directory in execution path of memoizer.

Some implementations of plugins are here:<br/>
https://github.com/io-project/memoizer-plugins-Komixxy<br/>
https://github.com/io-project/memoizer-plugins-Kwejk<br/>
https://github.com/io-project/memoizer-plugins-Demoty<br/>
