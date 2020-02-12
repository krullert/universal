grammar Universal;

universal
   : (LPAREN*) (expression | equation) (RPAREN*)
   ;

equation
   : (LPAREN*) expression relop expression (RPAREN*)
   ;

expression
   : multiplyingExpression ((AMPERSAND | PLUS | MINUS) multiplyingExpression)*
   ;

multiplyingExpression
   : powExpression ((TIMES | DIV) powExpression)*
   ;

powExpression
   : signedAtom (POW signedAtom)*
   ;

signedAtom
   : PLUS signedAtom
   | MINUS signedAtom
   | func
   | atom
   ;

atom
   : string
   | scientific
   | variable
   | constant
   | LPAREN expression RPAREN
   ;

string
   : STRING
   ;

scientific
   : SCIENTIFIC_NUMBER
   ;

constant
   : PI
   | EULER
   | I
   | TRUE
   | FALSE
   ;

variable
   : VARIABLE
   ;

func
   : funcname LPAREN ((equation | expression) (COMMA (equation | expression))*)? RPAREN
   ;

funcname
   // math & trig
   : ABS
   | ACOS
   | ACOSH
   | ACOT
   | ACOTH
   | AGGREGATE
   | ARABIC
   | ASIN
   | ASINH
   | ATAN
   | ATAN2
   | ATANH
   | BASE
   | CEILING
   | CEILING_MATH
   | CEILING_PRECISE
   | COMBIN
   | COMBINA
   | COS
   | COSH
   | COT
   | COTH
   | CSC
   | CSCH
   | DECIMAL
   | DEGREES
   | EVEN
   | EXP
   | FACT
   | FACTDOUBLE
   | FLOOR
   | FLOOR_MATH
   | FLOOR_PRECISE
   | GCD
   | INT
   | ISO_CEILING
   | LCM
   | LN
   | LOG
   | LOG2
   | LOG10
   | MDETERM
   | MINVERSE
   | MMULT
   | MOD
   | MROUND
   | MULTINOMIAL
   | MUNIT
   | ODD
   | PI
   | POWER
   | PRODUCT
   | QUOTIENT
   | RADIANS
   | RAND
   | RANDBETWEEN
   | ROMAN
   | ROUND
   | ROUNDDOWN
   | ROUNDUP
   | SEC
   | SECH
   | SERIESSUM
   | NUMSIGN
   | SIN
   | SINH
   | SQRT
   | SQRTPI
   | SUBTOTAL
   | SUM
   | SUMIF
   | SUMIFS
   | SUMPRODUCT
   | SUMSQ
   | SUMX2MY2
   | SUMX2PY2
   | SUMXMY2
   | TAN
   | TANH
   | TRUNC
   // TODO category
   | CBRT
   | HYPOT
   | ROOT
   | FACTORIAL
   | BERNOULLI
   | MANTISSA
   | INTEGRAL
   | FRACTION
   | AVG
   | MEAN
   | RANGE
   | MODE
   | MULTIPLY
   | DIVIDE
   // textual
   | ASC
   | BAHTTEXT
   | CHAR
   | CLEAN
   | CODE
   | CONCAT
   | CONCATENATE
   | DBCS
   | DOLLAR
   | EXACT
   | FIND
   | FINDB
   | FIXED
   | LEFT
   | LEFTB
   | LEN
   | LENB
   | LOWER
   | MID
   | MIDB
   | NUMBERVALUE
   | PHONETIC
   | PROPER
   | REPLACE
   | REPLACEB
   | REPT
   | RIGHT
   | RIGHTB
   | SEARCH
   | SEARCHB
   | SUBSTITUTE
   | T
   | TEXT
   | TEXTJOIN
   | TRIM
   | UNICHAR
   | UNICODE
   | UPPER
   | VALUE
   // additional textual
   | FORMAT
   | LTRIM
   | RTRIM
   | CHOP
   | REPEAT
   | PREFIX
   | SUFFIX
   | CAPITALIZE
   | CAPITALIZE_FULLY
   | DECAPITALIZE
   | DECAPITALIZE_FULLY
   | SUBSTRING
   | REMOVE
   | REMOVE_FIRST
   | REMOVE_LAST
   | REPLACE_FIRST
   | REPLACE_LAST
   | REVERSE
   | INITIALS
   | SWAP
   | QUOTE
   | UNQUOTE
   | ENCODE
   | DECODE
   | RANDOM
   | ATOB
   | BTOA
   | HEX
   | MD2
   | MD5
   | SHA256
   | CRC32
   | HTML2TEXT
   // logical
   | AND
   | FALSE
   | IF
   | IFERROR
   | IFNA
   | IFS
   | NOT
   | OR
   | SWITCH
   | TRUE
   | XOR
   // additional logical
   | ISNULL
   | ISNOTNULL
   // date & time
   | DATE
   | DATEDIF
   | DATEVALUE
   | DAY
   | DAYS
   | DAYS360
   | EDATE
   | EOMONTH
   | HOUR
   | ISOWEEKNUM
   | MINUTE
   | MONTH
   | NETWORKDAYS
   | NETWORKDAYS_INTL
   | NOW
   | SECOND
   | TIME
   | TIMEVALUE
   | TODAY
   | WEEKDAY
   | WEEKNUM
   | WORKDAY
   | WORKDAY_INTL
   | YEAR
   | YEARFRAC
   // additional date & time
   | WEEK
   | TIMENOW
   | DATETIMEVALUE
   // engineering
   | BESSELI
   | BESSELJ
   | BESSELK
   | BESSELY
   | BIN2DEC
   | BIN2HEX
   | BITAND
   | BITLSHIFT
   | BITOR
   | BITRSHIFT
   | BITXOR
   | COMPLEX
   | CONVERT
   | DEC2BIN
   | DEC2HEX
   | DEC2OCT
   | DELTA
   | ERF
   | ERF_PRECISE
   | ERFC
   | ERFC_PRECISE
   | GESTEP
   | HEX2BIN
   | HEX2DEC
   | HEX2OCT
   | IMABS
   | IMAGINARY
   | IMARGUMENT
   | IMCONJUGATE
   | IMCOS
   | IMCOSH
   | IMCOT
   | IMCSC
   | IMCSCH
   | IMDIV
   | IMEXP
   | IMLN
   | IMLOG10
   | IMLOG2
   | IMPOWER
   | IMPRODUCT
   | IMREAL
   | IMSEC
   | IMSECH
   | IMSIN
   | IMSINH
   | IMSQRT
   | IMSUB
   | IMSUM
   | IMTAN
   | OCT2BIN
   | OCT2DEC
   | OCT2HEX
   // additional engineering
   | BIN2OCT
   // financial
   | ACCRINT
   | ACCRINTM
   | AMORDEGRC
   | AMORLINC
   | COUPDAYBS
   | COUPDAYS
   | COUPDAYSNC
   | COUPNCD
   | COUPNUM
   | COUPPCD
   | CUMIPMT
   | CUMPRINC
   | DB
   | DDB
   | DISC
   | DOLLARDE
   | DOLLARFR
   | DURATION
   | EFFECT
   | FV
   | FVSCHEDULE
   | INTRATE
   | IPMT
   | IRR
   | ISPMT
   | MDURATION
   | MIRR
   | NOMINAL
   | NPER
   | NPV
   | ODDFPRICE
   | ODDFYIELD
   | ODDLPRICE
   | ODDLYIELD
   | PDURATION
   | PMT
   | PPMT
   | PRICE
   | PRICEDISC
   | PRICEMAT
   | PV
   | RATE
   | RECEIVED
   | RRI
   | SLN
   | SYD
   | TBILLEQ
   | TBILLPRICE
   | TBILLYIELD
   | VDB
   | XIRR
   | XNPV
   | YIELD
   | YIELDDISC
   | YIELDMAT
   // database
   | DAVERAGE
   | DCOUNT
   | DCOUNTA
   | DGET
   | DMAX
   | DMIN
   | DPRODUCT
   | DSTDEV
   | DSTDEVP
   | DSUM
   | DVAR
   | DVARP
   // cube
   | CUBEKPIMEMBER
   | CUBEMEMBER
   | CUBEMEMBERPROPERTY
   | CUBERANKEDMEMBER
   | CUBESET
   | CUBESETCOUNT
   | CUBEVALUE
   // information
   | CELL
   | ERROR_TYPE
   | INFO
   | ISBLANK
   | ISERR
   | ISERROR
   | ISEVEN
   | ISFORMULA
   | ISLOGICAL
   | ISNA
   | ISNONTEXT
   | ISNUMBER
   | ISODD
   | ISREF
   | ISTEXT
   | N
   | NA
   | SHEET
   | SHEETS
   | TYPE
   // lookup & reference
   | ADDRESS
   | AREAS
   | CHOOSE
   | COLUMN
   | COLUMNS
   | FORMULATEXT
   | GETPIVOTDATA
   | HLOOKUP
   | HYPERLINK
   | INDEX
   | INDIRECT
   | LOOKUP
   | MATCH
   | OFFSET
   | ROW
   | ROWS
   | RTD
   | TRANSPOSE
   | VLOOKUP
   // statistical
   | AVEDEV
   | AVERAGE
   | AVERAGEA
   | AVERAGEIF
   | AVERAGEIFS
   | BETA_DIST
   | BETA_INV
   | BINOM_DIST
   | BINOM_DIST_RANGE
   | BINOM_INV
   | CHISQ_DIST
   | CHISQ_DIST_RT
   | CHISQ_INV
   | CHISQ_INV_RT
   | CHISQ_TEST
   | CONFIDENCE_NORM
   | CONFIDENCE_T
   | CORREL
   | COUNT
   | COUNTA
   | COUNTBLANK
   | COUNTIF
   | COUNTIFS
   | COVARIANCE_P
   | COVARIANCE_S
   | DEVSQ
   | EXPON_DIST
   | F_DIST
   | F_DIST_RT
   | F_INV
   | F_INV_RT
   | F_TEST
   | FISHER
   | FISHERINV
   | FORECAST
   | FORECAST_ETS
   | FORECAST_ETS_CONFINT
   | FORECAST_ETS_SEASONALITY
   | FORECAST_ETS_STAT
   | FORECAST_LINEAR
   | FREQUENCY
   | GAMMA
   | GAMMA_DIST
   | GAMMA_INV
   | GAMMALN
   | GAMMALN_PRECISE
   | GAUSS
   | GEOMEAN
   | GROWTH
   | HARMEAN
   | HYPGEOM_DIST
   | INTERCEPT
   | KURT
   | LARGE
   | LINEST
   | LOGEST
   | LOGNORM_DIST
   | LOGNORM_INV
   | MAX
   | MAXA
   | MAXIFS
   | MEDIAN
   | MIN
   | MINA
   | MINIFS
   | MODE_MULT
   | MODE_SNGL
   | NEGBINOM_DIST
   | NORM_DIST
   | NORM_INV
   | NORM_S_DIST
   | NORM_S_INV
   | PEARSON
   | PERCENTILE_EXC
   | PERCENTILE_INC
   | PERCENTRANK_EXC
   | PERCENTRANK_INC
   | PERMUT
   | PERMUTATIONA
   | PHI
   | POISSON_DIST
   | PROB
   | QUARTILE_EXC
   | QUARTILE_INC
   | RANK_AVG
   | RANK_EQ
   | RSQ
   | SKEW
   | SKEW_P
   | SLOPE
   | SMALL
   | STANDARDIZE
   | STDEV_P
   | STDEV_S
   | STDEVA
   | STDEVPA
   | STEYX
   | T_DIST
   | T_DIST_2T
   | T_DIST_RT
   | T_INV
   | T_INV_2T
   | T_TEST
   | TREND
   | TRIMMEAN
   | VAR_P
   | VAR_S
   | VARA
   | VARPA
   | WEIBULL_DIST
   | Z_TEST
   // custom
   // code
   | CODE_ISIBAN
   | CODE_IBAN
   | CODE_IBAN_CD
   | CODE_ISISIN
   | CODE_ISIN
   | CODE_ISIN_CD
   | CODE_ISISSN
   | CODE_ISSN
   | CODE_ISSN_CD
   | CODE_ISISBN
   | CODE_ISBN
   | CODE_ISBN_CD
   | CODE_ISISBN10
   | CODE_ISBN10
   | CODE_ISBN10_CD
   | CODE_ISISBN13
   | CODE_ISBN13
   | CODE_ISBN13_CD
   | CODE_ISEAN
   | CODE_EAN
   | CODE_EAN_CD
   | CODE_ISEAN8
   | CODE_EAN8
   | CODE_EAN8_CD
   | CODE_ISEAN13
   | CODE_EAN13
   | CODE_EAN13_CD
   | CODE_ISUPCA
   | CODE_UPCA
   | CODE_UPCA_CD
   | CODE_ISUPCE
   | CODE_UPCE
   | CODE_UPCE_CD
   | CODE_ISSAFEHTML
   | CODE_ISEMAIL
   | CODE_ISREGEX
   | CODE_ISIP
   | CODE_ISIPV4
   | CODE_ISIPV6
   | CODE_ISURL
   | CODE_ISURN
   | CODE_ISDN
   | CODE_ISTLD
   | CODE_ISCREDITCARD
   | CODE_ISMOD10
   | CODE_ISMOD11
   | CODE_ISCNPJ
   | CODE_ISCPF
   | CODE_ISTITULO_ELEITORAL
   | CODE_ISNIP
   | CODE_ISPESEL
   | CODE_ISREGON
   | CODE_ISJSON
   | CODE_ISXML
   | CODE_ISYAML
   ;

