# The Play data validation library

Play new validation API extracted from Play.

## Using the validation api in your project

Add the following lines in your `build.sbt`

```scala
resolvers += "JTO snapshots" at "https://raw.github.com/jto/mvn-repo/master/snapshots"

libraryDependencies +="io.github.jto" %% "validation-core" % "1.0-1c770f4"
```

## Documentation

```bash
cd documentation
sbt -Dplay.version=2.3.0-RC1 run
# browse http://localhost:9000/@documentation/ScalaValidation
```

## Contributors

- Julien Tournay - http://jto.github.io
- Nick - https://github.com/stanch
- Ian Hummel - https://github.com/themodernlife
- Arthur Gautier - https://github.com/baloo
