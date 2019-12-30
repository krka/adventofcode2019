var tmp

func foo a b
add tmp a b
return tmp
endfunc

var x
var y
var z

add x 100 0
add y 23 0

call foo x y : z
output z
halt