relop
   : EQ
   | GT
   | GTE
   | LT
   | LTE
   | NEQ
   ;

// math & trig
ABS
    : 'ABS'
    ;


ACOS
    : 'ACOS'
    ;


ACOSH
    : 'ACOSH'
    ;


ACOT
    : 'ACOT'
    ;


ACOTH
    : 'ACOTH'
    ;


AGGREGATE
    : 'AGGREGATE'
    ;


ARABIC
    : 'ARABIC'
    ;


ASIN
    : 'ASIN'
    ;


ASINH
    : 'ASINH'
    ;


ATAN
    : 'ATAN'
    ;


ATAN2
    : 'ATAN2'
    ;


ATANH
    : 'ATANH'
    ;


BASE
    : 'BASE'
    ;


CEILING
    : 'CEIL' | 'CEILING'
    ;


CEILING_MATH
    : 'CEILING.MATH'
    ;


CEILING_PRECISE
    : 'CEILING.PRECISE'
    ;


COMBIN
    : 'COMBIN'
    ;


COMBINA
    : 'COMBINA'
    ;


COS
    : 'COS'
    ;


COSH
    : 'COSH'
    ;


COT
    : 'COT'
    ;


COTH
    : 'COTH'
    ;


CSC
    : 'CSC'
    ;


