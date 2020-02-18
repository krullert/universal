[![Build Status](https://travis-ci.org/onsense/universal.svg?branch=master)](https://travis-ci.org/onsense/universal) [ ![Download](https://api.bintray.com/packages/onsense/universal/onsense-universal/images/download.svg) ](https://bintray.com/onsense/universal/onsense-universal/_latestVersion)

# universe

Java implementation of a complex expression evaluator backed by most Excel formula functions.

# USAGE
Expressions can be evaluated with or without arguments using the Universal class
```
Universal.evaluate("ISOWEEKNUM(DATE(2012,3,9))")
```
Or with arguments
```
Universal.evaluate("ISOWEEKNUM(DATE(y,m,d))",
                ImmutableMap.<String, Class<?>>builder()
                        .put("y", Integer.class)
                        .put("m", Integer.class)
                        .put("d", Integer.class)
                        .build(),
                Arrays.asList(2012, 3, 9))
```

## Usage with maven
Dependency:
```
<dependency>
  <groupId>io.onsense</groupId>
  <artifactId>universal</artifactId>
  <version>1.0.0</version>
  <type>jar</type>
</dependency>
```

Repository:
```
<repositories>
  <repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com/</url>
  </repository>
</repositories>
```

# LICENSE
Universal is freely distributable under the terms of the MIT license. Copyright (c) 2019 onsense.

# EXAMPLES
More examples:
```
AVEDEV(4,5,6,7,5,4,3) + 1337 - 10 * 2
```

```
IMSUB('13+4j', '5+3j')
```

```
ACCRINT(DATE(2012,2,2),DATE(2012,3,30),DATE(2013,12,4),0.1,1000,4,0,true)
```

```
STDEV.P([1345,1301,1368,1322,1310,1370,1318,1350,1303,1299])
```

```
WORKDAY(DATE(2008,10,1), 151, DATE(2008,11,26), DATE(2008,12,4), DATE(2009,1,21))
```