CSCH
    : 'CSCH'
    ;


DECIMAL
    : 'DECIMAL'
    ;


DEGREES
    : 'DEG' | 'DEGREES'
    ;


EVEN
    : 'EVEN'
    ;


EXP
    : 'EXP'
    ;


FACT
    : 'FACT'
    ;


FACTDOUBLE
    : 'FACTDOUBLE'
    ;


FLOOR
    : 'FLOOR'
    ;


FLOOR_MATH
    : 'FLOOR.MATH'
    ;


FLOOR_PRECISE
    : 'FLOOR.PRECISE'
    ;


GCD
    : 'GCD'
    ;


INT
    : 'INT'
    ;


ISO_CEILING
    : 'ISO.CEILING'
    ;


LCM
    : 'LCM'
    ;


LN
    : 'LN'
    ;


LOG
    : 'LOG'
    ;


LOG2
   : 'LOG2'
   ;


LOG10
    : 'LOG10'
    ;


MDETERM
    : 'MDETERM'
    ;


MINVERSE
    : 'MINVERSE'
    ;


MMULT
    : 'MMULT'
    ;


MOD
    : 'MOD'
    ;


MROUND
    : 'MROUND'
    ;


MULTINOMIAL
    : 'MULTINOMIAL'
    ;


MUNIT
    : 'MUNIT'
    ;


ODD
    : 'ODD'
    ;


POWER
    : 'POWER' | 'POW'
    ;


PRODUCT
    : 'PRODUCT'
    ;


QUOTIENT
    : 'QUOTIENT'
    ;


RADIANS
    : 'RAD' | 'RADIANS'
    ;


RAND
    : 'RAND'
    ;


RANDBETWEEN
    : 'RANDBETWEEN'
    ;


ROMAN
    : 'ROMAN'
    ;


ROUND
    : 'ROUND'
    ;


ROUNDDOWN
    : 'ROUNDDOWN'
    ;


ROUNDUP
    : 'ROUNDUP'
    ;


SEC
    : 'SEC'
    ;


SECH
    : 'SECH'
    ;


SERIESSUM
    : 'SERIESSUM'
    ;


NUMSIGN
    : 'NUMSIGN' | 'SIGN'
    ;


SIN
    : 'SIN'
    ;


SINH
    : 'SINH'
    ;


SQRT
    : 'SQRT'
    ;


SQRTPI
    : 'SQRTPI'
    ;


SUBTOTAL
    : 'SUBTOTAL'
    ;


SUM
    : 'SUM'
    ;


SUMIF
    : 'SUMIF'
    ;


SUMIFS
    : 'SUMIFS'
    ;


SUMPRODUCT
    : 'SUMPRODUCT'
    ;


SUMSQ
    : 'SUMSQ'
    ;


SUMX2MY2
    : 'SUMX2MY2'
    ;


SUMX2PY2
    : 'SUMX2PY2'
    ;


SUMXMY2
    : 'SUMXMY2'
    ;


TAN
    : 'TAN'
    ;


TANH
    : 'TANH'
    ;


TRUNC
    : 'TRUNC' | 'TRUNCATE'
    ;

//  TODO category


CBRT
   : 'CBRT'
   ;


HYPOT
   : 'HYPOT'
   ;


ROOT
   : 'ROOT'
   ;


FACTORIAL
   : 'FACTORIAL'
   ;


BERNOULLI
   : 'BERNOULLI'
   ;


MANTISSA
   : 'MANTISSA'
   ;


INTEGRAL
   : 'INTEGRAL'
   ;


FRACTION
   : 'FRACTION'
   ;


AVG
   : 'AVG'
   ;


MEAN
   : 'MEAN'
   ;


RANGE
   : 'RANGE'
   ;


MODE
   : 'MODE'
   ;


MULTIPLY
   : 'MULTIPLY'
   ;


DIVIDE
   : 'DIVIDE'
   ;


// textual
ASC
    : 'ASC'
    ;


BAHTTEXT
    : 'BAHTTEXT'
    ;


CHAR
    : 'CHAR'
    ;


CLEAN
    : 'CLEAN'
    ;


CODE
    : 'CODE'
    ;


CONCAT
    : 'CONCAT'
    ;


CONCATENATE
    : 'CONCATENATE'
    ;


DBCS
    : 'DBCS'
    ;


DOLLAR
    : 'DOLLAR'
    ;


EXACT
    : 'EXACT'
    ;


FIND
    : 'FIND'
    ;


FINDB
    : 'FINDB'
    ;


FIXED
    : 'FIXED'
    ;


LEFT
    : 'LEFT'
    ;


LEFTB
    : 'LEFTB'
    ;


LEN
    : 'LEN'
    ;


LENB
    : 'LENB'
    ;


LOWER
    : 'LOWER'
    ;


MID
    : 'MID'
    ;


MIDB
    : 'MIDB'
    ;


NUMBERVALUE
    : 'NUMBERVALUE'
    ;


PHONETIC
    : 'PHONETIC'
    ;


PROPER
    : 'PROPER'
    ;


REPLACE
    : 'REPLACE'
    ;


REPLACEB
    : 'REPLACEB'
    ;


REPT
    : 'REPT'
    ;


RIGHT
    : 'RIGHT'
    ;


RIGHTB
    : 'RIGHTB'
    ;


SEARCH
    : 'SEARCH'
    ;


SEARCHB
    : 'SEARCHB'
    ;


SUBSTITUTE
    : 'SUBSTITUTE'
    ;


T
    : 'T'
    ;


TEXT
    : 'TEXT'
    ;


TEXTJOIN
    : 'TEXTJOIN'
    ;


TRIM
    : 'TRIM'
    ;


UNICHAR
    : 'UNICHAR'
    ;


UNICODE
    : 'UNICODE'
    ;


UPPER
    : 'UPPER'
    ;


VALUE
    : 'VALUE'
    ;


// additional textual
FORMAT
   : 'FORMAT'
   ;


LTRIM
   : 'LTRIM'
   ;


RTRIM
   : 'RTRIM'
   ;


CHOP
   : 'CHOP'
   ;


REPEAT
   : 'REPEAT'
   ;


PREFIX
   : 'PREFIX'
   ;


SUFFIX
   : 'SUFFIX'
   ;


CAPITALIZE
   : 'CAPITALIZE'
   ;


CAPITALIZE_FULLY
   : 'CAPITALIZEFULLY'
   ;


DECAPITALIZE
   : 'DECAPITALIZE'
   ;


DECAPITALIZE_FULLY
   : 'DECAPITALIZEFULLY'
   ;


SUBSTRING
   : 'SUBSTRING'
   ;


REMOVE
   : 'REMOVE'
   ;


REMOVE_FIRST
   : 'REMOVEFIRST'
   ;


REMOVE_LAST
   : 'REMOVELAST'
   ;


REPLACE_FIRST
   : 'REPLACEFIRST'
   ;


REPLACE_LAST
   : 'REPLACELAST'
   ;


REVERSE
   : 'REVERSE'
   ;


INITIALS
   : 'INITIALS'
   ;


SWAP
   : 'SWAP'
   ;


QUOTE
   : 'QUOTE'
   ;


UNQUOTE
   : 'UNQUOTE'
   ;


ENCODE
   : 'ENCODE'
   ;


DECODE
   : 'DECODE'
   ;


RANDOM
   : 'RANDOM'
   ;


ATOB
   : 'ATOB'
   ;


BTOA
   : 'BTOA'
   ;


HEX
   : 'HEX'
   ;


MD2
   : 'MD2'
   ;


MD5
   : 'MD5'
   ;


SHA256
   : 'SHA256'
   ;


CRC32
   : 'CRC32'
   ;


HTML2TEXT
   : 'HTML2TEXT'
   ;


// logical
AND
   : 'AND'
   ;


FALSE
   : 'FALSE' | 'false'
   ;


IF
   : 'IF'
   ;


IFERROR
   : 'IFERROR'
   ;


IFNA
   : 'IFNA'
   ;


IFS
   : 'IFS'
   ;


NOT
   : 'NOT'
   ;


OR
   : 'OR'
   ;


SWITCH
   : 'SWITCH'
   ;


TRUE
   : 'TRUE' | 'true'
   ;


XOR
   : 'XOR'
   ;


// additional logical
ISNULL
   : 'ISNULL' | 'NULL'
   ;


ISNOTNULL
   : 'ISNOTNULL' | 'NOTNULL'
   ;


// date & time
DATE
   : 'DATE'
   ;


DATEDIF
   : 'DATEDIF'
   ;


DATEVALUE
   : 'DATEVALUE'
   ;


DAY
   : 'DAY'
   ;


DAYS
   : 'DAYS'
   ;


DAYS360
   : 'DAYS360'
   ;


EDATE
   : 'EDATE'
   ;


EOMONTH
   : 'EOMONTH'
   ;


HOUR
   : 'HOUR'
   ;


ISOWEEKNUM
   : 'ISOWEEKNUM'
   ;


MINUTE
   : 'MINUTE'
   ;


MONTH
   : 'MONTH'
   ;


NETWORKDAYS
   : 'NETWORKDAYS'
   ;


NETWORKDAYS_INTL
   : 'NETWORKDAYS.INTL'
   ;


NOW
   : 'NOW'
   ;


SECOND
   : 'SECOND'
   ;


TIME
   : 'TIME'
   ;


TIMEVALUE
   : 'TIMEVALUE'
   ;


TODAY
   : 'TODAY'
   ;


WEEKDAY
   : 'WEEKDAY'
   ;


WEEKNUM
   : 'WEEKNUM'
   ;


WORKDAY
   : 'WORKDAY'
   ;


WORKDAY_INTL
   : 'WORKDAY.INTL'
   ;


YEAR
   : 'YEAR'
   ;


YEARFRAC
   : 'YEARFRAC'
   ;


// additional date & time
WEEK
   : 'WEEK'
   ;


TIMENOW
   : 'TIMENOW'
   ;


DATETIMEVALUE
   : 'DATETIMEVALUE'
   ;


// engineering
BESSELI
   : 'BESSELI'
   ;


BESSELJ
   : 'BESSELJ'
   ;


BESSELK
   : 'BESSELK'
   ;


BESSELY
   : 'BESSELY'
   ;


BIN2DEC
   : 'BIN2DEC'
   ;


BIN2HEX
   : 'BIN2HEX'
   ;


BITAND
   : 'BITAND'
   ;


BITLSHIFT
   : 'BITLSHIFT'
   ;


BITOR
   : 'BITOR'
   ;


BITRSHIFT
   : 'BITRSHIFT'
   ;


BITXOR
   : 'BITXOR'
   ;


COMPLEX
   : 'COMPLEX'
   ;


CONVERT
   : 'CONVERT'
   ;


DEC2BIN
   : 'DEC2BIN'
   ;


DEC2HEX
   : 'DEC2HEX'
   ;


DEC2OCT
   : 'DEC2OCT'
   ;


DELTA
   : 'DELTA'
   ;


ERF
   : 'ERF'
   ;


ERF_PRECISE
   : 'ERF.PRECISE'
   ;


ERFC
   : 'ERFC'
   ;


ERFC_PRECISE
   : 'ERFC.PRECISE'
   ;


GESTEP
   : 'GESTEP'
   ;


HEX2BIN
   : 'HEX2BIN'
   ;


HEX2DEC
   : 'HEX2DEC'
   ;


HEX2OCT
   : 'HEX2OCT'
   ;


IMABS
   : 'IMABS'
   ;


IMAGINARY
   : 'IMAGINARY'
   ;


IMARGUMENT
   : 'IMARGUMENT'
   ;


IMCONJUGATE
   : 'IMCONJUGATE'
   ;


IMCOS
   : 'IMCOS'
   ;


IMCOSH
   : 'IMCOSH'
   ;


IMCOT
   : 'IMCOT'
   ;


IMCSC
   : 'IMCSC'
   ;


IMCSCH
   : 'IMCSCH'
   ;


IMDIV
   : 'IMDIV'
   ;


IMEXP
   : 'IMEXP'
   ;


IMLN
   : 'IMLN'
   ;


IMLOG10
   : 'IMLOG10'
   ;


IMLOG2
   : 'IMLOG2'
   ;


IMPOWER
   : 'IMPOWER'
   ;


IMPRODUCT
   : 'IMPRODUCT'
   ;


IMREAL
   : 'IMREAL'
   ;


IMSEC
   : 'IMSEC'
   ;


IMSECH
   : 'IMSECH'
   ;


IMSIN
   : 'IMSIN'
   ;


IMSINH
   : 'IMSINH'
   ;


IMSQRT
   : 'IMSQRT'
   ;


IMSUB
   : 'IMSUB'
   ;


IMSUM
   : 'IMSUM'
   ;


IMTAN
   : 'IMTAN'
   ;


OCT2BIN
   : 'OCT2BIN'
   ;


OCT2DEC
   : 'OCT2DEC'
   ;


OCT2HEX
   : 'OCT2HEX'
   ;


// additional engineering
BIN2OCT
    : 'BIN2OCT'
    ;


// financial
ACCRINT
   : 'ACCRINT'
   ;


ACCRINTM
   : 'ACCRINTM'
   ;


AMORDEGRC
   : 'AMORDEGRC'
   ;


AMORLINC
   : 'AMORLINC'
   ;


COUPDAYBS
   : 'COUPDAYBS'
   ;


COUPDAYS
   : 'COUPDAYS'
   ;


COUPDAYSNC
   : 'COUPDAYSNC'
   ;


COUPNCD
   : 'COUPNCD'
   ;


COUPNUM
   : 'COUPNUM'
   ;


COUPPCD
   : 'COUPPCD'
   ;


CUMIPMT
   : 'CUMIPMT'
   ;


CUMPRINC
   : 'CUMPRINC'
   ;


DB
   : 'DB'
   ;


DDB
   : 'DDB'
   ;


DISC
   : 'DISC'
   ;


DOLLARDE
   : 'DOLLARDE'
   ;


DOLLARFR
   : 'DOLLARFR'
   ;


DURATION
   : 'DURATION'
   ;


EFFECT
   : 'EFFECT'
   ;


FV
   : 'FV'
   ;


FVSCHEDULE
   : 'FVSCHEDULE'
   ;


INTRATE
   : 'INTRATE'
   ;


IPMT
   : 'IPMT'
   ;


IRR
   : 'IRR'
   ;


ISPMT
   : 'ISPMT'
   ;


MDURATION
   : 'MDURATION'
   ;


MIRR
   : 'MIRR'
   ;


NOMINAL
   : 'NOMINAL'
   ;


NPER
   : 'NPER'
   ;


NPV
   : 'NPV'
   ;


ODDFPRICE
   : 'ODDFPRICE'
   ;


ODDFYIELD
   : 'ODDFYIELD'
   ;


ODDLPRICE
   : 'ODDLPRICE'
   ;


ODDLYIELD
   : 'ODDLYIELD'
   ;


PDURATION
   : 'PDURATION'
   ;


PMT
   : 'PMT'
   ;


PPMT
   : 'PPMT'
   ;


PRICE
   : 'PRICE'
   ;


PRICEDISC
   : 'PRICEDISC'
   ;


PRICEMAT
   : 'PRICEMAT'
   ;


PV
   : 'PV'
   ;


RATE
   : 'RATE'
   ;


RECEIVED
   : 'RECEIVED'
   ;


RRI
   : 'RRI'
   ;


SLN
   : 'SLN'
   ;


SYD
   : 'SYD'
   ;


TBILLEQ
   : 'TBILLEQ'
   ;


TBILLPRICE
   : 'TBILLPRICE'
   ;


TBILLYIELD
   : 'TBILLYIELD'
   ;


VDB
   : 'VDB'
   ;


XIRR
   : 'XIRR'
   ;


XNPV
   : 'XNPV'
   ;


YIELD
   : 'YIELD'
   ;


YIELDDISC
   : 'YIELDDISC'
   ;


YIELDMAT
   : 'YIELDMAT'
   ;


// database
DAVERAGE
   : 'DAVERAGE'
   ;


DCOUNT
   : 'DCOUNT'
   ;


DCOUNTA
   : 'DCOUNTA'
   ;


DGET
   : 'DGET'
   ;


DMAX
   : 'DMAX'
   ;


DMIN
   : 'DMIN'
   ;


DPRODUCT
   : 'DPRODUCT'
   ;


DSTDEV
   : 'DSTDEV'
   ;


DSTDEVP
   : 'DSTDEVP'
   ;


DSUM
   : 'DSUM'
   ;


DVAR
   : 'DVAR'
   ;


DVARP
   : 'DVARP'
   ;


// cube
CUBEKPIMEMBER
   : 'CUBEKPIMEMBER'
   ;


CUBEMEMBER
   : 'CUBEMEMBER'
   ;


CUBEMEMBERPROPERTY
   : 'CUBEMEMBERPROPERTY'
   ;


CUBERANKEDMEMBER
   : 'CUBERANKEDMEMBER'
   ;


CUBESET
   : 'CUBESET'
   ;


CUBESETCOUNT
   : 'CUBESETCOUNT'
   ;


CUBEVALUE
   : 'CUBEVALUE'
   ;


// information
CELL
   : 'CELL'
   ;


ERROR_TYPE
   : 'ERROR.TYPE'
   ;


INFO
   : 'INFO'
   ;


ISBLANK
   : 'ISBLANK'
   ;


ISERR
   : 'ISERR'
   ;


ISERROR
   : 'ISERROR'
   ;


ISEVEN
   : 'ISEVEN'
   ;


ISFORMULA
   : 'ISFORMULA'
   ;


ISLOGICAL
   : 'ISLOGICAL'
   ;


ISNA
   : 'ISNA'
   ;


ISNONTEXT
   : 'ISNONTEXT'
   ;


ISNUMBER
   : 'ISNUMBER'
   ;


ISODD
   : 'ISODD'
   ;


ISREF
   : 'ISREF'
   ;


ISTEXT
   : 'ISTEXT'
   ;


N
   : 'N'
   ;


NA
   : 'NA'
   ;


SHEET
   : 'SHEET'
   ;


SHEETS
   : 'SHEETS'
   ;


TYPE
   : 'TYPE'
   ;


// lookup & refernce
ADDRESS
   : 'ADDRESS'
   ;


AREAS
   : 'AREAS'
   ;


CHOOSE
   : 'CHOOSE'
   ;


COLUMN
   : 'COLUMN'
   ;


COLUMNS
   : 'COLUMNS'
   ;


FORMULATEXT
   : 'FORMULATEXT'
   ;


GETPIVOTDATA
   : 'GETPIVOTDATA'
   ;


HLOOKUP
   : 'HLOOKUP'
   ;


HYPERLINK
   : 'HYPERLINK'
   ;


INDEX
   : 'INDEX'
   ;


INDIRECT
   : 'INDIRECT'
   ;


LOOKUP
   : 'LOOKUP'
   ;


MATCH
   : 'MATCH'
   ;


OFFSET
   : 'OFFSET'
   ;


ROW
   : 'ROW'
   ;


ROWS
   : 'ROWS'
   ;


RTD
   : 'RTD'
   ;


TRANSPOSE
   : 'TRANSPOSE'
   ;


VLOOKUP
   : 'VLOOKUP'
   ;


// statistical
AVEDEV
    : 'AVEDEV'
    ;


AVERAGE
    : 'AVERAGE'
    ;


AVERAGEA
    : 'AVERAGEA'
    ;


AVERAGEIF
    : 'AVERAGEIF'
    ;


AVERAGEIFS
    : 'AVERAGEIFS'
    ;


BETA_DIST
    : 'BETA.DIST'
    ;


BETA_INV
    : 'BETA.INV'
    ;


BINOM_DIST
    : 'BINOM.DIST'
    ;


BINOM_DIST_RANGE
    : 'BINOM.DIST.RANGE'
    ;


BINOM_INV
    : 'BINOM.INV'
    ;


CHISQ_DIST
    : 'CHISQ.DIST'
    ;


CHISQ_DIST_RT
    : 'CHISQ.DIST.RT'
    ;


CHISQ_INV
    : 'CHISQ.INV'
    ;


CHISQ_INV_RT
    : 'CHISQ.INV.RT'
    ;


CHISQ_TEST
    : 'CHISQ.TEST'
    ;


CONFIDENCE_NORM
    : 'CONFIDENCE.NORM'
    ;


CONFIDENCE_T
    : 'CONFIDENCE.T'
    ;


CORREL
    : 'CORREL'
    ;


COUNT
    : 'COUNT'
    ;


COUNTA
    : 'COUNTA'
    ;


COUNTBLANK
    : 'COUNTBLANK'
    ;


COUNTIF
    : 'COUNTIF'
    ;


COUNTIFS
    : 'COUNTIFS'
    ;


COVARIANCE_P
    : 'COVARIANCE.P'
    ;


COVARIANCE_S
    : 'COVARIANCE.S'
    ;


DEVSQ
    : 'DEVSQ'
    ;


EXPON_DIST
    : 'EXPON.DIST'
    ;


F_DIST
    : 'F.DIST'
    ;


F_DIST_RT
    : 'F.DIST.RT'
    ;


F_INV
    : 'F.INV'
    ;


F_INV_RT
    : 'F.INV.RT'
    ;


F_TEST
    : 'F.TEST'
    ;


FISHER
    : 'FISHER'
    ;


FISHERINV
    : 'FISHERINV'
    ;


FORECAST
    : 'FORECAST'
    ;


FORECAST_ETS
    : 'FORECAST.ETS'
    ;


FORECAST_ETS_CONFINT
    : 'FORECAST.ETS.CONFINT'
    ;


FORECAST_ETS_SEASONALITY
    : 'FORECAST.ETS.SEASONALITY'
    ;


FORECAST_ETS_STAT
    : 'FORECAST.ETS.STAT'
    ;


FORECAST_LINEAR
    : 'FORECAST.LINEAR'
    ;


FREQUENCY
    : 'FREQUENCY'
    ;


GAMMA
    : 'GAMMA'
    ;


GAMMA_DIST
    : 'GAMMA.DIST'
    ;


GAMMA_INV
    : 'GAMMA.INV'
    ;


GAMMALN
    : 'GAMMALN'
    ;


GAMMALN_PRECISE
    : 'GAMMALN.PRECISE'
    ;


GAUSS
    : 'GAUSS'
    ;


GEOMEAN
    : 'GEOMEAN'
    ;


GROWTH
    : 'GROWTH'
    ;


HARMEAN
    : 'HARMEAN'
    ;


HYPGEOM_DIST
    : 'HYPGEOM.DIST'
    ;


INTERCEPT
    : 'INTERCEPT'
    ;


KURT
    : 'KURT'
    ;


LARGE
    : 'LARGE'
    ;


LINEST
    : 'LINEST'
    ;


LOGEST
    : 'LOGEST'
    ;


LOGNORM_DIST
    : 'LOGNORM.DIST'
    ;


LOGNORM_INV
    : 'LOGNORM.INV'
    ;


MAX
    : 'MAX'
    ;


MAXA
    : 'MAXA'
    ;


MAXIFS
    : 'MAXIFS'
    ;


MEDIAN
    : 'MEDIAN'
    ;


MIN
    : 'MIN'
    ;


MINA
    : 'MINA'
    ;


MINIFS
    : 'MINIFS'
    ;


MODE_MULT
    : 'MODE.MULT'
    ;


MODE_SNGL
    : 'MODE.SNGL'
    ;


NEGBINOM_DIST
    : 'NEGBINOM.DIST'
    ;


NORM_DIST
    : 'NORM.DIST'
    ;


NORM_INV
    : 'NORM.INV'
    ;


NORM_S_DIST
    : 'NORM.S.DIST'
    ;


NORM_S_INV
    : 'NORM.S.INV'
    ;


PEARSON
    : 'PEARSON'
    ;


PERCENTILE_EXC
    : 'PERCENTILE.EXC'
    ;


PERCENTILE_INC
    : 'PERCENTILE.INC'
    ;


PERCENTRANK_EXC
    : 'PERCENTRANK.EXC'
    ;


PERCENTRANK_INC
    : 'PERCENTRANK.INC'
    ;


PERMUT
    : 'PERMUT'
    ;


PERMUTATIONA
    : 'PERMUTATIONA'
    ;


PHI
    : 'PHI'
    ;


POISSON_DIST
    : 'POISSON.DIST'
    ;


PROB
    : 'PROB'
    ;


QUARTILE_EXC
    : 'QUARTILE.EXC'
    ;


QUARTILE_INC
    : 'QUARTILE.INC'
    ;


RANK_AVG
    : 'RANK.AVG'
    ;


RANK_EQ
    : 'RANK.EQ'
    ;


RSQ
    : 'RSQ'
    ;


SKEW
    : 'SKEW'
    ;


SKEW_P
    : 'SKEW.P'
    ;


SLOPE
    : 'SLOPE'
    ;


SMALL
    : 'SMALL'
    ;


STANDARDIZE
    : 'STANDARDIZE'
    ;


STDEV_P
    : 'STDEV.P'
    ;


STDEV_S
    : 'STDEV.S'
    ;


STDEVA
    : 'STDEVA'
    ;


STDEVPA
    : 'STDEVPA'
    ;


STEYX
    : 'STEYX'
    ;


T_DIST
    : 'T.DIST'
    ;


T_DIST_2T
    : 'T.DIST.2T'
    ;


T_DIST_RT
    : 'T.DIST.RT'
    ;


T_INV
    : 'T.INV'
    ;


T_INV_2T
    : 'T.INV.2T'
    ;


T_TEST
    : 'T.TEST'
    ;


TREND
    : 'TREND'
    ;


TRIMMEAN
    : 'TRIMMEAN'
    ;


VAR_P
    : 'VAR.P'
    ;


VAR_S
    : 'VAR.S'
    ;


VARA
    : 'VARA'
    ;


VARPA
    : 'VARPA'
    ;


WEIBULL_DIST
    : 'WEIBULL.DIST'
    ;


Z_TEST
    : 'Z.TEST'
    ;


// custom
// code
CODE_ISIBAN
    : 'CODE.ISIBAN'
    ;


CODE_ISISIN
    : 'CODE.ISISIN'
    ;


CODE_ISISSN
    : 'CODE.ISISSN'
    ;


CODE_ISISBN
    : 'CODE.ISISBN'
    ;


CODE_ISISBN10
    : 'CODE.ISISBN10'
    ;


CODE_ISISBN13
    : 'CODE.ISISBN13'
    ;


CODE_ISEAN
    : 'CODE.ISEAN'
    ;


CODE_ISEAN8
    : 'CODE.ISEAN8'
    ;


CODE_ISEAN13
    : 'CODE.ISEAN13'
    ;


CODE_ISUPCA
    : 'CODE.ISUPCA'
    ;


CODE_ISUPCE
    : 'CODE.ISUPCE'
    ;


CODE_ISSAFEHTML
    : 'CODE.ISSAFEHTML'
    ;


CODE_ISEMAIL
    : 'CODE.ISEMAIL'
    ;


CODE_ISREGEX
    : 'CODE.ISREGEX'
    ;


CODE_ISIP
    : 'CODE.ISIP'
    ;


CODE_ISIPV4
    : 'CODE.ISIPV4'
    ;


CODE_ISIPV6
    : 'CODE.ISIPV6'
    ;


CODE_ISURL
    : 'CODE.ISURL'
    ;


CODE_ISURN
    : 'CODE.ISURN'
    ;


CODE_ISDN
    : 'CODE.ISDN'
    ;


CODE_ISTLD
    : 'CODE.ISTLD'
    ;

CODE_ISCREDITCARD
    : 'CODE.ISCREDITCARD'
    ;


CODE_ISMOD10
    : 'CODE.ISMOD10'
    ;


CODE_ISMOD11
    : 'CODE.ISMOD11'
    ;


CODE_IBAN
    : 'CODE.IBAN'
    ;


CODE_ISIN
    : 'CODE.ISIN'
    ;


CODE_ISSN
    : 'CODE.ISSN'
    ;


CODE_ISBN
    : 'CODE.ISBN'
    ;


CODE_ISBN10
    : 'CODE.ISBN10'
    ;


CODE_ISBN13
    : 'CODE.ISBN13'
    ;


CODE_EAN
    : 'CODE.EAN'
    ;


CODE_EAN8
    : 'CODE.EAN8'
    ;


CODE_EAN13
    : 'CODE.EAN13'
    ;


CODE_UPCA
    : 'CODE.UPCA'
    ;


CODE_UPCE
    : 'CODE.UPCE'
    ;


CODE_IBAN_CD
    : 'CODE.IBAN.CD'
    ;


CODE_ISIN_CD
    : 'CODE.ISIN.CD'
    ;


CODE_ISSN_CD
    : 'CODE.ISSN.CD'
    ;


CODE_ISBN_CD
    : 'CODE.ISBN.CD'
    ;


CODE_ISBN10_CD
    : 'CODE.ISBN10.CD'
    ;


CODE_ISBN13_CD
    : 'CODE.ISBN13.CD'
    ;


CODE_EAN_CD
    : 'CODE.EAN.CD'
    ;


CODE_EAN8_CD
    : 'CODE.EAN8.CD'
    ;


CODE_EAN13_CD
    : 'CODE.EAN13.CD'
    ;


CODE_UPCA_CD
    : 'CODE.UPCA.CD'
    ;


CODE_UPCE_CD
    : 'CODE.UPCE.CD'
    ;


CODE_ISCNPJ
    : 'CODE.ISCNPJ'
    ;


CODE_ISCPF
    : 'CODE.ISCPF'
    ;


CODE_ISTITULO_ELEITORAL
    : 'CODE.ISTITULO.ELEITORAL'
    ;


CODE_ISNIP
    : 'CODE.ISNIP'
    ;


CODE_ISPESEL
    : 'CODE.ISPESEL'
    ;


CODE_ISREGON
    : 'CODE.ISREGON'
    ;


CODE_ISJSON
    : 'CODE.ISJSON'
    ;


CODE_ISXML
    : 'CODE.ISXML'
    ;


CODE_ISYAML
    : 'CODE.ISYAML'
    ;


// operators

LPAREN
   : '('
   ;


RPAREN
   : ')'
   ;


AMPERSAND
   : '&'
   ;


PLUS
   : '+'
   ;


MINUS
   : '-'
   ;


TIMES
   : '*'
   ;


DIV
   : '/'
   ;


GT
   : '>'
   ;


GTE
   : '>='
   ;


LT
   : '<'
   ;


LTE
   : '<='
   ;


EQ
   : '='
   ;


NEQ
   : '!=' | '<>'
   ;


PCT
   : '%'
   ;


COMMA
   : ','
   ;


POINT
   : '.'
   ;


POW
   : '^'
   ;


PI
   : 'PI' | 'pi'
   ;


EULER
   : E2
   ;


I
   : 'i'
   ;


VARIABLE
   : VALID_ID_START VALID_ID_CHAR*
   ;


fragment VALID_ID_START
   : ('a' .. 'z') | ('A' .. 'Z') | '_'
   ;


fragment VALID_ID_CHAR
   : VALID_ID_START | ('0' .. '9')
   ;


SCIENTIFIC_NUMBER
   : NUMBER ((E1 | E2) SIGN? NUMBER)?
   ;


fragment NUMBER
   : ('0' .. '9') + ('.' ('0' .. '9') +)?
   ;


STRING
   : ('"'|'\'') (('""' | '\'\'') | ~ ('"' | '\''))* ('"' | '\'')
   ;


fragment E1
   : 'E'
   ;


fragment E2
   : 'e'
   ;


fragment SIGN
   : ('+' | '-')
   ;


WS
   : [ \r\n\t] + -> skip
   ;